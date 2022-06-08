package co.edu.unbosque.payrollsystem.dto;

import lombok.Data;

import java.util.List;

@Data
public class ValidatePayrollFile {
    private PayrollFileData payrollFileData;

    private List<ValidateError> validateError;
}

