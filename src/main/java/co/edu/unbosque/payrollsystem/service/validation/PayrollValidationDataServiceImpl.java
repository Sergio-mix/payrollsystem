package co.edu.unbosque.payrollsystem.service.validation;

import co.edu.unbosque.payrollsystem.dto.PayrollFile;
import co.edu.unbosque.payrollsystem.dto.PayrollFileData;
import co.edu.unbosque.payrollsystem.dto.ValidateError;
import co.edu.unbosque.payrollsystem.exception.PayrollException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service(value = "payrollValidationDataService")
public class PayrollValidationDataServiceImpl extends PayrollValidationServiceImpl {

    public void validatePayroll(final PayrollFile payrollFile) throws PayrollException {
        boolean isValid = true;
        List<ValidateError> errors = new ArrayList<>();

        errors.add(validateTypeDocument(payrollFile.getTypeDocument()));
        errors.add(validateDocumentNumber(payrollFile.getDocumentNumber()));
        errors.add(validateBusinessName(payrollFile.getBusinessName()));
        errors.add(validateReference(payrollFile.getReference()));
        errors.add(validateRequest(payrollFile.getRequest()));
        errors.add(validateHeaders(payrollFile.getHeaders()));
        errors.add(validateHeadersData(payrollFile.getHeadersData()));
        errors.removeIf(Objects::isNull);
        payrollFile.setValidateErrors(errors);

        if (payrollFile.getValidateErrors().isEmpty()) {
            List<PayrollFileData> listData = payrollFile.getPayrollFileData();

            for (PayrollFileData payrollFileData : listData) {
                System.out.println(payrollFileData);
                List<ValidateError> errorsData = validateData(payrollFileData);
                System.out.println(errorsData);
                if (errorsData.isEmpty()) {
                    listData.remove(payrollFileData);
                }else {
                    payrollFileData.setValidateErrors(errorsData);
                    listData.add(payrollFileData);
                }
            }

            if(listData.isEmpty()){
                payrollFile.setPayrollFileData(null);
            }else{
                isValid = false;
                payrollFile.setPayrollFileData(listData);
            }
        }else {
            isValid = false;
        }

        if (!isValid) {
            throw new PayrollException(payrollFile);
        }
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
        errors.add(validateWorkedDays(payrollFileData.getWorkedDays()));
        errors.add(validateDaysOfDisability(payrollFileData.getDaysOfDisability()));
        errors.add(validateLeaveDays(payrollFileData.getLeaveDays()));
        errors.add(validateTotalDays(payrollFileData.getTotalDays()));
        errors.add(validateDateOfAdmission(payrollFileData.getDateOfAdmission()));

        errors.removeIf(Objects::isNull);

        return errors;
    }
}

