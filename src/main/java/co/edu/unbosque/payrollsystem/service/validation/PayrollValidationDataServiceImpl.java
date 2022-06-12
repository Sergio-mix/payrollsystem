package co.edu.unbosque.payrollsystem.service.validation;

import co.edu.unbosque.payrollsystem.dto.PayrollFile;
import co.edu.unbosque.payrollsystem.dto.PayrollFileData;
import co.edu.unbosque.payrollsystem.dto.PayrollFileDynamic;
import co.edu.unbosque.payrollsystem.dto.ValidateError;
import co.edu.unbosque.payrollsystem.exception.PayrollException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service(value = "payrollValidationDataService")
public class PayrollValidationDataServiceImpl extends PayrollValidationServiceImpl {

    public void validatePayroll(final PayrollFile payrollFile) throws PayrollException {
        PayrollFile payroll = payrollFile;
        List<ValidateError> errors = new ArrayList<>();

        ValidateError validateTypeDocument = validateTypeDocument(payroll.getTypeDocument());
        ValidateError validateDocumentNumber = validateDocumentNumber(payroll.getDocumentNumber());

        if (validateTypeDocument == null && validateDocumentNumber == null) {
            errors.add(validateDocumentCompany(payroll.getTypeDocument(), payroll.getDocumentNumber()));
        }

        errors.add(validateTypeDocument);
        errors.add(validateDocumentNumber);
        errors.add(validateBusinessName(payroll.getBusinessName()));
        errors.add(validateReference(payroll.getReference()));
        errors.add(validateRequest(payroll.getRequest()));
        errors.add(validateHeaders(payroll.getHeaders()));//TODO: headers info
        errors.add(validateHeadersData(payroll.getHeadersData()));//TODO: headers data
        errors.add(validateHeadersDynamic(payroll.getHeadersDataDynamic()));//TODO: headers dynamic
        errors.removeIf(Objects::isNull);
        payroll.setValidateErrors(errors);

        List<PayrollFileData> list = validateDataAll(payroll.getPayrollFileData());
        payroll.setPayrollFileData(list.isEmpty() ? null : list);

        if (!payroll.getValidateErrors().isEmpty() || payroll.getPayrollFileData() != null) {
            throw new PayrollException(payroll);
        }
    }

    private List<PayrollFileData> validateDataAll(final List<PayrollFileData> listData) {
        List<PayrollFileData> listError = new ArrayList<>();
        for (PayrollFileData data : listData) {
            List<ValidateError> errors = validateData(data);
            errors.removeIf(Objects::isNull);
            if (!errors.isEmpty()) {
                data.setValidateErrors(errors);
                listError.add(data);
            }
        }
        return listError;
    }

    private List<ValidateError> validateData(final PayrollFileData payrollFileData) {
        List<ValidateError> errors = new ArrayList<>();

        errors.add(validateOrder(payrollFileData.getOrder()));
        errors.add(validateTypeDocument(payrollFileData.getTypeDocument()));
        errors.add(validateDocumentNumber(payrollFileData.getDocumentNumber()));
        errors.add(validateNameOfTheContributor(payrollFileData.getNameOfTheContributor()));
        errors.add(validatePosition(payrollFileData.getPosition()));
        errors.add(validateYear(payrollFileData.getYear()));
        errors.add(validateMonth(payrollFileData.getMonth()));
        errors.add(validateSalary(payrollFileData.getSalary()));

        ValidateError validateWorkedDays = validateWorkedDays(payrollFileData.getWorkedDays());
        ValidateError validateDaysOfDisability = validateDaysOfDisability(payrollFileData.getDaysOfDisability());
        ValidateError validateLeaveDays = validateLeaveDays(payrollFileData.getLeaveDays());

        errors.add(validateWorkedDays == null && validateDaysOfDisability == null && validateLeaveDays == null
                ? validateTotalDays(payrollFileData.getTotalDays(), payrollFileData.getWorkedDays()
                - (payrollFileData.getLeaveDays() + payrollFileData.getDaysOfDisability()))
                : validateTotalDays(payrollFileData.getTotalDays(), 0));

        errors.add(validateWorkedDays);
        errors.add(validateDaysOfDisability);
        errors.add(validateLeaveDays);
        errors.add(validateDateOfAdmission(payrollFileData.getDateOfAdmission()));
        errors.addAll(validatePayrollDynamic(payrollFileData.getDynamicData()));//TODO: validatePayrollDynamic
        errors.removeIf(Objects::isNull);

        return errors;
    }

    private List<ValidateError> validatePayrollDynamic(final PayrollFileDynamic payrollFileDynamic) {
        List<ValidateError> errors = new ArrayList<>();

        errors.add(validateMinimumWage(payrollFileDynamic.getMinimumWage()));
        errors.add(validateSupport(payrollFileDynamic.getSupport()));
        errors.add(validateOvertimeHour(payrollFileDynamic.getOvertimeHour()));
        errors.add(validateOvertimeHourFa(payrollFileDynamic.getOvertimeHourFa()));
        errors.add(validateCommissions(payrollFileDynamic.getCommissions()));
        errors.add(validateHolidays(payrollFileDynamic.getHolidays()));
        errors.add(validateRequiredHoliday(payrollFileDynamic.getRequiredHoliday()));
        errors.add(validateAjAporIns(payrollFileDynamic.getAjAporIns()));
        errors.add(validateWithdrawalBonus(payrollFileDynamic.getWithdrawalBonus()));
        errors.add(validateCompensation(payrollFileDynamic.getCompensation()));
        errors.add(validateInability(payrollFileDynamic.getInability()));

        errors.removeIf(Objects::isNull);
        return errors;
    }
}

