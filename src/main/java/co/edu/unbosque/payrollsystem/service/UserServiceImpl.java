package co.edu.unbosque.payrollsystem.service;

import co.edu.unbosque.payrollsystem.component.DateCalendarComponent;
import co.edu.unbosque.payrollsystem.dto.Usernames;
import co.edu.unbosque.payrollsystem.exception.UserException;
import co.edu.unbosque.payrollsystem.model.Authority;
import co.edu.unbosque.payrollsystem.model.User;
import co.edu.unbosque.payrollsystem.repository.UserRepository;
import co.edu.unbosque.payrollsystem.service.validation.UserRegisterValidationServiceImpl;
import co.edu.unbosque.payrollsystem.service.validation.UserUpdateValidationServiceImpl;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.*;

/**
 * The type User service impl.
 */
@Data
@Service(value = "UserServiceImpl")
public class UserServiceImpl {

    /**
     * The User repository.
     */
    @Autowired
    private UserRepository userRepository;

    /**
     * The Date calendar component.
     */
    @Autowired
    private DateCalendarComponent dateCalendar;

    @Autowired
    private UserRegisterValidationServiceImpl validateRegisterUser;

    @Autowired
    private UserUpdateValidationServiceImpl validateRegisterUpdate;

    /**
     * save user.
     *
     * @param user the user
     * @return the user
     */
    public Optional<User> saveUser(User user) throws UserException {
        validateRegisterUser.validateRegisterUser(user);//validate user
        List<Authority> authorities = (List<Authority>) user.getAuthorities();

        user.setNames(user.getNames().toUpperCase());
        user.setLastNames(user.getLastNames().toUpperCase());
        user.setAuthorities(null);
        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));//encrypt password
        user.setEmail(user.getEmail().toLowerCase());
        user.setPasswordExpiration(dateCalendar.addDaysCurrentDate(user.getDaysToExpire()));

        Optional<User> userSaved = userRepository.save(user);//save user

        userSaved.get().setAuthorities(authorities);
        return userRepository.save(userSaved.get());
    }

    /**
     * update user.
     *
     * @param userDataUpdate user data update
     * @param currentUser    user data current
     */
    public Optional<User> updateUser(User userDataUpdate, User currentUser) throws UserException {
        validateRegisterUpdate.validateUpdateUser(userDataUpdate, currentUser);//validate user
        //set data
        currentUser.setUsername(userDataUpdate.getUsername());//set username
        currentUser.setNames(userDataUpdate.getNames().toUpperCase());
        currentUser.setLastNames(userDataUpdate.getLastNames().toUpperCase());
        currentUser.setCellPhone(userDataUpdate.getCellPhone());
        currentUser.setDocumentNumber(userDataUpdate.getDocumentNumber());
        currentUser.setEmail(userDataUpdate.getEmail());//set email
        currentUser.setCountryCode(userDataUpdate.getCountryCode());
        currentUser.setTypeDocument(userDataUpdate.getTypeDocument());
        currentUser.setAuthorities((List<Authority>) userDataUpdate.getAuthorities());//set authorities

        //update days to expire
        if (!Objects.equals(userDataUpdate.getDaysToExpire(), currentUser.getDaysToExpire())) {
            currentUser.setDaysToExpire(userDataUpdate.getDaysToExpire());
            currentUser.setPasswordExpiration(dateCalendar.addDaysCurrentDate(userDataUpdate.getDaysToExpire()));
        }

        //update password
        if (!userDataUpdate.getPassword().isEmpty()) {
            currentUser.setPassword(DigestUtils.md5DigestAsHex(userDataUpdate.getPassword().getBytes()));//encrypt password

            //update password expiration
            if (currentUser.getPasswordExpiration().before(new Date())) {
                currentUser.setPasswordExpiration(dateCalendar.addDaysCurrentDate(currentUser.getDaysToExpire()));
            }
        }

        return userRepository.save(currentUser);
    }

    /**
     * all usernames list.
     */
    public List<Usernames> findByUsernames() {
        List<Usernames> users = new ArrayList<>();
        userRepository.findAll().forEach(user -> {
            users.add(new Usernames(user.getId(), user.getUsername()));
        });

        return users;
    }

    /**
     * user enable
     *
     * @param user the user
     * @return the user
     */
    public Optional<User> statusUser(User user) {
        user.setEnabled(!user.getEnabled());
        user.setState(user.getEnabled() ? User.ACTIVE : User.INACTIVE);
        return userRepository.save(user);
    }

    /**
     * all users list.
     *
     * @return the list
     */
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    /**
     * find user by id.
     *
     * @param id the id
     * @return the user
     */
    public Optional<User> findByIdUser(Integer id) {
        return userRepository.findById(id);
    }
}