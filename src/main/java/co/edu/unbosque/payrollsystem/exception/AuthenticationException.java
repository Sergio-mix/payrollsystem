package co.edu.unbosque.payrollsystem.exception;

import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;

/**
 * Exception for authentication
 */
public class AuthenticationException extends Exception implements Serializable {

    @Serial
    @Getter
    private static final long serialVersionUID = 7004554L;

    /**
     * Constructor
     */
    public AuthenticationException(final String message) {
        super(message);
    }
}