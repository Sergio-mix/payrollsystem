package co.edu.unbosque.payrollsystem.repository;

import co.edu.unbosque.payrollsystem.model.CountryCode;
import co.edu.unbosque.payrollsystem.repository.jpa.CountryCodeJpa;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Data
@Repository(value = "CountryCodeRepository")
public class CountryCodeRepository {

    @Autowired
    private CountryCodeJpa countryCodeJpa;

    /**
     * Method that gets all countries
     *
     * @return List<CountryCodeEntity> list of countries
     */
    public List<CountryCode> findAll() {
        return countryCodeJpa.findAll();
    }
}
