package co.edu.unbosque.payrollsystem.repository;

import co.edu.unbosque.payrollsystem.model.TypeDocument;
import co.edu.unbosque.payrollsystem.repository.jpa.TypeDocumentJpa;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Data
@Repository(value = "TypeDocumentRepository")
public class TypeDocumentRepository {

    @Autowired
    private TypeDocumentJpa typeDocument;

    /**
     * Method that allows obtaining all types of documents
     *
     * @return List<TypeDocumentEntity> list of types of documents
     */
    public List<TypeDocument> findAll() {
        return typeDocument.findAll();
    }

    public Optional<TypeDocument> findByName(String name) {
        return Optional.ofNullable(typeDocument.findByName(name));
    }
}
