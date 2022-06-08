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
 * The type User validation update service impl.
 */
@Data
@Service(value = "UserValidationUpdateServiceImpl")
public class UserUpdateValidationServiceImpl extends UserValidationServiceImpl {

    /**
     * Validate user update.
     *
     * @param user        the user
     * @param currentUser the currentUser
     */
    public void validateUpdateUser(final User user, final User currentUser) throws UserException {
        List<ValidateError> errors;
        errors = new ArrayList<>();

        if (!user.getNames().equals(currentUser.getNames())) {
            errors.add(validateNames(user.getNames()));
        }

        if (!user.getLastNames().equals(currentUser.getLastNames())) {
            errors.add(validateLastName(user.getLastNames()));
        }

        if (!user.getCellPhone().equals(currentUser.getCellPhone())) {
            errors.add(validateCellPhone(user.getCountryCode(), user.getCellPhone()));
        }

        if (!user.getEmail().equals(currentUser.getEmail())) {
            errors.add(validateEmail(user.getEmail()));
        }

        if (!user.getPassword().isEmpty()) {
            errors.add(validatePassword(user.getPassword()));
        }

        if (!user.getUsername().equals(currentUser.getUsername())) {
            errors.add(validateUsername(user.getUsername()));
        }

        if (!user.getTypeDocument().equals(currentUser.getTypeDocument())) {
            errors.add(validateDocument(user.getTypeDocument(), user.getDocumentNumber()));
        }

        if (!user.getDaysToExpire().equals(currentUser.getDaysToExpire())) {
            errors.add(validateDaysToExpire(user.getDaysToExpire()));
        }

        errors.removeIf(Objects::isNull);

        if (!errors.isEmpty()) {
            throw new UserException(errors);
        }
    }

}
