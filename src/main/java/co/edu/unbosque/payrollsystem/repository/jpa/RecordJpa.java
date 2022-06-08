package co.edu.unbosque.payrollsystem.repository.jpa;

import co.edu.unbosque.payrollsystem.model.Record;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecordJpa extends JpaRepository<Record, Integer> {

    Record findByName(String name);
}
