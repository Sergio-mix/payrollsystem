package co.edu.unbosque.payrollsystem.repository.jpa;

import co.edu.unbosque.payrollsystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface UserJpa extends JpaRepository<User, Integer> {

    @Query("select u from User u order by u.enabled desc, u.lastNames asc")
    List<User> findAllUsers();

    User findByUsername(String username);

    User findByEmail(String email);

    Boolean existsByEmail(String email);

    Boolean existsByUsername(String username);

    Boolean existsByCountryCodeIdAndCellPhone(Integer countryCode, String cellPhone);

    Boolean existsByTypeDocumentIdAndDocumentNumber(Integer type, String number);
}
