package co.edu.unbosque.payrollsystem.repository.jpa;

import co.edu.unbosque.payrollsystem.model.Contributor;
import co.edu.unbosque.payrollsystem.model.TypeDocument;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContributorJpa extends JpaRepository<Contributor, Integer> {

    Contributor findByTypeDocumentAndDocumentNumber(TypeDocument type, String number);

    Integer countAllByState(String state);
}
