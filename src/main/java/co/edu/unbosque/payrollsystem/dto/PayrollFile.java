package co.edu.unbosque.payrollsystem.dto;

import lombok.Data;

import java.util.List;

@Data
public class PayrollFile {
    private Object typeDocument;
    private Object documentNumber;
    private Object businessName;
    private Object reference;
    private Object request;

    private List<Object> headers;
    private List<Object> headersData;
    private List<Object> headersDataDynamic;

    private List<PayrollFileData> PayrollFileData;

    private List<ValidateError> validateErrors;
}
