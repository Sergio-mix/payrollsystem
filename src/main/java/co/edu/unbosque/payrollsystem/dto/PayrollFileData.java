package co.edu.unbosque.payrollsystem.dto;

import lombok.Data;

import java.util.List;

@Data
public class PayrollFileData {
    private Object order;
    private Object typeDocument;
    private Object documentNumber;
    private Object nameOfTheContributor;
    private Object position;
    private Object year;
    private Object month;
    private Object salary;
    private Object workedDays;
    private Object daysOfDisability;
    private Object leaveDays;
    private Object totalDays;
    private Object dateOfAdmission;

    private List<ValidateError> validateErrors;
//    private Object minimumWage;
//    private Object support;
//    private Object overtimeHour;
//    private Object overtimeHourFa;
//    private Object commission;
//    private Object holiday;
//    private Object requiredHoliday;
//    private Object ajAporIns;
//    private Object withdrawalBonus;
//    private Object compensation;
//    private Object inability;
}
