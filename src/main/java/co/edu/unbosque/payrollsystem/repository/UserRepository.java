package co.edu.unbosque.payrollsystem.repository;

import co.edu.unbosque.payrollsystem.model.User;
import co.edu.unbosque.payrollsystem.repository.jpa.UserJpa;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Data
@Repository(value = "UserRepository")
public class UserRepository {

    @Autowired
    private UserJpa userJpa;

    public Optional<User> save(User user) {
        return Optional.of(userJpa.save(user));
    }

    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(userJpa.findByUsername(username));
    }

    public List<User> findAll() {
        return userJpa.findAllUsers();
    }

    public Boolean existsByEmail(String email) {
        return userJpa.existsByEmail(email);
    }

    public Boolean existsByUsername(String username) {
        return userJpa.existsByUsername(username);
    }

    public Optional<User> findById(Integer id) {
        return userJpa.findById(id);
    }

    public Optional<User> findByEmail(String email) {
        return Optional.ofNullable(userJpa.findByEmail(email));
    }

    public Boolean existsByCellPhone(Integer countryCode, String cellPhone) {
        return userJpa.existsByCountryCodeIdAndCellPhone(countryCode, cellPhone);
    }

    public Boolean existsDocument(Integer type, String number) {
        return userJpa.existsByTypeDocumentIdAndDocumentNumber(type, number);
    }
}
