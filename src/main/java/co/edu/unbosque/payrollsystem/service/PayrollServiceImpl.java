package co.edu.unbosque.payrollsystem.service;

import co.edu.unbosque.payrollsystem.model.*;
import co.edu.unbosque.payrollsystem.repository.*;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

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

    public String addPayroll(MultipartFile file) {
        StringBuilder result = new StringBuilder();
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
            XSSFSheet sheet = workbook.getSheetAt(0);

            List<PayrollData> payrollDataList = new ArrayList<>();

            for (int i = 5; i < sheet.getPhysicalNumberOfRows() + 5; i++) {
                XSSFRow row = sheet.getRow(i);
                Optional<Contributor> contributor = creteContributor(row.getCell(3).toString(), row.getCell(1).toString(), row.getCell(2).toString());
//                createPayrollData(null,contributor.get(),);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return result.toString();
    }

    private Optional<Payroll> createPayroll(String typeDocument, String documentNumber, String businessName, String reference, String request) {
        Payroll payroll = new Payroll();
        payroll.setTypeDocument(typeDocumentRepository.findByName(typeDocument).get());
        payroll.setDocumentNumber(documentNumber);
        payroll.setBusinessName(businessName);
        payroll.setReference(reference);
        payroll.setRequest(request);
        payroll.setDate(new Date());
        return payrollRepository.save(payroll);
    }

    private Optional<PayrollData> createPayrollData(Payroll payroll, Contributor contributor,
                                                    Date collaboratorDate, Integer salary,
                                                    Integer workedDays, Integer daysOfDisability,
                                                    Integer leaveDays, Integer totalDays, Date dateOfAdmission,
                                                    List<PayrollDynamic> payrollDynamics) {
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
        payrollData.setPayrollDynamic(payrollDynamics);
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
