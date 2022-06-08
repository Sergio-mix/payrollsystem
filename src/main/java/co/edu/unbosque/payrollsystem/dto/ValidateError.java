package co.edu.unbosque.payrollsystem.dto;

import lombok.Data;

@Data
public class ValidateError {

    private String attribute;

    private String message;

    public ValidateError(String attribute, String message) {
        this.attribute = attribute;
        this.message = message;
    }

}
