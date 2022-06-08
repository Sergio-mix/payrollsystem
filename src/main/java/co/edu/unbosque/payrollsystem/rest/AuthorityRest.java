package co.edu.unbosque.payrollsystem.rest;

import co.edu.unbosque.payrollsystem.dto.ReplyMessage;
import co.edu.unbosque.payrollsystem.model.Authority;
import co.edu.unbosque.payrollsystem.service.AuthorityServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController(value = "AuthorityRest")
@RequestMapping(value = "/authority/api/v1")
public class AuthorityRest {

    @Autowired
    private AuthorityServiceImpl authorityService;

    @GetMapping(value = "/all")
    public ResponseEntity<?> allAuthority() {
        try {
            List<Authority> list = authorityService.findAll();
            return list.isEmpty() ? new ResponseEntity<>(list, HttpStatus.NO_CONTENT)
                    : ResponseEntity.ok(list);
        } catch (Exception e) {
            return new ResponseEntity<>(ReplyMessage.ERROR_505, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
