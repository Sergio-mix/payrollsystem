package co.edu.unbosque.payrollsystem.component;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class ValidationComponent {

    /**
     * is null or empty boolean.
     *
     * @param value the user
     * @return the validate error
     */
    public boolean isNullOrEmpty(final String value) {
        return value == null || value.isEmpty();
    }

    /**
     * Max length boolean.
     *
     * @param value the value
     * @param max   the max
     * @return the validate error
     */
    public boolean maxLength(final String value, final int max) {
        return value.length() > max;
    }

    /**
     * Min length boolean.
     *
     * @param value the value
     * @param min   the min
     * @return the validate error
     */
    public boolean minLength(final String value, final int min) {
        return value.length() < min;
    }

    /**
     * Contains uppercase boolean.
     *
     * @param str the value
     * @return the validate error
     */
    public boolean containsUppercase(final String str, final int min) {
        int count;
        count = 0;
        for (int i = 0; i < str.length(); i++) {
            if (Character.isUpperCase(str.charAt(i))
                    && !Character.isDigit(str.charAt(i))) {
                count++;
            }
        }

        return count < min;
    }

    /**
     * Contains lowercase boolean.
     *
     * @param str the value
     * @return the validate error
     */
    public boolean containsLowercase(String str, int min) {
        int count;
        count = 0;
        for (int i = 0; i < str.length(); i++) {
            if (Character.isLowerCase(str.charAt(i))
                    && !Character.isDigit(str.charAt(i))) {
                count++;
            }
        }

        return count < min;
    }

    /**
     * Contains number boolean.
     *
     * @param str the value
     * @return the validate error
     */
    public boolean containsNumber(String str, int n) {
        int count;
        count = 0;
        for (int i = 0; i < str.length(); i++) {
            if (Character.isDigit(str.charAt(i))) {
                count++;
            }
        }

        return count >= n;
    }
}
