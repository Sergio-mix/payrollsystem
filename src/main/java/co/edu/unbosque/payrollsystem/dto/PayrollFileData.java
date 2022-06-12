package co.edu.unbosque.payrollsystem.dto;

import lombok.Data;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;
import java.util.List;

@Data
public class PayrollFileData {
    private Integer order;
    private String typeDocument;
    private Integer documentNumber;
    private String nameOfTheContributor;
    private String position;
    private Integer year;
    private Integer month;
    private Float salary;
    private Integer workedDays;
    private Integer daysOfDisability;
    private Integer leaveDays;
    private Integer totalDays;
    @Temporal(TemporalType.DATE)
    private Date dateOfAdmission;
    private PayrollFileDynamic dynamicData;

    private List<ValidateError> validateErrors;
}
