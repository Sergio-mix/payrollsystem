package co.edu.unbosque.payrollsystem.repository.jpa;

import co.edu.unbosque.payrollsystem.model.PayrollData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PayrollDataJpa extends JpaRepository<PayrollData, Integer> {

}
