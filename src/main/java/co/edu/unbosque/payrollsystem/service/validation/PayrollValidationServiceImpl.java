package co.edu.unbosque.payrollsystem.service.validation;

import co.edu.unbosque.payrollsystem.component.ValidationComponent;
import co.edu.unbosque.payrollsystem.dto.ValidateError;
import co.edu.unbosque.payrollsystem.repository.*;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Data
@Service("payrollValidationService")
public class PayrollValidationServiceImpl {

    @Autowired
    private PayrollDynamicRepository payrollDynamic;

    @Autowired
    private PayrollRepository payroll;

    @Autowired
    private ContributorRepository contributor;
    @Autowired
    private ValidationComponent validation;
    @Autowired
    private TypeDocumentRepository typeDocument;

    public ValidateError validateDocument(final String documentType, final String documentNumber) {
        final var attribute = "document";
        return documentType == null || documentNumber == null
                ? new ValidateError(attribute, "The document is required")
                : validation.isNullOrEmpty(documentNumber) && validation.isNullOrEmpty(documentType)
                ? new ValidateError(attribute, "Missing data to complete the document")
                : typeDocument.findByName(documentType).isEmpty()
                ? new ValidateError(attribute, "The document type is not valid")
                : null;
    }

    public ValidateError validateName(final String name) {
        final var attribute = "name";
        return validation.isNullOrEmpty(name) ? new ValidateError(attribute, "The names is required")
                : validation.maxLength(name, 100)
                ? new ValidateError(attribute, "The lastNames must contain at least " + 100 + " characters") : null;
    }

    public ValidateError validatePosition(final String position) {
        final var attribute = "position";
        return validation.isNullOrEmpty(position) ? new ValidateError(attribute, "The position is required")
                : null;
    }
}
