package co.edu.unbosque.payrollsystem.component;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.springframework.stereotype.Component;

@Component(value = "ExcelComponent")
public class ExcelComponent {

    public Object formatParse(XSSFCell object) {
        DataFormatter dataFormatter = new DataFormatter();
        return dataFormatter.formatCellValue(object);
    }
}
