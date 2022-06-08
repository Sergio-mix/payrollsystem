package co.edu.unbosque.payrollsystem.repository;

import co.edu.unbosque.payrollsystem.model.TypeDocument;
import co.edu.unbosque.payrollsystem.repository.jpa.TypeDocumentJpa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository(value = "TypeDocumentRepository")
public class TypeDocumentRepository {

    @Autowired
    private TypeDocumentJpa typeDocumentJpa;

    /**
     * Method that allows obtaining all types of documents
     *
     * @return List<TypeDocumentEntity> list of types of documents
     */
    public List<TypeDocument> findAll() {
        return typeDocumentJpa.findAll();
    }
}
