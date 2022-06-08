package co.edu.unbosque.payrollsystem.exception;

import co.edu.unbosque.payrollsystem.dto.ValidateError;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * The Class UserException.
 */
public class UserException extends Exception implements Serializable {
    @Serial
    @Getter
    private static final long serialVersionUID = 701546L;

    /**
     * The errors.
     */
    @Getter
    private List<ValidateError> errors;

    /**
     * Constructor
     */
    public UserException(final List<ValidateError> errors) {
        this.errors = errors;
    }

    /**
     * Constructor
     */
    public UserException(final String message) {
        super(message);
    }
}