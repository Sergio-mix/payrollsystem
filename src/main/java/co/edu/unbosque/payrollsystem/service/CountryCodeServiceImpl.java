package co.edu.unbosque.payrollsystem.service;

import co.edu.unbosque.payrollsystem.model.CountryCode;
import co.edu.unbosque.payrollsystem.repository.CountryCodeRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The type Country code service impl.
 */
@Data
@Service(value = "CountryCodeServiceImpl")
public class CountryCodeServiceImpl {
    /**
     * The Country code repository.
     */
    @Autowired
    private CountryCodeRepository countryCode;

    /**
     * Gets all country codes.
     *
     * @return the all country codes
     */
    public List<CountryCode> getAll() {
        return countryCode.findAll();
    }
}
