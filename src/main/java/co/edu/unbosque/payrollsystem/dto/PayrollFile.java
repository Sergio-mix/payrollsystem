package co.edu.unbosque.payrollsystem.dto;

import lombok.Data;

import java.util.List;

@Data
public class PayrollFile {
    private String typeDocument;
    private Integer documentNumber;
    private String businessName;
    private Integer reference;
    private String request;

    private List<String> headers;
    private List<String> headersData;
    private List<String> headersDataDynamic;

    private List<PayrollFileData> PayrollFileData;

    private List<ValidateError> validateErrors;
}
