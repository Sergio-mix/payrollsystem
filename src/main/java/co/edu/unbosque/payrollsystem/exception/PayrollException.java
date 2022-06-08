package co.edu.unbosque.payrollsystem.exception;

import java.io.Serial;
import java.io.Serializable;

public class PayrollException extends Exception implements Serializable {

    @Serial
    private static final long serialVersionUID = 70156789546L;

    public PayrollException(String message) {
        super(message);
    }
}
