package co.edu.unbosque.payrollsystem.component;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.zip.DataFormatException;

@Component(value = "ExcelComponent")
public class ExcelComponent {

    @Autowired
    private ValidationComponent validation;


    public String formatParseString(XSSFCell object) {
        try{
        DataFormatter dataFormatter = new DataFormatter();
        String value = dataFormatter.formatCellValue(object).trim();
        return value.isEmpty() ? "" : validation.isString(value) ? value : null;
        } catch (Exception e) {
            return null;
        }
    }

    public Integer formatParseInteger(XSSFCell object) {
        try {
            DataFormatter dataFormatter = new DataFormatter();
            String value = dataFormatter.formatCellValue(object).trim();
            return value.isEmpty() ? null : validation.isNumber(value) ? Integer.parseInt(value) : null;
        } catch (Exception e) {
            return null;
        }
    }

    public Float formatParseFloat(XSSFCell object) {
        try {
            DataFormatter dataFormatter = new DataFormatter();
            String value = dataFormatter.formatCellValue(object).trim();
            return value.isEmpty() ? null : validation.isNumber(value) ? Float.parseFloat(value) : null;
        } catch (Exception e) {
            return null;
        }
    }

    public Float formatParseFloatV2(XSSFCell object) {
        try {
            DataFormatter dataFormatter = new DataFormatter();
            String value = dataFormatter.formatCellValue(object).trim();
            return value.isEmpty() ? 0 : validation.isNumber(value) ? Float.parseFloat(value) : null;
        } catch (Exception e) {
            return null;
        }
    }

    public Date formatParseDate(XSSFCell object) {
        try {
            DataFormatter dataFormatter = new DataFormatter();
            String value = dataFormatter.formatCellValue(object).trim();
            return value.isEmpty() ? null : validation.isDate(value) ? new Date(value) : null;
        } catch (Exception e) {
            return null;
        }
    }
}
