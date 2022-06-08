package co.edu.unbosque.payrollsystem;

import co.edu.unbosque.payrollsystem.model.Authority;
import co.edu.unbosque.payrollsystem.model.CountryCode;
import co.edu.unbosque.payrollsystem.model.TypeDocument;
import co.edu.unbosque.payrollsystem.model.User;
import co.edu.unbosque.payrollsystem.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.annotation.PostConstruct;
import java.util.List;

@SpringBootApplication
public class PayrollsystemApplication {
    @Autowired
    private UserServiceImpl userServiceImpl;

    public static void main(String[] args) {
        SpringApplication.run(PayrollsystemApplication.class, args);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("*");
            }
        };
    }

    @PostConstruct
    public void init() {
        try {
            User user1 = new User();
            user1.setNames("Alejo");
            user1.setLastNames("sergio");
            user1.setEmail("alejo.80.123@gmail.com");
            user1.setPassword("Alejo1");
            user1.setUsername("Alejo");

            CountryCode countryCode = new CountryCode();
            countryCode.setId(1);
            countryCode.setCode(57);
            countryCode.setCountry("Colombia");

            user1.setCountryCode(countryCode);
            user1.setCellPhone("3232323");

            TypeDocument typeDocument = new TypeDocument();
            typeDocument.setId(1);
            typeDocument.setDescription("cedula de ciudadania");
            typeDocument.setName("CC");

            user1.setTypeDocument(typeDocument);
            user1.setDocumentNumber("32424234");

            Authority authority1 = new Authority();
            authority1.setId(1);
            authority1.setDescription("ROLE_ADMIN");
            authority1.setRoleCode(Authority.ADMIN);

            Authority authority2 = new Authority();
            authority2.setId(2);
            authority2.setDescription("ROLE_USER");
            authority2.setRoleCode(Authority.USER);

            user1.setAuthorities(List.of(authority1, authority2));

            System.out.println(userServiceImpl.saveUser(user1));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
