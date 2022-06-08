package co.edu.unbosque.payrollsystem.dto;

import lombok.Data;

@Data
public class RecoveryCodeStatus {
    private String message;

    private String username = null;

    public RecoveryCodeStatus(String message, String username) {
        this.message = message;
        this.username = username;
    }

    public RecoveryCodeStatus(String message) {
        this.message = message;
    }
}
