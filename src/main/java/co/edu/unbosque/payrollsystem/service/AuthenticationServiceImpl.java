package co.edu.unbosque.payrollsystem.service;

import co.edu.unbosque.payrollsystem.component.CodeComponent;
import co.edu.unbosque.payrollsystem.component.DateCalendarComponent;
import co.edu.unbosque.payrollsystem.component.JWTTokenHelperComponent;
import co.edu.unbosque.payrollsystem.dto.DataLoginToken;
import co.edu.unbosque.payrollsystem.dto.RecoveryCodeStatus;
import co.edu.unbosque.payrollsystem.dto.ReplyMessage;
import co.edu.unbosque.payrollsystem.dto.ValidateError;
import co.edu.unbosque.payrollsystem.exception.AuthenticationException;
import co.edu.unbosque.payrollsystem.exception.UserException;
import co.edu.unbosque.payrollsystem.model.User;
import co.edu.unbosque.payrollsystem.repository.UserRepository;
import co.edu.unbosque.payrollsystem.service.validation.AuthenticationValidationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;


@Service(value = "AuthenticationServiceImpl")
public class AuthenticationServiceImpl {

    @Autowired
    private DateCalendarComponent dateCalendar;

    @Autowired
    private CodeComponent code;

    @Value("${auth.user.code_length}")
    private Integer codeLength;

    @Value("${auth.user.time_recovery_code}")
    private Integer time;

    @Autowired
    private EmailSenderServiceImpl mailService;

    @Autowired
    private AuthenticationValidationServiceImpl authenticationValidation;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTTokenHelperComponent jWTTokenHelper;

    public DataLoginToken loginUser(String username, String password, HttpServletRequest request) throws AuthenticationException {
        try {
            DataLoginToken response = null;
            authenticationValidation.validateAuthentication(username, password);//validate user
            final Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            User user = (User) authentication.getPrincipal();
            if (user != null) {
                String token = jWTTokenHelper.generateToken(user.getUsername());
                response = new DataLoginToken(user.getId(), user.getUsername(), token);
            }
            return response;
        } catch (AuthenticationException e) {
            if (AuthenticationValidationServiceImpl.EXPIRED.equals(e.getMessage())) {
                resetPassword(username, request);
            }
            throw new AuthenticationException(e.getMessage());
        }
    }

    public void resetPassword(final String username, final HttpServletRequest request) {
        Optional<User> user = authenticationValidation.getUserRepository().findByUsername(username);
        if (user.isPresent() && user.get().getEnabled()) {
            sendRecoveryCode(user, request);
        }
    }

    public String recoverPassword(final String username, final HttpServletRequest request) {
        Optional<User> user = authenticationValidation.getUserRepository().findByUsername(username);
        if (user.isPresent() && authenticationValidation.isAdmin(user.get())) {
            sendRecoveryCode(user, request);
        }
        return ReplyMessage.RECOVER_PASSWORD;
    }

    private void sendRecoveryCode(Optional<User> user, final HttpServletRequest request) {
        if (time != null && time > 0) {
            user.get().setRecoveryCode(code.getRandomString(codeLength));
            user.get().setRecoveryCodeExpiration(dateCalendar.addMinutesCurrentDate(time));
            user = authenticationValidation.getUserRepository().save(user.get());
            mailService.sendMailRecoveryCode(user.get().getEmail(), user.get().getUsername(), user.get().getId(), user.get().getRecoveryCode(), request, time);
        }
    }

    public RecoveryCodeStatus verificateRecoveryCode(final int id, final String recoveryCode, final String email) {
        RecoveryCodeStatus result;
        Optional<User> user = authenticationValidation.getUserRepository().findById(id);
        if (user.isEmpty() || !user.get().getEmail().equals(email)) {
            result = new RecoveryCodeStatus(ReplyMessage.EMAIL_INVALID);
        } else {
            if (!user.get().getEnabled() && !authenticationValidation.isAdmin(user.get()) && (user.get().getState().equals(User.ACTIVE))) {
                result = new RecoveryCodeStatus(ReplyMessage.USER_NOT_ENABLED);
            } else {
                return user.filter(value -> !dateCalendar.isDateExpired(value.getRecoveryCodeExpiration())).map(value -> value.getRecoveryCode().equals(recoveryCode)
                        ? new RecoveryCodeStatus(ReplyMessage.VALID_CODE, value.getUsername())
                        : new RecoveryCodeStatus(ReplyMessage.INVALID_CODE)).orElseGet(() ->
                        new RecoveryCodeStatus(ReplyMessage.CODE_EXPIRED));
            }
        }
        return result;
    }

    public RecoveryCodeStatus updatePassword(final int id, final String password) throws UserException {
        RecoveryCodeStatus result;
        ValidateError validation = authenticationValidation.validatePassword(password);//Validate password
        if (validation != null) {
            throw new UserException(validation.getMessage());
        }

        Optional<User> user = authenticationValidation.getUserRepository().findById(id);
        if (user.isEmpty()) {
            result = new RecoveryCodeStatus(ReplyMessage.INVALID_DATA);
        } else {
            String pass = DigestUtils.md5DigestAsHex(password.getBytes());
            if (pass.equals(user.get().getPassword())) {
                result = new RecoveryCodeStatus(ReplyMessage.PASSWORD_EQUALS);
            } else {
                user.get().setPassword(pass);
                user.get().setPasswordExpiration(dateCalendar.addDaysCurrentDate(user.get().getDaysToExpire()));
                user.get().setRecoveryCode(null);
                user.get().setRecoveryCodeExpiration(null);

                if (authenticationValidation.isAdmin(user.get()) && (user.get().getState().equals(User.ACTIVE))) {
                    user.get().setEnabled(true);
                }

                user = authenticationValidation.getUserRepository().save(user.get());
                result = user.map(value -> new RecoveryCodeStatus(ReplyMessage.PASSWORD_UPDATED, value.getUsername())).orElseGet(()
                        -> new RecoveryCodeStatus(ReplyMessage.PASSWORD_NOT_UPDATED));
            }
        }
        return result;
    }
}
