package co.edu.unbosque.payrollsystem.response;

import lombok.Data;

@Data
public class UserInfo {
    private String username;

    private Object authorities;

    public UserInfo(String username, Object authorities) {
        this.username = username;
        this.authorities = authorities;
    }
}
