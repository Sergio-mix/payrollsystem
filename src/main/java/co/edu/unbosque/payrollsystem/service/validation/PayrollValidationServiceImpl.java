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
    public String[] headersList = {"TIPODEDOCUMENTO", "NUMERO", "RAZONSOCIAL", "REFERENCIA", "SOLICITUD"};
    public String[] headersDataList = {"ORDEN", "TIPODOCUMENTO", "NUMERO", "NOMBRECOTIZANTE", "CARGO", "ANO", "MES",
            "SALARIO", "DIASTRABAJADOS", "DIASINCAPACIDAD", "DIASLICENCIA", "TOTALDIAS", "FECHADEINGRESO"};
    public String[] headersDynamicList = {"SUELDOBASICO", "APOYO", "HORAEXTRADIURNA", "HORAEXTRAFA", "COMISIONES",
            "VACACIONES", "VACACIONESOBLIGATORIAS", "AJAPORINS", "BONODERETIRO", "COMPENSACION", "INCAPACIDAD"};

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
                ? new ValidateError(attribute, "The document number is not valid it must be a number")
                : documentNumber < 0
                ? new ValidateError(attribute, "The document number cannot be negative")
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
                ? new ValidateError(attribute, "The reference is invalid it must be a number")
                : reference < 0
                ? new ValidateError(attribute, "The reference cannot be negative")
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
                    sb.append("(").append(headers.get(i)).append(") ").append("does not match the format, ");
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
                result = "The number of headers data is not valid " +
                        (headersData.size() < headersDataList.length
                                ? "missing  " + (headersData.size() - headersDataList.length) + " headers"
                                : "left over " + -(headersDataList.length - headersData.size()) + " headers");
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
                ? new ValidateError(attribute, "The order is invalid it must be a number")
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
                ? new ValidateError(attribute, "The year is invalid it must be a number")
                : year < 0
                ? new ValidateError(attribute, "The year cannot be negative")
                : null;
    }

    public ValidateError validateMonth(final Integer month) {
        final var attribute = "month";
        return month == null
                ? new ValidateError(attribute, "The month is invalid it must be a number")
                : month < 0
                ? new ValidateError(attribute, "The month cannot be negative")
                : null;
    }

    public ValidateError validateSalary(final Float salary) {
        final var attribute = "salary";
        return salary == null
                ? new ValidateError(attribute, "The salary is invalid it must be a number")
                : salary < 0
                ? new ValidateError(attribute, "The salary cannot be negative")
                : null;
    }

    public ValidateError validateWorkedDays(final Integer workedDays) {
        final var attribute = "workedDays";
        return workedDays == null
                ? new ValidateError(attribute, "The worked days is invalid it must be a number")
                : workedDays < 0
                ? new ValidateError(attribute, "The worked days cannot be negative")
                : null;
    }

    public ValidateError validateDaysOfDisability(final Integer daysOfDisability) {
        final var attribute = "daysOfDisability";
        return daysOfDisability == null
                ? new ValidateError(attribute, "The days of disability is invalid it must be a number")
                : daysOfDisability < 0
                ? new ValidateError(attribute, "The days of disability cannot be negative")
                : null;
    }

    public ValidateError validateLeaveDays(final Integer leaveDays) {
        final var attribute = "leaveDays";
        return leaveDays == null
                ? new ValidateError(attribute, "The leave days is invalid it must be a number")
                : leaveDays < 0
                ? new ValidateError(attribute, "The leave days cannot be negative")
                : null;
    }

    public ValidateError validateTotalDays(final Integer totalDays, final Integer sumOfDays) {
        final var attribute = "totalDays";
        return totalDays == null
                ? new ValidateError(attribute, "The total days is invalid it must be a number")
                : !sumOfDays.equals(totalDays)
                ? new ValidateError(attribute, "The total of days does not coincide with the calculation of days")
                : null;
    }

    public ValidateError validateDateOfAdmission(final Date dateOfAdmission) {
        final var attribute = "dateOfAdmission";
        return dateOfAdmission == null
                ? new ValidateError(attribute, "The date of entry is not valid, it must be in the format dd/mm/yyyy")
                : null;
    }

    public ValidateError validateHeadersDynamic(final List<String> headersDynamic) {
        String result = null;
        String sb;
        if (headersDynamic != null) {
            sb = headersValidateList(headersDynamic, headersDynamicList);
            if (sb != null) {
                result = "The headers data is not valid please verify that it follows the format already established.\n " + sb;
            }
        } else {
            result = "The headers data is required";
        }

        return result != null ? new ValidateError("headersDataDynamic", result) : null;
    }

    public ValidateError validateMinimumWage(final Float minimumWage) {
        final var attribute = "minimumWage";
        return minimumWage == null
                ? new ValidateError(attribute, "The minimum wage is invalid it must be a number")
                : minimumWage < 0
                ? new ValidateError(attribute, "The minimum wage cannot be negative")
                : null;
    }

    public ValidateError validateSupport(final Float support) {
        final var attribute = "support";
        return support == null
                ? new ValidateError(attribute, "The support is invalid it must be a number")
                : support < 0
                ? new ValidateError(attribute, "The support cannot be negative")
                : null;
    }

    public ValidateError validateOvertimeHour(final Float overtimeHour) {
        final var attribute = "overtimeHour";
        return overtimeHour == null
                ? new ValidateError(attribute, "The overtime hour is invalid it must be a number")
                : overtimeHour < 0
                ? new ValidateError(attribute, "The overtime hour cannot be negative")
                : null;
    }

    public ValidateError validateOvertimeHourFa(final Float overtimeHourFa) {
        final var attribute = "overtimeHourFa";
        return overtimeHourFa == null
                ? new ValidateError(attribute, "the overtime hourFa is invalid it must be a number")
                : overtimeHourFa < 0
                ? new ValidateError(attribute, "the overtime hourFa cannot be negative")
                : null;
    }

    public ValidateError validateCommissions(final Float commissions) {
        final var attribute = "commissions";
        return commissions == null
                ? new ValidateError(attribute, "The commissions is invalid it must be a number")
                : commissions < 0
                ? new ValidateError(attribute, "The commissions cannot be negative")
                : null;
    }

    public ValidateError validateHolidays(final Float holidays) {
        final var attribute = "holidays";
        return holidays == null
                ? new ValidateError(attribute, "The holidays is invalid it must be a number")
                : holidays < 0
                ? new ValidateError(attribute, "The holidays cannot be negative")
                : null;
    }

    public ValidateError validateRequiredHoliday(final Float requiredHoliday) {
        final var attribute = "requiredHoliday";
        return requiredHoliday == null
                ? new ValidateError(attribute, "The required holiday is invalid it must be a number")
                : requiredHoliday < 0
                ? new ValidateError(attribute, "The required holiday cannot be negative")
                : null;
    }

    public ValidateError validateAjAporIns(final Float ajAporIns) {
        final var attribute = "ajAporIns";
        return ajAporIns == null
                ? new ValidateError(attribute, "The aj aporIns is invalid it must be a number")
                : ajAporIns < 0
                ? new ValidateError(attribute, "The aj aporIns cannot be negative")
                : null;
    }

    public ValidateError validateWithdrawalBonus(final Float withdrawalBonus) {
        final var attribute = "withdrawalBonus";
        return withdrawalBonus == null
                ? new ValidateError(attribute, "The withdrawal bonus is invalid it must be a number")
                : withdrawalBonus < 0
                ? new ValidateError(attribute, "The withdrawal bonus cannot be negative")
                : null;
    }

    public ValidateError validateCompensation(final Float compensation) {
        final var attribute = "compensation";
        return compensation == null
                ? new ValidateError(attribute, "The compensation is invalid it must be a number")
                : compensation < 0
                ? new ValidateError(attribute, "The compensation cannot be negative")
                : null;
    }

    public ValidateError validateInability(final Float inability) {
        final var attribute = "inability";
        return inability == null
                ? new ValidateError(attribute, "The inability is invalid it must be a number")
                : inability < 0
                ? new ValidateError(attribute, "The inability cannot be negative")
                : null;
    }


}
