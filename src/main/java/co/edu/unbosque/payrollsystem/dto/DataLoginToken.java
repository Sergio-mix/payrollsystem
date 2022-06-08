package co.edu.unbosque.payrollsystem.dto;

import lombok.Data;

@Data
public class DataLoginToken {

    private Integer userId;

    private String username;

    private String token;

    public DataLoginToken(Integer userId, String username, String token) {
        this.userId = userId;
        this.username = username;
        this.token = token;
    }
}
