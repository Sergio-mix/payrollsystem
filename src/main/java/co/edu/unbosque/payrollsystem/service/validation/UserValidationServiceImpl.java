package co.edu.unbosque.payrollsystem.service.validation;

import co.edu.unbosque.payrollsystem.component.ValidationComponent;
import co.edu.unbosque.payrollsystem.dto.ValidateError;
import co.edu.unbosque.payrollsystem.model.CountryCode;
import co.edu.unbosque.payrollsystem.model.TypeDocument;
import co.edu.unbosque.payrollsystem.repository.UserRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * The type User validation service impl.
 */
@Data
@Service(value = "UserValidationServiceImpl")
public class UserValidationServiceImpl {

    /**
     * The User repository.
     */
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ValidationComponent validation;

    /**
     * Validate name validate error.
     *
     * @param names the names
     * @return the validate error
     */
    public ValidateError validateName(final String names) {
        final var attribute = "names";
        return validation.isNullOrEmpty(names) ? new ValidateError(attribute, "The names is required")
                : validation.maxLength(names, 25)
                ? new ValidateError(attribute, "The names must contain at least " + 25 + " characters")
                : null;
    }

    /**
     * Validate last name validate error.
     *
     * @param lastNames the last names
     * @return the validate error
     */
    public ValidateError validateLastName(final String lastNames) {
        final var attribute = "lastNames";
        return validation.isNullOrEmpty(lastNames) ? new ValidateError(attribute, "The names is required")
                : validation.maxLength(lastNames, 25)
                ? new ValidateError(attribute, "The lastNames must contain at least " + 25 + " characters") : null;
    }

    /**
     * Validate cell phone validate error.
     *
     * @param countryCode the country code
     * @param cellPhone   the cell phone
     * @return the validate error
     */
    public ValidateError validateCellPhone(final CountryCode countryCode, final String cellPhone) {
        final var attribute = "cellPhone";
        return validation.isNullOrEmpty(cellPhone) || countryCode == null
                || countryCode.getId() == null
                ? new ValidateError(attribute, "The cell phone is required")
                : !cellPhone.matches("\\d+")
                ? new ValidateError(attribute, "The cell phone must contain only numbers")
                : userRepository.existsByCellPhone(countryCode.getId(), cellPhone)//exist user with same cell phone
                ? new ValidateError(attribute, "The cell phone already exists")
                : null;
    }

    /**
     * Validate email validate error.
     *
     * @param email the email
     * @return the validate error
     */
    public ValidateError validateEmail(final String email) {
        final var attribute = "email";
        return validation.isNullOrEmpty(email)
                ? new ValidateError(attribute, "The email is required")
                : !email.matches("^[\\w-+]+(\\.\\w+)*@[\\w-]+(\\.\\w+)*(\\.[a-z]{2,})$")
                ? new ValidateError(attribute, "The email is invalid")
                : validation.maxLength(email, 50)
                ? new ValidateError(attribute, "The email must contain at least " + 50 + " characters")
                : validation.minLength(email, 5)
                ? new ValidateError(attribute, "The email must contain at least " + 5 + " characters")
                : userRepository.existsByEmail(email)
                ? new ValidateError(attribute, "The email already exists") : null;
    }

    /**
     * Validate password validate error.
     *
     * @param password the password
     * @return the validate error
     */
    public ValidateError validatePassword(final String password) {
        final var attribute = "password";
        final int uppercase = 1;
        final int lowercase = 1;
        final int number = 1;
        return validation.isNullOrEmpty(password)
                ? new ValidateError(attribute, "The password is required")
                : validation.maxLength(password, 8)
                ? new ValidateError(attribute, "The password must contain at least " + 8 + " characters")
                : validation.containsUppercase(password, uppercase)
                ? new ValidateError(attribute, "The password must contain at least " + uppercase + " uppercase character")
                : validation.containsLowercase(password, lowercase)
                ? new ValidateError(attribute, "The password must contain at least " + lowercase + " lowercase character")
                : !validation.containsNumber(password, number)
                ? new ValidateError(attribute, "The password must have at least " + number + " number")
                : null;
    }

    /**
     * Validate user name validate error.
     *
     * @param username the username
     * @return the validate error
     */
    public ValidateError validateUsername(final String username) {
        final var attribute = "username";
        final int uppercase = 1;
        final int lowercase = 1;
        return validation.isNullOrEmpty(username)
                ? new ValidateError(attribute, "The user name is required")
                : validation.maxLength(username, 8)
                ? new ValidateError(attribute, "The user name must contain at least " + 8 + " characters")
                : validation.containsUppercase(username, uppercase)
                ? new ValidateError(attribute, "The user name must contain at least " + uppercase + " uppercase character")
                : validation.containsLowercase(username, lowercase)
                ? new ValidateError(attribute, "The user name must contain at least " + lowercase + " lowercase character")
                : userRepository.existsByUsername(username)
                ? new ValidateError(attribute, "The user name already exists")
                : null;
    }

    /**
     * Validate document validate error.
     *
     * @param documentType   the type document
     * @param documentNumber the document number
     * @return the validate error
     */
    public ValidateError validateDocument(final TypeDocument documentType, final String documentNumber) {
        final var attribute = "document";
        return documentType == null || documentType.getId() == null
                ? new ValidateError(attribute, "The document is required")
                : validation.isNullOrEmpty(documentNumber)
                ? new ValidateError(attribute, "Missing data to complete the document")
                : userRepository.existsDocument(documentType.getId(), documentNumber)
                ? new ValidateError(attribute, "The document already exists")
                : null;
    }

    /**
     * Validate days to expire validate error.
     *
     * @param daysToExpire the days to expire
     * @return the validate error
     */
    public ValidateError validateDaysToExpire(final Integer daysToExpire) {
        return daysToExpire < 1
                ? new ValidateError("daysToExpire", "The number of expiration days must be greater than 0")
                : null;
    }
}
