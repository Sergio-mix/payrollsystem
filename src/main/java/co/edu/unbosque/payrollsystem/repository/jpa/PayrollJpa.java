package co.edu.unbosque.payrollsystem.repository.jpa;

import co.edu.unbosque.payrollsystem.model.Payroll;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PayrollJpa extends JpaRepository<Payroll, Integer> {

    boolean existsByTypeDocumentNameAndDocumentNumber(String typeDocument, String documentNumber);

    boolean existsByReference(String reference);
    @NotNull
    List<Payroll> findAll();
}
