package co.edu.unbosque.payrollsystem.dto;

import lombok.Data;

@Data
public class ValidateError {

    private Object attribute;

    private String message;

    public ValidateError(Object attribute, String message) {
        this.attribute = attribute;
        this.message = message;
    }

}
