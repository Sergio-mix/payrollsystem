package co.edu.unbosque.payrollsystem.exception;

import co.edu.unbosque.payrollsystem.dto.PayrollFile;
import co.edu.unbosque.payrollsystem.dto.PayrollFileData;
import co.edu.unbosque.payrollsystem.dto.ValidateError;
import lombok.Getter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public class PayrollException extends Exception implements Serializable {

    @Serial
    private static final long serialVersionUID = 34433432323L;

    @Getter
    private final PayrollFile payrollFile;

    public PayrollException(PayrollFile payrollFile) {
        this.payrollFile = payrollFile;
    }
}
