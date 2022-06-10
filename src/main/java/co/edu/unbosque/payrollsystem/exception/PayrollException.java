package co.edu.unbosque.payrollsystem.exception;

import co.edu.unbosque.payrollsystem.dto.PayrollFile;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;

public class PayrollException extends Exception implements Serializable {

    @Serial
    @Getter
    private static final long serialVersionUID = 34343432L;

    @Getter
    private PayrollFile payrollFile;

    public PayrollException(PayrollFile payrollFile) {
        this.payrollFile = payrollFile;
    }

    public PayrollException(String message) {
        super(message);
    }
}
