package co.edu.unbosque.payrollsystem.rest;

import co.edu.unbosque.payrollsystem.dto.ReplyMessage;
import co.edu.unbosque.payrollsystem.exception.PayrollException;
import co.edu.unbosque.payrollsystem.model.Payroll;
import co.edu.unbosque.payrollsystem.model.PayrollData;
import co.edu.unbosque.payrollsystem.service.PayrollServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@RestController(value = "PayrollRest")
@RequestMapping(value = "/payroll/api/v1")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PayrollRest {

    @Autowired
    private PayrollServiceImpl payrollService;

    @PostMapping(value = "/save")
    public ResponseEntity<?> addPayrollFile(@RequestBody MultipartFile file) {
        ResponseEntity<?> response;
        try {
            if (payrollService.addPayroll(file).isPresent()) {
                response = new ResponseEntity<>("Successful registration", HttpStatus.OK);
            } else {
                response = new ResponseEntity<>("Registration failed", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (PayrollException e) {
            response = new ResponseEntity<>(e.getPayrollFile(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            response = new ResponseEntity<>(ReplyMessage.ERROR_505, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }
}