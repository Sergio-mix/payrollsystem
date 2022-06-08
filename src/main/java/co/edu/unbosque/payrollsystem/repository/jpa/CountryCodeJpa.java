package co.edu.unbosque.payrollsystem.repository.jpa;

import co.edu.unbosque.payrollsystem.model.CountryCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryCodeJpa extends JpaRepository<CountryCode, Long> {

}
