package co.edu.unbosque.payrollsystem.dto;

import lombok.Data;

import java.util.Date;

@Data


public class PayrollFileData {

    private String typeDocumentCompany;
    private Integer numDocumentCompany;
    private String businessName;
    private Integer reference;
    private String request;
    private Integer order;
    private String typeDocumentEmployee;
    private Integer numDocumentEmployee;
    private String contributorName;
    private String position;
    private Date dateEmployee;
    private Integer salary;
    private Integer workedDays;
    private Integer inabilityDays;
    private Integer Totaldays;
    private Date AdmissionDay;
    private Integer minimumWage;
    private Integer support;
    private Integer overtimeHour;
    private Integer overtimeHourFa;
    private Integer commission;
    private Integer holiday;
    private Integer requiredHoliday;
    private Integer ajAporIns;
    private Integer withdrawalBonus;
    private Integer compensation;
    private Integer inability;


}
