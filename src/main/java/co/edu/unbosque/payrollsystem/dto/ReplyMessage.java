package co.edu.unbosque.payrollsystem.dto;

import lombok.Data;

@Data
public class ReplyMessage {
    public static final String VALID_CODE = "Valid code";
    public static final String INVALID_CODE = "Invalid code";
    public static final String CODE_EXPIRED = "Code expired";
    public static final String USER_NOT_FOUND = "User not found";
    public static final String EMAIL_INVALID = "Email invalid";
    public static final String INVALID_DATA = "Invalid data";
    public static final String PASSWORD_UPDATED = "Successfully password update";
    public static final String PASSWORD_EQUALS = "Can't put the same password";
    public static final String PASSWORD_NOT_UPDATED = "Password not updated";
    public static final String USER_NOT_ENABLED = "Your user is disabled, please contact the administrator";
    public final static String ERROR_505 = "Something went wrong";
    public static final String RECOVER_PASSWORD = "Check your email if I don't arrive it's because you don't have the required permissions";
    public static final String BEGINNING_BEFORE_FINAL = "The beginning date must be before the final date";
    public static final String UPDATE_USER = "Successfully updated user";
    public static final String FAILED_UPDATE_USER = "Failed to update user";
    public static final String NOT_ENABLE = "You can't enable this user";
    public static final String NOT_LOGIN = "Could not log in";
}