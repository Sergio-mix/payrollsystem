package co.edu.unbosque.payrollsystem.repository;

import co.edu.unbosque.payrollsystem.model.Payroll;
import co.edu.unbosque.payrollsystem.repository.jpa.PayrollDataJpa;
import co.edu.unbosque.payrollsystem.repository.jpa.PayrollJpa;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Data
@Repository(value = "PayrollRepository")
public class PayrollRepository {

    @Autowired
    private PayrollJpa payrollJpa;

    public Optional<Payroll> save(Payroll payroll) {
        return Optional.of(payrollJpa.save(payroll));
    }


    public Optional<List<Payroll>> getAll() {
        return Optional.of(payrollJpa.findAll());
    }

    public boolean existsPayroll(String typeDocument, String documentNumber) {
        return payrollJpa.existsByTypeDocumentNameAndDocumentNumber(typeDocument, documentNumber);
    }

    public boolean existsReference(String reference) {
        return payrollJpa.existsByReference(reference);
    }

    public Integer countAll() {
        return payrollJpa.countAllByState(Payroll.ACTIVE);
    }

}
