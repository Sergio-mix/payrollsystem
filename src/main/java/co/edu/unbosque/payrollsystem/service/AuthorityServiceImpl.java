package co.edu.unbosque.payrollsystem.service;

import co.edu.unbosque.payrollsystem.model.Authority;
import co.edu.unbosque.payrollsystem.repository.AuthorityRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The type Authority service.
 */
@Data
@Service(value = "AuthorityServiceImpl")
public class AuthorityServiceImpl {
    /**
     * The Authority repository.
     */
    @Autowired
    private AuthorityRepository authority;

    /**
     * Gets all.
     *
     * @return the all
     */
    public List<Authority> findAll() {
        return authority.findAll();
    }

}
