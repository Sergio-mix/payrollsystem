package co.edu.unbosque.payrollsystem.repository;

import co.edu.unbosque.payrollsystem.model.Contributor;
import co.edu.unbosque.payrollsystem.model.Payroll;
import co.edu.unbosque.payrollsystem.model.TypeDocument;
import co.edu.unbosque.payrollsystem.repository.jpa.PayrollJpa;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Data
@Repository(value = "PayrollRepository")
public class PayrollRepository {

    @Autowired
    private PayrollJpa payrollJpa;

    public Optional<Payroll> save(Payroll payroll) {
        return Optional.of(payrollJpa.save(payroll));
    }

    public boolean existsPayroll(String typeDocument, String documentNumber) {
        return payrollJpa.existsByTypeDocumentNameAndDocumentNumber(typeDocument, documentNumber);
    }

    public boolean existsReference(String reference) {
        return payrollJpa.existsByReference(reference);
    }
}
