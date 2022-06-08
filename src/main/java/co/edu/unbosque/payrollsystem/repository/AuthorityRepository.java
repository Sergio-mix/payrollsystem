package co.edu.unbosque.payrollsystem.repository;

import co.edu.unbosque.payrollsystem.model.Authority;
import co.edu.unbosque.payrollsystem.repository.jpa.AuthorityJpa;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Data
@Repository(value = "AuthorityRepository")
public class AuthorityRepository {

    @Autowired
    private AuthorityJpa authorityJpa;

    /**
     * Method that allows to obtain all the records of the authority table
     *
     * @return List<AuthorityEntity> list of all the records of the authority table
     */
    public List<Authority> findAll() {
        return authorityJpa.findAll();
    }

}
