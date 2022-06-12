package co.edu.unbosque.payrollsystem.repository;

import co.edu.unbosque.payrollsystem.model.Payroll;
import co.edu.unbosque.payrollsystem.model.PayrollData;
import co.edu.unbosque.payrollsystem.model.PayrollDynamic;
import co.edu.unbosque.payrollsystem.repository.jpa.PayrollDynamicJpa;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Data
@Repository(value = "PayrollDynamicRepository")
public class PayrollDynamicRepository {

    @Autowired
    private PayrollDynamicJpa payrollDynamicJpa;

    public Optional<PayrollDynamic> save(PayrollDynamic payrollDynamic) {
        return Optional.of(payrollDynamicJpa.save(payrollDynamic));
    }

}
