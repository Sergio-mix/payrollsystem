package co.edu.unbosque.payrollsystem.response;

import lombok.Data;

@Data
public class LoginResponse {
    private String token;

    private Integer userId;

    public LoginResponse(String token, Integer userId) {
        this.token = token;
        this.userId = userId;
    }
}
