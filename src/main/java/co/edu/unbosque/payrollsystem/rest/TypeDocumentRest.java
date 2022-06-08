package co.edu.unbosque.payrollsystem.rest;

import co.edu.unbosque.payrollsystem.dto.ReplyMessage;
import co.edu.unbosque.payrollsystem.model.TypeDocument;
import co.edu.unbosque.payrollsystem.service.TypeDocumentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController(value = "TypeDocumentRest")
@RequestMapping(value = "/type-document/api/v1")
public class TypeDocumentRest {

    @Autowired
    private TypeDocumentServiceImpl typeDocumentService;

    @GetMapping(value = "/all")
    public ResponseEntity<?> allTypeDocument() {
        ResponseEntity<?> response;
        try {
            List<TypeDocument> list = typeDocumentService.findAll();
            response = list.isEmpty() ? new ResponseEntity<>(list, HttpStatus.NO_CONTENT) : ResponseEntity.ok(list);
        } catch (Exception e) {
            response = new ResponseEntity<>(ReplyMessage.ERROR_505, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }
}
