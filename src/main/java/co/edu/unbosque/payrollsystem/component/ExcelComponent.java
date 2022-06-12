package co.edu.unbosque.payrollsystem.component;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component(value = "ExcelComponent")
public class ExcelComponent {

    @Autowired
    private ValidationComponent validation;


    public String formatParseString(XSSFCell object) {
        DataFormatter dataFormatter = new DataFormatter();
        String value = dataFormatter.formatCellValue(object).trim();
        return value.isEmpty() ? "" : validation.isString(value) ? value : null;
    }

    public Integer formatParseInteger(XSSFCell object) {
        DataFormatter dataFormatter = new DataFormatter();
        String value = dataFormatter.formatCellValue(object).trim();
        return value.isEmpty() ? null : validation.isNumber(value) ? Integer.parseInt(value) : null;
    }

    public Float formatParseFloat(XSSFCell object) {
        DataFormatter dataFormatter = new DataFormatter();
        String value = dataFormatter.formatCellValue(object).trim();
        return value.isEmpty() ? null : validation.isNumber(value) ? Float.parseFloat(value) : null;
    }

    public Date formatParseDate(XSSFCell object) {
        DataFormatter dataFormatter = new DataFormatter();
        String value = dataFormatter.formatCellValue(object).trim();
        return value.isEmpty() ? null : validation.isDate(value) ? new Date(value) : null;
    }
}
