package co.edu.unbosque.payrollsystem.service;

import co.edu.unbosque.payrollsystem.model.User;
import co.edu.unbosque.payrollsystem.repository.UserRepository;
import lombok.Data;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Class that implements the UserDetailsService interface to obtain user data
 */
@Data
@Service(value = "CustomUserServiceImpl")
public class CustomUserServiceImpl implements UserDetailsService {
    /**
     * User repository
     */
    private final UserRepository userDetailsRepository;

    /**
     * Method that returns the user data
     *
     * @param userDetailsRepository user repository
     */
    public CustomUserServiceImpl(UserRepository userDetailsRepository) {
        this.userDetailsRepository = userDetailsRepository;
    }

    /**
     * Method that returns the user data
     *
     * @param username username
     * @return UserDetails
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userDetailsRepository.findByUsername(username);

        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User Not Found with userName" + username);
        }

        return user.get();
    }

}