package co.edu.unbosque.payrollsystem.service;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service(value = "payrollService")
public class PayrollServiceImpl {

    public String addPayroll(MultipartFile file) {
        StringBuilder result = new StringBuilder();
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
            XSSFSheet sheet = workbook.getSheetAt(0);//get first sheet

            for (int i = 0; i < sheet.getPhysicalNumberOfRows(); i++) {
                XSSFRow row = sheet.getRow(i);
                if (row != null) {
                    for (int j = 0; j < row.getPhysicalNumberOfCells(); j++) {
                        result.append(row.getCell(j)).append(" ");
                    }
                    result.append("\n");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }

        return result.toString();
    }
}
