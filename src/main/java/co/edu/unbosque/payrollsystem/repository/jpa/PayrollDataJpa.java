package co.edu.unbosque.payrollsystem.repository.jpa;

import co.edu.unbosque.payrollsystem.model.PayrollData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PayrollDataJpa extends JpaRepository<PayrollData, Integer> {

    List<PayrollData> findByPayrollId(Integer payrollId);

    @Query(value = "SELECT AVG(p.salary) FROM PayrollData p")
    Float avgSalary();

}
