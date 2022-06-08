package co.edu.unbosque.payrollsystem.dto;

import lombok.Data;

/**
 * userNames class
 */
@Data
public class Usernames {

    /**
     * id user
     */
    private Integer idUser;

    /**
     * userName
     */
    private String username;

    /**
     * constructor
     */
    public Usernames(final Integer idUser, final String username) {
        this.idUser = idUser;
        this.username = username;
    }
}
