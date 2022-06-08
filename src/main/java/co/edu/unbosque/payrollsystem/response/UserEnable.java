package co.edu.unbosque.payrollsystem.response;

import lombok.Data;

@Data
public class UserEnable {
    private String message;

    private Boolean enabled;

    public UserEnable(String message, Boolean enabled) {
        this.message = message;
        this.enabled = enabled;
    }
}
