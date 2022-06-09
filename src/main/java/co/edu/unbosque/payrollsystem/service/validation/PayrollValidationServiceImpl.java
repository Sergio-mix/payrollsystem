package co.edu.unbosque.payrollsystem.service.validation;

import co.edu.unbosque.payrollsystem.component.ValidationComponent;
import co.edu.unbosque.payrollsystem.dto.ValidateError;
import co.edu.unbosque.payrollsystem.repository.*;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
            "DIAS", "SALARIO", "DIASTRABAJADOS", "DIASINCAPACIDAD","DIASLICENCIA", "TOTALDIAS", "FECHADEINGRESO"};

    public ValidateError validateTypeDocument(final Object documentType) {
        final var attribute = "typeDocument";
        return documentType == null
                ? new ValidateError(attribute, "The type document is required")
                : !validation.isString(documentType)
                ? new ValidateError(attribute, "Invalid value")
                : validation.isNullOrEmpty((String) documentType)
                ? new ValidateError(attribute, "The type document is required")
                : typeDocument.findByName(documentType.toString().trim().toUpperCase()).isEmpty()
                ? new ValidateError(attribute, "The document type is not valid")
                : null;
    }

    public ValidateError validateDocumentNumber(final Object documentNumber) {
        final var attribute = "documentNumber";
        return documentNumber == null
                ? new ValidateError(attribute, "The number document is required")
                : !validation.isNumber(documentNumber)
                ? new ValidateError(attribute, "Invalid value not a number")
                : null;
    }

    public ValidateError validateDocumentCompany(final String documentType, final Integer documentNumber) {
        final var attribute = "documentCompany";
        return payroll.existsPayroll(documentType.toUpperCase(), String.valueOf(documentNumber))
                ? new ValidateError(attribute, "The document is already registered")
                : null;
    }

    public ValidateError validateBusinessName(final Object businessName) {
        final var attribute = "businessName";
        return businessName == null
                ? new ValidateError(attribute, "The business name is required")
                : !validation.isString(businessName)
                ? new ValidateError(attribute, "Invalid value")
                : null;
    }

    public ValidateError validateReference(final Object reference) {
        final var attribute = "reference";
        return reference == null
                ? new ValidateError(attribute, "The reference is required")
                : !validation.isNumber(reference)
                ? new ValidateError(attribute, "Invalid value not a number")
                : payroll.existsReference(String.valueOf(Integer.parseInt(String.valueOf(reference))))
                ? new ValidateError(attribute, "The reference is already registered")
                : null;
    }

    public ValidateError validateRequest(final Object request) {
        final var attribute = "request";
        return request == null
                ? new ValidateError(attribute, "The request is required")
                : !validation.isString(request)
                ? new ValidateError(attribute, "Invalid value")
                : null;
    }

    private String headersValidateList(List<Object> headers, String[] headersList) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < headers.size(); i++) {
            if (validation.isString(headers.get(i)) && !validation.isNullOrEmpty((String) headers.get(i))) {
                if (!headersList[i].equals(validation.removeAccents(headers.get(i).toString().replaceAll("\\s+", "")).toUpperCase())) {
                    sb.append(headers.get(i).toString()).append(", ");
                }
            } else {
                sb.append(headers.get(i).toString()).append(", ");
            }
        }
        return sb.toString().isEmpty() ? null : sb.toString();
    }

    public ValidateError validateHeaders(final List<Object> headers) {
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


    public ValidateError validateHeadersData(final List<Object> headersData) {
        String result = null;
        String sb = null;
        if (headersData != null) {
            if (headersData.size() == headersDataList.length) {
                sb = headersValidateList(headersData, headersDataList);
            } else {
                result = "The number of headers data is not valid";
            }

            if (sb != null) {
                result = "The headers data is not valid: " + sb + " verify that it follows the format already established.";
            }
        } else {
            result = "The headers data is required";
        }

        return result != null ? new ValidateError("headersData", result) : null;
    }

    public ValidateError validateOrder(final Object order) {
        final var attribute = "order";
        return order == null
                ? new ValidateError(attribute, "The order is required")
                : !validation.isNumber(order)
                ? new ValidateError(attribute, "Invalid value")
                : validation.isNullOrEmpty((String) order)
                ? new ValidateError(attribute, "The order is required")
                : null;
    }

    public ValidateError validateNameOfTheContributor(final Object nameOfTheContributor) {
        final var attribute = "nameOfTheContributor";
        return nameOfTheContributor == null
                ? new ValidateError(attribute, "The name of the contributor is required")
                : !validation.isString(nameOfTheContributor)
                ? new ValidateError(attribute, "Invalid value")
                : null;
    }

    public ValidateError validatePosition(final Object position) {
        final var attribute = "position";
        return position == null
                ? new ValidateError(attribute, "The cargo is required")
                : !validation.isString(position)
                ? new ValidateError(attribute, "Invalid value")
                : null;
    }

    public ValidateError validateYear(final Object year) {
        final var attribute = "year";
        return year == null
                ? new ValidateError(attribute, "The year is required")
                : !validation.isNumber(year)
                ? new ValidateError(attribute, "Invalid value")
                : null;
    }

    public ValidateError validateMonth(final Object month) {
        final var attribute = "month";
        return month == null
                ? new ValidateError(attribute, "The year is required")
                : !validation.isNumber(month)
                ? new ValidateError(attribute, "Invalid value")
                : null;
    }

    public ValidateError validateSalary(final Object salary) {
        final var attribute = "salary";
        return salary == null
                ? new ValidateError(attribute, "The year is required")
                : !validation.isNumber(salary)
                ? new ValidateError(attribute, "Invalid value")
                : null;
    }

    public ValidateError validateWorkedDays(final Object workedDays) {
        final var attribute = "workedDays";
        return workedDays == null
                ? new ValidateError(attribute, "The year is required")
                : !validation.isNumber(workedDays)
                ? new ValidateError(attribute, "Invalid value")
                : null;
    }

    public ValidateError validateDaysOfDisability(final Object daysOfDisability) {
        final var attribute = "daysOfDisability";
        return daysOfDisability == null
                ? new ValidateError(attribute, "The year is required")
                : !validation.isNumber(daysOfDisability)
                ? new ValidateError(attribute, "Invalid value")
                : null;
    }

    public ValidateError validateLeaveDays(final Object leaveDays) {
        final var attribute = "leaveDays";
        return leaveDays == null
                ? new ValidateError(attribute, "The year is required")
                : !validation.isNumber(leaveDays)
                ? new ValidateError(attribute, "Invalid value")
                : null;
    }

    public ValidateError validateTotalDays(final Object totalDays) {
        final var attribute = "totalDays";
        return totalDays == null
                ? new ValidateError(attribute, "The year is required")
                : !validation.isNumber(totalDays)
                ? new ValidateError(attribute, "Invalid value")
                : null;
    }

    public ValidateError validateDateOfAdmission(final Object dateOfAdmission) {
        final var attribute = "dateOfAdmission";
        return dateOfAdmission == null
                ? new ValidateError(attribute, "The year is required")
                : !validation.isDate(dateOfAdmission)
                ? new ValidateError(attribute, "Invalid value")
                : null;
    }

}
