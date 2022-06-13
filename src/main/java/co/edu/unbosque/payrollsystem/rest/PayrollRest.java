package co.edu.unbosque.payrollsystem.rest;

import co.edu.unbosque.payrollsystem.dto.ReplyMessage;
import co.edu.unbosque.payrollsystem.exception.PayrollException;
import co.edu.unbosque.payrollsystem.model.Payroll;
import co.edu.unbosque.payrollsystem.model.PayrollData;
import co.edu.unbosque.payrollsystem.model.Record;
import co.edu.unbosque.payrollsystem.service.PayrollServiceImpl;
import co.edu.unbosque.payrollsystem.service.UserHistoryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@RestController(value = "PayrollRest")
@RequestMapping(value = "/payroll/api/v1")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PayrollRest {

    @Autowired
    private PayrollServiceImpl payrollService;

    @Autowired
    private UserHistoryServiceImpl userHistoryServiceImpl;

    @Async
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
            response = new ResponseEntity<>(ReplyMessage.ERROR_505, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    @GetMapping(value = "/all")
    public ResponseEntity<?> getAllPayrolls(Principal userLogin, HttpServletRequest request) {
        ResponseEntity<?> response;
        try {
            Optional<List<Payroll>> List = payrollService.getPayrolls();
            if (List.isEmpty()) {
                response = new ResponseEntity<>(ReplyMessage.ERROR_505, HttpStatus.INTERNAL_SERVER_ERROR);
            } else {
                response = List.get().isEmpty() ? new ResponseEntity<>(List, HttpStatus.NO_CONTENT) : new ResponseEntity<>(List, HttpStatus.OK);
                userHistoryServiceImpl.save(userLogin.getName(), new Record(Record.GET, "list payroll"), null, request);//Save record
            }
        } catch (Exception e) {
            response = new ResponseEntity<>(ReplyMessage.ERROR_505, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    @GetMapping(value = "/data/{id}")
    public ResponseEntity<?> getPayrollDataByPayrollId(@PathVariable("id") Integer payrollId, Principal userLogin, HttpServletRequest request) {
        ResponseEntity<?> response;
        try {
            Optional<List<PayrollData>> list = payrollService.getPayrollDataByPayrollId(payrollId);
            if (list.isEmpty()) {
                response = new ResponseEntity<>(ReplyMessage.ERROR_505, HttpStatus.INTERNAL_SERVER_ERROR);
            } else {
                response = list.get().isEmpty() ? new ResponseEntity<>(list, HttpStatus.NO_CONTENT) : new ResponseEntity<>(list, HttpStatus.OK);
                userHistoryServiceImpl.save(userLogin.getName(), new Record(Record.GET, "list payroll data by payroll"), null, request);//Save record
            }
        } catch (Exception e) {
            response = new ResponseEntity<>(ReplyMessage.ERROR_505, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    @Async
    @GetMapping(value = "/cont-contributors")
    public CompletableFuture<ResponseEntity<?>> getContContributors(Principal userLogin, HttpServletRequest request) {
        ResponseEntity<?> response;
        try {
            Integer cont = payrollService.getContContributor();
            response = new ResponseEntity<>(cont, HttpStatus.OK);
            userHistoryServiceImpl.save(
                    userLogin.getName(), new Record(Record.GET, "number of contributors"),
                    null, request);//Save record
        } catch (Exception e) {
            response = new ResponseEntity<>(ReplyMessage.ERROR_505, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return CompletableFuture.completedFuture(response);
    }

    @Async
    @GetMapping(value = "/cont-payrolls")
    public CompletableFuture<ResponseEntity<?>> getContPayroll(Principal userLogin, HttpServletRequest request) {
        ResponseEntity<?> response;
        try {
            Integer cont = payrollService.getContPayroll();
            response = new ResponseEntity<>(cont, HttpStatus.OK);
            userHistoryServiceImpl.save(
                    userLogin.getName(), new Record(Record.GET, "number of payrolls"),
                    null, request);//Save record
        } catch (Exception e) {
            response = new ResponseEntity<>(ReplyMessage.ERROR_505, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return CompletableFuture.completedFuture(response);
    }

    @Async
    @GetMapping(value = "/avg-salary")
    public CompletableFuture<ResponseEntity<?>> getAvgSalary(Principal userLogin, HttpServletRequest request) {
        ResponseEntity<?> response;
        try {
            Float avg = payrollService.getAvgSalary();
            response = new ResponseEntity<>(avg, HttpStatus.OK);
            userHistoryServiceImpl.save(
                    userLogin.getName(), new Record(Record.GET, "average salary"),
                    null, request);//Save record
        } catch (Exception e) {
            response = new ResponseEntity<>(ReplyMessage.ERROR_505, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return CompletableFuture.completedFuture(response);
    }
}