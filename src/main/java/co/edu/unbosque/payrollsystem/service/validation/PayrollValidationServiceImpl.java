package co.edu.unbosque.payrollsystem.service.validation;

import co.edu.unbosque.payrollsystem.component.ValidationComponent;
import co.edu.unbosque.payrollsystem.dto.ValidateError;
import co.edu.unbosque.payrollsystem.repository.*;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Data
@Service(value = "PayrollValidationServiceImpl")
public class PayrollValidationServiceImpl {

    @Autowired
    private PayrollRepository payroll;
    @Autowired
    private ValidationComponent validation;
    @Autowired
    private TypeDocumentRepository typeDocument;
    String[] headersList = {"TIPODEDOCUMENTO", "NUMERO", "RAZONSOCIAL", "REFERENCIA", "SOLICITUD"};
    String[] headersDataList = {"ORDEN", "TIPODOCUMENTO", "NUMERO", "NOMBRECOTIZANTE", "CARGO", "ANO", "MES",
            "SALARIO", "DIASTRABAJADOS", "DIASINCAPACIDAD", "DIASLICENCIA", "TOTALDIAS", "FECHADEINGRESO"};

    public ValidateError validateTypeDocument(final String documentType) {
        final var attribute = "typeDocument";
        return documentType == null
                ? new ValidateError(attribute, "The type document not valid")
                : documentType.isEmpty()
                ? new ValidateError(attribute, "The type document is empty")
                : typeDocument.findByName(documentType.toUpperCase()).isEmpty()
                ? new ValidateError(attribute, "The document type is not valid")
                : null;
    }

    public ValidateError validateDocumentNumber(final Integer documentNumber) {
        final var attribute = "documentNumber";
        return documentNumber == null
                ? new ValidateError(attribute, "Invalid value not a number")
                : null;
    }

    public ValidateError validateDocumentCompany(final String documentType, final Integer documentNumber) {
        final var attribute = "documentNumber";
        return payroll.existsPayroll(documentType.toUpperCase(), String.valueOf(documentNumber))
                ? new ValidateError(attribute, "The document is already registered")
                : null;
    }

    public ValidateError validateBusinessName(final String businessName) {
        final var attribute = "businessName";
        return businessName == null
                ? new ValidateError(attribute, "The business name invalid value")
                : businessName.isEmpty()
                ? new ValidateError(attribute, "The business name is empty")
                : null;
    }

    public ValidateError validateReference(final Integer reference) {
        final var attribute = "reference";
        return reference == null
                ? new ValidateError(attribute, "The reference not a number")
                : payroll.existsReference(String.valueOf(reference))
                ? new ValidateError(attribute, "The reference is already registered")
                : null;
    }

    public ValidateError validateRequest(final String request) {
        final var attribute = "request";
        return request == null
                ? new ValidateError(attribute, "The request invalid value")
                : request.isEmpty()
                ? new ValidateError(attribute, "The request is empty")
                : null;
    }

    private String headersValidateList(List<String> headers, String[] headersList) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < headers.size(); i++) {
            if (!validation.isNullOrEmpty(headers.get(i))) {
                if (!headersList[i].equals(validation.removeAccents(headers.get(i).replaceAll("\\s+", "")).toUpperCase())) {
                    sb.append("(").append(headers.get(i)).append(")").append("does not match the format, ");
                }
            } else {
                sb.append(headers.get(i) == null ? "not valid" : headersList[i] + " it is required").append(", ");
            }
        }
        return sb.toString().isEmpty() ? null : sb.toString();
    }

    public ValidateError validateHeaders(final List<String> headers) {
        String result = null;
        String sb = null;
        if (headers != null) {
            if (headers.size() == headersList.length) {
                sb = headersValidateList(headers, headersList);
            } else {
                result = "The number of headers is not valid";
            }

            if (sb != null) {
                result = "The headers data is not valid: " + sb + " verify that it follows the format already established.";
            }
        } else {
            result = "The headers is required";
        }

        return result != null ? new ValidateError("headers", result) : null;
    }


    public ValidateError validateHeadersData(final List<String> headersData) {
        String result = null;
        String sb = null;
        if (headersData != null) {
            if (headersData.size() == headersDataList.length) {
                sb = headersValidateList(headersData, headersDataList);
            } else {
                result = "The number of headers data is not valid";
            }

            if (sb != null) {
                result = "The headers data is not valid please verify that it follows the format already established.\n " + sb;
            }
        } else {
            result = "The headers data is required";
        }

        return result != null ? new ValidateError("headersData", result) : null;
    }

    public ValidateError validateOrder(final Integer order) {
        final var attribute = "order";
        return order == null
                ? new ValidateError(attribute, "The order not a number")
                : null;
    }

    public ValidateError validateNameOfTheContributor(final String nameOfTheContributor) {
        final var attribute = "nameOfTheContributor";
        return validation.isNullOrEmpty(nameOfTheContributor)
                ? new ValidateError(attribute, "The name of the contributor invalid value")
                : null;
    }

    public ValidateError validatePosition(final String position) {
        final var attribute = "position";
        return validation.isNullOrEmpty(position)
                ? new ValidateError(attribute, "The position invalid value")
                : null;
    }

    public ValidateError validateYear(final Integer year) {
        final var attribute = "year";
        return year == null
                ? new ValidateError(attribute, "The year is not a number")
                : null;
    }

    public ValidateError validateMonth(final Integer month) {
        final var attribute = "month";
        return month == null
                ? new ValidateError(attribute, "The year is not a number")
                : null;
    }

    public ValidateError validateSalary(final Integer salary) {
        final var attribute = "salary";
        return salary == null
                ? new ValidateError(attribute, "The year is not a number")
                : null;
    }

    public ValidateError validateWorkedDays(final Integer workedDays) {
        final var attribute = "workedDays";
        return workedDays == null
                ? new ValidateError(attribute, "The year is not a number")
                : null;
    }

    public ValidateError validateDaysOfDisability(final Integer daysOfDisability) {
        final var attribute = "daysOfDisability";
        return daysOfDisability == null
                ? new ValidateError(attribute, "The year is not a number")
                : null;
    }

    public ValidateError validateLeaveDays(final Integer leaveDays) {
        final var attribute = "leaveDays";
        return leaveDays == null
                ? new ValidateError(attribute, "The year is not a number")
                : null;
    }

    public ValidateError validateTotalDays(final Integer totalDays) {
        final var attribute = "totalDays";
        return totalDays == null
                ? new ValidateError(attribute, "The year is not a number")
                : null;
    }

    public ValidateError validateDateOfAdmission(final Date dateOfAdmission) {
        final var attribute = "dateOfAdmission";
        return dateOfAdmission == null
                ? new ValidateError(attribute, "The year is not Valid")
                : null;
    }

}
