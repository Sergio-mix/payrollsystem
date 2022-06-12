package co.edu.unbosque.payrollsystem.dto;

import lombok.Data;

import java.util.List;

@Data
public class PayrollFileDynamic {
    private Float minimumWage = 0F;
    private Float support = 0F;
    private Float overtimeHour = 0F;
    private Float overtimeHourFa = 0F;
    private Float commissions = 0F;
    private Float holidays = 0F;
    private Float requiredHoliday = 0F;
    private Float ajAporIns = 0F;
    private Float withdrawalBonus = 0F;
    private Float compensation = 0F;
    private Float inability = 0F;

    List<ValidateError> validateErrors;
}
