package co.edu.unbosque.payrollsystem.service;

import co.edu.unbosque.payrollsystem.component.ExcelComponent;
import co.edu.unbosque.payrollsystem.dto.PayrollFile;
import co.edu.unbosque.payrollsystem.dto.PayrollFileData;
import co.edu.unbosque.payrollsystem.exception.PayrollException;
import co.edu.unbosque.payrollsystem.model.*;
import co.edu.unbosque.payrollsystem.repository.*;
import co.edu.unbosque.payrollsystem.service.validation.PayrollValidationDataServiceImpl;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.IntStream;

@Service(value = "payrollService")
public class PayrollServiceImpl {
    @Autowired
    private PayrollDynamicRepository payrollDynamicRepository;

    @Autowired
    private PayrollDataRepository payrollDataRepository;

    @Autowired
    private PayrollRepository payrollRepository;

    @Autowired
    private ContributorRepository contributorRepository;

    @Autowired
    private TypeDocumentRepository typeDocumentRepository;

    @Autowired
    private PayrollValidationDataServiceImpl payrollValidationDataService;

    @Autowired
    private ExcelComponent excelComponent;

    public String addPayroll(MultipartFile file) throws PayrollException, IOException {
        StringBuilder result = new StringBuilder();
        XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
        XSSFSheet sheet = workbook.getSheetAt(0);

        PayrollFile payrollFile = setPayrollDataDto(sheet);
        payrollFile.setHeaders(setListHeaders(sheet));
        payrollFile.setHeadersData(setListHeadersData(sheet));
        payrollFile.setPayrollFileData(setPayrollFileDataContributor(sheet));
        System.out.println(payrollFile);

        payrollValidationDataService.validatePayroll(payrollFile);//validate payroll

        result.append("Se ha cargado el archivo correctamente");

        return result.toString();
    }

    private PayrollFile setPayrollDataDto(final XSSFSheet sheet) {
        PayrollFile payrollFile = new PayrollFile();
        payrollFile.setTypeDocument(excelComponent.formatParse(sheet.getRow(3).getCell(1)));
        payrollFile.setDocumentNumber(excelComponent.formatParse(sheet.getRow(3).getCell(2)));
        payrollFile.setBusinessName(excelComponent.formatParse(sheet.getRow(3).getCell(3)));
        payrollFile.setReference(excelComponent.formatParse(sheet.getRow(3).getCell(4)));
        payrollFile.setRequest(excelComponent.formatParse(sheet.getRow(3).getCell(5)));
        return payrollFile;
    }

    private List<Object> setListHeaders(final XSSFSheet sheet) {
        List<Object> headers = new ArrayList<>();
        sheet.getRow(2).forEach(cell -> {
            if (cell.getColumnIndex() > 0 && cell.getColumnIndex() < 6) {
                headers.add(excelComponent.formatParse(sheet.getRow(2).getCell(cell.getColumnIndex())));
            }
        });

        return headers;
    }

    private List<Object> setListHeadersData(final XSSFSheet sheet) {
        List<Object> headersData = new ArrayList<>();
        sheet.getRow(5).forEach(cell -> {
            if (cell.getColumnIndex() < 13) {
                headersData.add(excelComponent.formatParse(sheet.getRow(5).getCell(cell.getColumnIndex())));
            }
        });

        return headersData;
    }

    private List<PayrollFileData> setPayrollFileDataContributor(final XSSFSheet sheet) {
        List<PayrollFileData> listContributor = new ArrayList<>();
        IntStream.range(6, sheet.getPhysicalNumberOfRows() + 3).mapToObj(sheet::getRow).forEach(rowListContributor -> {
            PayrollFileData contributor = new PayrollFileData();
            contributor.setOrder(excelComponent.formatParse(rowListContributor.getCell(0)));
            contributor.setTypeDocument(excelComponent.formatParse(rowListContributor.getCell(1)));
            contributor.setDocumentNumber(excelComponent.formatParse(rowListContributor.getCell(2)));
            contributor.setNameOfTheContributor(excelComponent.formatParse(rowListContributor.getCell(3)));
            contributor.setPosition(excelComponent.formatParse(rowListContributor.getCell(4)));
            contributor.setYear(excelComponent.formatParse(rowListContributor.getCell(5)));
            contributor.setMonth(excelComponent.formatParse(rowListContributor.getCell(6)));
            contributor.setSalary(excelComponent.formatParse(rowListContributor.getCell(7)));
            contributor.setWorkedDays(excelComponent.formatParse(rowListContributor.getCell(8)));
            contributor.setDaysOfDisability(excelComponent.formatParse(rowListContributor.getCell(9)));
            contributor.setLeaveDays(excelComponent.formatParse(rowListContributor.getCell(10)));
            contributor.setTotalDays(excelComponent.formatParse(rowListContributor.getCell(11)));
            contributor.setDateOfAdmission(excelComponent.formatParse(rowListContributor.getCell(12)));
            listContributor.add(contributor);
        });
        return listContributor;
    }

    private Optional<Payroll> createPayroll(String typeDocument, String documentNumber,
                                            String businessName, String reference,
                                            String request, List<PayrollData> payrollDataList) {
        Payroll payroll = new Payroll();
        payroll.setTypeDocument(typeDocumentRepository.findByName(typeDocument).get());
        payroll.setDocumentNumber(documentNumber);
        payroll.setBusinessName(businessName);
        payroll.setReference(reference);
        payroll.setRequest(request);
        payroll.setDate(new Date());
        payroll.setPayrollData(payrollDataList);
        return payrollRepository.save(payroll);
    }

    private Optional<PayrollData> createPayrollData(Payroll payroll, Contributor contributor,
                                                    Date collaboratorDate, Integer salary,
                                                    Integer workedDays, Integer daysOfDisability,
                                                    Integer leaveDays, Integer totalDays, Date dateOfAdmission) {
        PayrollData payrollData = new PayrollData();
        payrollData.setPayroll(payroll);
        payrollData.setContributor(contributor);
        payrollData.setCollaboratorDate(collaboratorDate);
        payrollData.setSalary(salary);
        payrollData.setWorkedDays(workedDays);
        payrollData.setDaysOfDisability(daysOfDisability);
        payrollData.setLeaveDays(leaveDays);
        payrollData.setTotalDays(totalDays);
        payrollData.setDateOfAdmission(dateOfAdmission);
        return payrollDataRepository.save(payrollData);
    }

    private Optional<Contributor> creteContributor(String name, String typeDocument, String documentNumber) {
        Optional<Contributor> result;
        Optional<TypeDocument> typeDocumentOptional = typeDocumentRepository.findByName(typeDocument.toUpperCase());
        Optional<Contributor> contributor = contributorRepository.existsContributor(typeDocumentOptional.get(), documentNumber);
        if (contributor.isEmpty()) {
            Contributor createdContributor = new Contributor();
            createdContributor.setNameOfTheContributor(name.toUpperCase());
            createdContributor.setTypeDocument(typeDocumentOptional.get());
            createdContributor.setDocumentNumber(documentNumber);
            result = contributorRepository.save(createdContributor);
        } else {
            result = contributor;
        }
        return result;
    }

}
