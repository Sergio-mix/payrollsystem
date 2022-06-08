package co.edu.unbosque.payrollsystem.service.validation;

import co.edu.unbosque.payrollsystem.exception.AuthenticationException;
import co.edu.unbosque.payrollsystem.model.Authority;
import co.edu.unbosque.payrollsystem.model.User;
import lombok.Data;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.Optional;

/**
 * The type Authentication validation service impl.
 */
@Service(value = "AuthenticationValidationServiceImpl")
@Data
public class AuthenticationValidationServiceImpl extends UserValidationServiceImpl {

    public static final String INCORRECT = "Check your credentials the username or password is incorrect 🔒";
    public static final String EXPIRED = "Password expired, we have sent an email with the instructions to renew your password \uD83D\uDCEC";
    public static final String IS_NOT_ENABLED = "Your user is disabled, please contact the administrator ❌";

    public void validateAuthentication(final String userName, final String password) throws AuthenticationException {
        final String result = usernameAndPassword(userName, password);
        if (result != null) {
            throw new AuthenticationException(result);
        }
    }

    /**
     * Username and password string.
     *
     * @param userName the username
     * @param password the password
     * @return the string
     */
    private String usernameAndPassword(final String userName, final String password) {
        final Optional<User> user = userName == null || userName.isEmpty() || password == null || password.isEmpty()
                ? Optional.empty() : getUserRepository().findByUsername(userName);//check if the user exists and if it does not exist returns null
        String result = null;

        if (user.isEmpty()) {
            result = INCORRECT;
        } else {
            if (!user.get().getPassword().equals(DigestUtils.md5DigestAsHex(password.getBytes()))) {
                addKeyAttempts(user.get());
                result = INCORRECT;
            } else {
                final var validateDate = user.get().getEnabled()
                        ? user.get().getPasswordExpiration().before(new Date()) ? EXPIRED : null : IS_NOT_ENABLED;//check if the user is enabled and if it is not enabled returns null
                if (validateDate != null) {
                    result = validateDate;
                } else {
                    if (user.get().getKeyAttempts() > 0) {
                        user.get().setKeyAttempts(0);
                        getUserRepository().save(user.get());
                    }
                }
            }
        }

        return result;
    }

    private void addKeyAttempts(User user) {
        final int attempts = user.getKeyAttempts() + 1;
        if (attempts > 3) {
            user.setEnabled(false);
            user.setKeyAttempts(0);
        } else {
            user.setKeyAttempts(attempts);
        }
        getUserRepository().save(user);
    }

    public Boolean isAdmin(final User user) {
        final Boolean[] isAdmin = {false};
        user.getAuthorities().forEach(e -> {
            if (e.getAuthority().equals(Authority.ADMIN)) {
                isAdmin[0] = true;
            }
        });
        return isAdmin[0];
    }
}