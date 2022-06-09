package co.edu.unbosque.payrollsystem.rest;

import co.edu.unbosque.payrollsystem.dto.ReplyMessage;
import co.edu.unbosque.payrollsystem.exception.PayrollException;
import co.edu.unbosque.payrollsystem.service.PayrollServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController(value = "PayrollRest")
@RequestMapping(value = "/payroll/api/v1")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PayrollRest {

    @Autowired
    private PayrollServiceImpl payrollService;

    @PostMapping(value = "/save")
    public ResponseEntity<?> addPayroll(@RequestBody MultipartFile file) {
        ResponseEntity<?> response;
        try {
            String map = payrollService.addPayroll(file);
            if (!map.isEmpty()) {
                response = new ResponseEntity<>(map, HttpStatus.OK);
            } else {
                response = new ResponseEntity<>("F", HttpStatus.BAD_REQUEST);
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