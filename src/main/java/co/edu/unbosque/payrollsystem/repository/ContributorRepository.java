package co.edu.unbosque.payrollsystem.repository;

import co.edu.unbosque.payrollsystem.model.Contributor;
import co.edu.unbosque.payrollsystem.model.TypeDocument;
import co.edu.unbosque.payrollsystem.repository.jpa.ContributorJpa;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Data
@Repository(value = "ContributorRepository")
public class ContributorRepository {

    @Autowired
    private ContributorJpa contributorJpa;

    public Optional<Contributor> save(Contributor contributor) {
        return Optional.of(contributorJpa.save(contributor));
    }

    public Optional<Contributor> existsContributor(TypeDocument type, String number) {
        return Optional.ofNullable(contributorJpa.findByTypeDocumentAndDocumentNumber(type, number));
    }
}
