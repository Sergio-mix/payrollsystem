package co.edu.unbosque.payrollsystem.rest;

import co.edu.unbosque.payrollsystem.dto.ReplyMessage;
import co.edu.unbosque.payrollsystem.exception.PayrollException;
import co.edu.unbosque.payrollsystem.model.Payroll;
import co.edu.unbosque.payrollsystem.model.PayrollData;
import co.edu.unbosque.payrollsystem.model.Record;
import co.edu.unbosque.payrollsystem.service.PayrollServiceImpl;
import co.edu.unbosque.payrollsystem.service.UserHistoryServiceImpl;
import org.apache.poi.xssf.binary.XSSFBParseException;
import org.apache.xmlbeans.XMLStreamValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Optional;

@RestController(value = "PayrollRest")
@RequestMapping(value = "/payroll/api/v1")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PayrollRest {

    @Autowired
    private PayrollServiceImpl payrollService;

    @Autowired
    private UserHistoryServiceImpl userHistoryServiceImpl;

    @PostMapping(value = "/save")
    public ResponseEntity<?> addPayrollFile(@RequestBody MultipartFile file, Principal userLogin, HttpServletRequest request) {
        ResponseEntity<?> response;
        try {
            if (file.isEmpty() || !file.getContentType().equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
                response = new ResponseEntity<>("File is empty or not a xlsx file", HttpStatus.INTERNAL_SERVER_ERROR);
            } else {
                Optional<Payroll> payroll = payrollService.addPayroll(file);
                if (payroll.isEmpty()) {
                    response = new ResponseEntity<>("Registration failed", HttpStatus.INTERNAL_SERVER_ERROR);
                } else {
                    userHistoryServiceImpl.save(userLogin.getName(), new Record(Record.POST, "register payroll"),
                            "the name was added " + payroll.get().getReference(), request);//Save record
                    response = new ResponseEntity<>("Successful registration", HttpStatus.OK);
                }
            }
        } catch (PayrollException e) {
            response = new ResponseEntity<>(e.getPayrollFile(), HttpStatus.BAD_REQUEST);
        } catch (NullPointerException e) {
            response = new ResponseEntity<>(ReplyMessage.FORMAT_NOT_VALID, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            response = new ResponseEntity<>(ReplyMessage.ERROR_505, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }
}