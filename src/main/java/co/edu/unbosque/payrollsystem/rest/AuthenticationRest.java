package co.edu.unbosque.payrollsystem.rest;

import co.edu.unbosque.payrollsystem.dto.DataLoginToken;
import co.edu.unbosque.payrollsystem.dto.RecoveryCodeStatus;
import co.edu.unbosque.payrollsystem.dto.ReplyMessage;
import co.edu.unbosque.payrollsystem.exception.AuthenticationException;
import co.edu.unbosque.payrollsystem.exception.UserException;
import co.edu.unbosque.payrollsystem.model.Record;
import co.edu.unbosque.payrollsystem.request.AuthenticationRequest;
import co.edu.unbosque.payrollsystem.request.RecoveryCode;
import co.edu.unbosque.payrollsystem.request.RecoveryUsername;
import co.edu.unbosque.payrollsystem.response.LoginResponse;
import co.edu.unbosque.payrollsystem.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

@RestController(value = "AuthenticationRest")
@RequestMapping("/auth/api/v1")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AuthenticationRest {
    @Autowired
    private UserHistoryServiceImpl recordUserService;
    @Autowired
    private AuthenticationServiceImpl authenticationService;

    @PostMapping(value = "/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest authenticationRequest, HttpServletRequest request) {
            ResponseEntity<?> response;
        try {
            DataLoginToken dataToken = authenticationService.loginUser(authenticationRequest.getUsername(), authenticationRequest.getPassword(), request);
            if (dataToken.getToken() != null) {
                recordUserService.save(authenticationRequest.getUsername(), new Record(Record.POST, "login"), null, request);//Save record
                response = new ResponseEntity<>(new LoginResponse(dataToken.getToken(), dataToken.getUserId()), HttpStatus.OK);
            } else {
                response = new ResponseEntity<>(ReplyMessage.NOT_LOGIN, HttpStatus.BAD_REQUEST);
            }
        } catch (AuthenticationException e) {
            response = new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            response = new ResponseEntity<>(ReplyMessage.ERROR_505, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    @PostMapping(value = "/verify-code")
    public ResponseEntity<?> verifyCode(@RequestBody RecoveryCode recoveryCode) {
        ResponseEntity<?> response;
        try {
            if (recoveryCode.getCode() != null && recoveryCode.getId() != null && !recoveryCode.getCode().isEmpty()
                    && !recoveryCode.getEmail().isEmpty() && recoveryCode.getEmail() != null) {
                RecoveryCodeStatus result = authenticationService.verificateRecoveryCode(recoveryCode.getId(), recoveryCode.getCode(), recoveryCode.getEmail());
                response = result.getUsername() != null
                        ? new ResponseEntity<>(result.getMessage(), HttpStatus.OK)
                        : new ResponseEntity<>(result.getMessage(), HttpStatus.BAD_REQUEST);
            } else {
                response = new ResponseEntity<>(ReplyMessage.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            response = new ResponseEntity<>(ReplyMessage.ERROR_505, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    @PutMapping(value = "/verify-code-change-password")
    @Transactional
    public ResponseEntity<?> updatePassword(final @RequestBody RecoveryCode recoveryCode, final HttpServletRequest request) {
        ResponseEntity<?> response;
        try {
            if (recoveryCode.getId() == null || recoveryCode.getPassword() == null || recoveryCode.getPassword().isEmpty()
                    || recoveryCode.getEmail().isEmpty() || recoveryCode.getEmail() == null) {
                response = new ResponseEntity<>(ReplyMessage.INVALID_DATA, HttpStatus.BAD_REQUEST);
            } else {
                RecoveryCodeStatus recoveryCodeStatus = authenticationService.verificateRecoveryCode(recoveryCode.getId(), recoveryCode.getCode(), recoveryCode.getEmail());
                if (recoveryCodeStatus.getUsername() == null) {
                    response = new ResponseEntity<>(recoveryCodeStatus.getMessage(), HttpStatus.BAD_REQUEST);
                } else {
                    RecoveryCodeStatus resultUpdate = authenticationService.updatePassword(recoveryCode.getId(), recoveryCode.getPassword());

                    if (resultUpdate.getUsername() == null) {
                        response = new ResponseEntity<>(resultUpdate.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
                    } else {
                        recordUserService.save(resultUpdate.getUsername(), new Record(Record.PUT, "update password"), null, request);
                        response = new ResponseEntity<>(resultUpdate.getMessage(), HttpStatus.OK);
                    }
                }
            }
        } catch (UserException e) {
            response = new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            response = new ResponseEntity<>(ReplyMessage.ERROR_505, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    @PostMapping(value = "/recovery-password")
    public ResponseEntity<?> recoverPassword(final @RequestBody RecoveryUsername recoveryUsername, final HttpServletRequest request) {
        ResponseEntity<?> response;
        try {
            if (recoveryUsername.getUsername() != null && !recoveryUsername.getUsername().isEmpty()) {
                response = new ResponseEntity<>(authenticationService.recoverPassword(recoveryUsername.getUsername(), request), HttpStatus.OK);
            } else {
                response = new ResponseEntity<>(ReplyMessage.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            response = new ResponseEntity<>(ReplyMessage.ERROR_505, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }
}