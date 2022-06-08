package co.edu.unbosque.payrollsystem.request;

import lombok.Data;

@Data
public class RecoveryCode {
    private Integer id;

    private String password;

    private String code;

    private String email;
}
