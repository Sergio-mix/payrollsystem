package co.edu.unbosque.payrollsystem.service.validation;

import co.edu.unbosque.payrollsystem.dto.ValidateError;
import co.edu.unbosque.payrollsystem.exception.UserException;
import co.edu.unbosque.payrollsystem.model.User;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The type User validation register service impl.
 */
@Service(value = "UserValidationRegisterServiceImpl")
@Data
public class UserRegisterValidationServiceImpl extends UserValidationServiceImpl {

    /**
     * Validate user register.
     *
     * @param user the user
     */
    public void validateRegisterUser(final User user) throws UserException {
        List<ValidateError> errors;
        errors = new ArrayList<>();

        errors.add(validateName(user.getNames()));
        errors.add(validateLastName(user.getLastNames()));
        errors.add(validateCellPhone(user.getCountryCode(), user.getCellPhone()));
        errors.add(validateEmail(user.getEmail()));
        errors.add(validatePassword(user.getPassword()));
        errors.add(validateUsername(user.getUsername()));
        errors.add(validateDocument(user.getTypeDocument(), user.getDocumentNumber()));
        errors.add(validateDaysToExpire(user.getDaysToExpire()));

        errors.removeIf(Objects::isNull);

        if (!errors.isEmpty()) {
            throw new UserException(errors);
        }
    }
}
