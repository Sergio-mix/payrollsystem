package co.edu.unbosque.payrollsystem.service;

import co.edu.unbosque.payrollsystem.component.DateCalendarComponent;
import co.edu.unbosque.payrollsystem.component.ExcelComponent;
import co.edu.unbosque.payrollsystem.dto.PayrollFile;
import co.edu.unbosque.payrollsystem.dto.PayrollFileData;
import co.edu.unbosque.payrollsystem.dto.PayrollFileDynamic;
import co.edu.unbosque.payrollsystem.exception.PayrollException;
import co.edu.unbosque.payrollsystem.model.*;
import co.edu.unbosque.payrollsystem.repository.*;
import co.edu.unbosque.payrollsystem.service.validation.PayrollValidationDataServiceImpl;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.util.*;
import java.util.stream.IntStream;

@Service(value = "payrollService")
public class PayrollServiceImpl {
    @Autowired
    private PayrollDataRepository payrollDataRepository;

    @Autowired
    private PayrollDynamicRepository payrollDynamicRepository;

    @Autowired
    private DateCalendarComponent dateCalendar;

    @Autowired
    private ContributorRepository contributorRepository;

    @Autowired
    private PayrollValidationDataServiceImpl payrollValidation;

    @Autowired
    private ExcelComponent excelComponent;

    public Optional<Payroll> addPayroll(MultipartFile file) throws PayrollException, IOException {
        XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
        XSSFSheet sheet = workbook.getSheetAt(0);

        PayrollFile payrollFile = setPayrollDataDto(sheet);
        payrollFile.setHeaders(setListHeadersDto(sheet));
        payrollFile.setHeadersData(setListHeadersDataDto(sheet));
        payrollFile.setHeadersDataDynamic(setListHeadersDynamicDto(sheet));
        List<PayrollFileData> setPayrollFileDataDto = setPayrollFileDataDto(sheet, payrollFile.getHeadersDataDynamic());
        payrollFile.setPayrollFileData(setPayrollFileDataDto);
        payrollValidation.validatePayroll(payrollFile);//validate payroll

        return registerPayroll(payrollFile, setPayrollFileDataDto);
    }

    @Transactional(rollbackOn = {Payroll.class, PayrollData.class, Contributor.class})
    Optional<Payroll> registerPayroll(PayrollFile payrollFile, List<PayrollFileData> payrollFileData) {
        try {
            Optional<Payroll> payroll = createPayroll(payrollFile.getTypeDocument(), payrollFile.getDocumentNumber(),
                    payrollFile.getBusinessName(), payrollFile.getReference(), payrollFile.getRequest());

            List<PayrollData> listPayrollData = new ArrayList<>();
            for (PayrollFileData data : payrollFileData) {
                Optional<Contributor> contributor = creteContributor(data.getNameOfTheContributor(), data.getTypeDocument(), data.getDocumentNumber().toString());
                Optional<PayrollData> payrollData = createPayrollData(payroll.get(), contributor.get(), data);
                Optional<PayrollDynamic> payrollDynamic = createPayrollDynamic(payrollData.get(), data.getDynamicData());
                payrollData.get().setPayrollDynamic(payrollDynamic.get());
                listPayrollData.add(payrollDataRepository.save(payrollData.get()).get());
            }

            payroll.get().setPayrollData(listPayrollData);
            return payrollValidation.getPayroll().save(payroll.get());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private PayrollFile setPayrollDataDto(final XSSFSheet sheet) {
        PayrollFile payrollFile = new PayrollFile();
        payrollFile.setTypeDocument(excelComponent.formatParseString(sheet.getRow(3).getCell(1)));
        payrollFile.setDocumentNumber(excelComponent.formatParseInteger(sheet.getRow(3).getCell(2)));
        payrollFile.setBusinessName(excelComponent.formatParseString(sheet.getRow(3).getCell(3)));
        payrollFile.setReference(excelComponent.formatParseInteger(sheet.getRow(3).getCell(4)));
        payrollFile.setRequest(excelComponent.formatParseString(sheet.getRow(3).getCell(5)));
        return payrollFile;
    }

    private List<String> setListHeadersDto(final XSSFSheet sheet) {
        List<String> headers = new ArrayList<>();
        sheet.getRow(2).forEach(cell -> {
            if (cell.getColumnIndex() > 0 && cell.getColumnIndex() < 6) {
                headers.add(excelComponent.formatParseString(sheet.getRow(2).getCell(cell.getColumnIndex())));
            }
        });
        return headers;
    }

    private List<String> setListHeadersDataDto(final XSSFSheet sheet) {
        List<String> headersData = new ArrayList<>();
        sheet.getRow(5).forEach(cell -> {
            if (cell.getColumnIndex() < 13) {
                headersData.add(excelComponent.formatParseString(sheet.getRow(5).getCell(cell.getColumnIndex())));
            }
        });
        return headersData;
    }

    private List<String> setListHeadersDynamicDto(final XSSFSheet sheet) {
        List<String> headersDynamic = new ArrayList<>();
        sheet.getRow(5).forEach(cell -> {
            if (cell.getColumnIndex() > 13) {
                headersDynamic.add(excelComponent.formatParseString(sheet.getRow(5).getCell(cell.getColumnIndex())));
            }
        });
        return headersDynamic;
    }

    private List<PayrollFileData> setPayrollFileDataDto(final XSSFSheet sheet, List<String> headersDynamic) {
        List<PayrollFileData> listOrder = new ArrayList<>();
        IntStream.range(6, sheet.getPhysicalNumberOfRows() + 3).mapToObj(sheet::getRow).forEach(rowListContributor -> {
            PayrollFileData order = new PayrollFileData();
            order.setOrder(excelComponent.formatParseInteger(rowListContributor.getCell(0)));
            order.setTypeDocument(excelComponent.formatParseString(rowListContributor.getCell(1)));
            order.setDocumentNumber(excelComponent.formatParseInteger(rowListContributor.getCell(2)));
            order.setNameOfTheContributor(excelComponent.formatParseString(rowListContributor.getCell(3)));
            order.setPosition(excelComponent.formatParseString(rowListContributor.getCell(4)));
            order.setYear(excelComponent.formatParseInteger(rowListContributor.getCell(5)));
            order.setMonth(excelComponent.formatParseInteger(rowListContributor.getCell(6)));
            order.setSalary(excelComponent.formatParseFloat(rowListContributor.getCell(7)));
            order.setWorkedDays(excelComponent.formatParseInteger(rowListContributor.getCell(8)));
            order.setDaysOfDisability(excelComponent.formatParseInteger(rowListContributor.getCell(9)));
            order.setLeaveDays(excelComponent.formatParseInteger(rowListContributor.getCell(10)));
            order.setTotalDays(excelComponent.formatParseInteger(rowListContributor.getCell(11)));
            order.setDateOfAdmission(excelComponent.formatParseDate(rowListContributor.getCell(12)));
            if (!headersDynamic.isEmpty()) {
                order.setDynamicData(setPayrollFileDynamicDto(rowListContributor, headersDynamic));
            }
            listOrder.add(order);
        });
        return listOrder;
    }


    private PayrollFileDynamic setPayrollFileDynamicDto(final XSSFRow sheet, final List<String> headersDynamic) {
        PayrollFileDynamic dynamic = new PayrollFileDynamic();
        for (String header : headersDynamic) {
            final int index = Arrays.asList(payrollValidation.headersDynamicList).indexOf(payrollValidation.getValidation().removeAccents(header).replaceAll("\\s+", "").toUpperCase());
            if (index != -1) {
                Field field = dynamic.getClass().getDeclaredFields()[index];
                field.setAccessible(true);
                try {
                    field.set(dynamic, excelComponent.formatParseFloatV2(sheet.getCell(headersDynamic.indexOf(header) + 14)));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return dynamic;
    }

    @Transactional(rollbackOn = {Payroll.class})
    Optional<Payroll> createPayroll(String typeDocument, Integer documentNumber,
                                    String businessName, Integer reference,
                                    String request) {
        Payroll payroll = new Payroll();
        payroll.setTypeDocument(payrollValidation.getTypeDocument().findByName(typeDocument.toUpperCase()).get());
        payroll.setDocumentNumber(documentNumber.toString());
        payroll.setBusinessName(businessName.toUpperCase());
        payroll.setReference(reference.toString());
        payroll.setRequest(request.toUpperCase());
        payroll.setPayrollData(null);
        payroll.setDate(dateCalendar.getDate());
        return payrollValidation.getPayroll().save(payroll);
    }

    @Transactional(rollbackOn = {PayrollData.class})
    Optional<PayrollData> createPayrollData(Payroll payroll, Contributor contributor, PayrollFileData data) throws ParseException {
        PayrollData payrollData = new PayrollData();
        payrollData.setPayroll(payroll);
        payrollData.setContributor(contributor);
        payrollData.setPosition(data.getPosition().toUpperCase());
        payrollData.setCollaboratorDate(dateCalendar.dateFormat("yyyy-MM-dd", data.getYear() + "-" + data.getMonth() + "-01"));
        payrollData.setSalary(data.getSalary());
        payrollData.setWorkedDays(data.getWorkedDays());
        payrollData.setDaysOfDisability(data.getDaysOfDisability());
        payrollData.setLeaveDays(data.getLeaveDays());
        payrollData.setTotalDays(data.getTotalDays());
        payrollData.setDateOfAdmission(data.getDateOfAdmission());
        return payrollDataRepository.save(payrollData);
    }

    @Transactional(rollbackOn = {Contributor.class})
    Optional<Contributor> creteContributor(String name, String typeDocument, String documentNumber) {
        Optional<TypeDocument> typeDocumentOptional = payrollValidation.getTypeDocument().findByName(typeDocument.toUpperCase());
        Optional<Contributor> contributor = contributorRepository.existsContributor(typeDocumentOptional.get(), documentNumber);
        if (contributor.isEmpty()) {
            contributor = contributorRepository.save(new Contributor(typeDocumentOptional.get(), documentNumber, name.toUpperCase()));
        }
        return contributor;
    }

    @Transactional(rollbackOn = {PayrollDynamic.class})
    Optional<PayrollDynamic> createPayrollDynamic(PayrollData payrollData, PayrollFileDynamic payrollFileDynamic) {
        PayrollDynamic payrollDynamic = new PayrollDynamic();
        payrollDynamic.setPayrollData(payrollData);
        payrollDynamic.setMinimumWage(payrollFileDynamic.getMinimumWage());
        payrollDynamic.setSupport(payrollFileDynamic.getSupport());
        payrollDynamic.setOvertimeHour(payrollFileDynamic.getOvertimeHour());
        payrollDynamic.setOvertimeHourFa(payrollFileDynamic.getOvertimeHourFa());
        payrollDynamic.setCommissions(payrollFileDynamic.getCommissions());
        payrollDynamic.setHolidays(payrollFileDynamic.getHolidays());
        payrollDynamic.setRequiredHoliday(payrollFileDynamic.getRequiredHoliday());
        payrollDynamic.setAjAporIns(payrollFileDynamic.getAjAporIns());
        payrollDynamic.setWithdrawalBonus(payrollFileDynamic.getWithdrawalBonus());
        payrollDynamic.setCompensation(payrollFileDynamic.getCompensation());
        payrollDynamic.setInability(payrollFileDynamic.getInability());
        return payrollDynamicRepository.save(payrollDynamic);
    }

}
