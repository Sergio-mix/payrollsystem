package co.edu.unbosque.payrollsystem.repository.jpa;

import co.edu.unbosque.payrollsystem.model.Payroll;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PayrollJpa extends JpaRepository<Payroll, Integer> {

    boolean existsByTypeDocumentNameAndDocumentNumber(String typeDocument, String documentNumber);

    boolean existsByReference(String reference);
}
