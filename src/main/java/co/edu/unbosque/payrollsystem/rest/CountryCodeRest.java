package co.edu.unbosque.payrollsystem.rest;

import co.edu.unbosque.payrollsystem.dto.ReplyMessage;
import co.edu.unbosque.payrollsystem.model.CountryCode;
import co.edu.unbosque.payrollsystem.service.CountryCodeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController(value = "CountryCodeRest")
@RequestMapping(value = "/country-code/api/v1")
public class CountryCodeRest {

    @Autowired
    private CountryCodeServiceImpl countryCodeServiceImpl;

    @GetMapping(value = "/all")
    public ResponseEntity<?> allCountryCode() {
        ResponseEntity<?> response;
        try {
            List<CountryCode> list = countryCodeServiceImpl.getAll();
            response = list.isEmpty() ? new ResponseEntity<>(list, HttpStatus.NO_CONTENT) : ResponseEntity.ok(list);
        } catch (Exception e) {
            response = new ResponseEntity<>(ReplyMessage.ERROR_505, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }
}
