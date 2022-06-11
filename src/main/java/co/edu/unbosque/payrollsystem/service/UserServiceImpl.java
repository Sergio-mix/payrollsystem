package co.edu.unbosque.payrollsystem.service;

import co.edu.unbosque.payrollsystem.component.DateCalendarComponent;
import co.edu.unbosque.payrollsystem.dto.Usernames;
import co.edu.unbosque.payrollsystem.exception.UserException;
import co.edu.unbosque.payrollsystem.model.Authority;
import co.edu.unbosque.payrollsystem.model.User;
import co.edu.unbosque.payrollsystem.service.validation.UserValidationDataServiceImpl;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.transaction.Transactional;
import java.util.*;

/**
 * The type User service impl.
 */
@Data
@Service(value = "UserServiceImpl")
public class UserServiceImpl {

    /**
     * The Date calendar component.
     */
    @Autowired
    private DateCalendarComponent dateCalendar;

    @Autowired
    private UserValidationDataServiceImpl userValidation;

    /**
     * save user.
     *
     * @param user the user
     * @return the user
     */
    @Transactional(rollbackOn = {User.class, Authority.class})
    public Optional<User> saveUser(User user) throws UserException {
        userValidation.validateRegisterUser(user);//validate user
        List<Authority> authorities = (List<Authority>) user.getAuthorities();

        user.setNames(user.getNames().toUpperCase());
        user.setLastNames(user.getLastNames().toUpperCase());
        user.setAuthorities(null);
        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));//encrypt password
        user.setEmail(user.getEmail().toLowerCase());
        user.setPasswordExpiration(dateCalendar.addDaysCurrentDate(user.getDaysToExpire()));

        Optional<User> userSaved = userValidation.getUserRepository().save(user);//save user

        userSaved.get().setAuthorities(authorities);
        return userValidation.getUserRepository().save(userSaved.get());
    }

    /**
     * update user.
     *
     * @param userDataUpdate user data update
     * @param currentUser    user data current
     */
    @Transactional(rollbackOn = User.class)
    public Optional<User> updateUser(User userDataUpdate, User currentUser) throws UserException {
        userValidation.validateUpdateUser(userDataUpdate, currentUser);//validate user
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

        return userValidation.getUserRepository().save(currentUser);
    }

    /**
     * all usernames list.
     */
    public List<Usernames> findByUsernames() {
        List<Usernames> users = new ArrayList<>();
        userValidation.getUserRepository().findAll().forEach(user -> {
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
    @Transactional(rollbackOn = {User.class})
    public Optional<User> statusUser(User user) {
        user.setEnabled(!user.getEnabled());
        user.setState(user.getEnabled() ? User.ACTIVE : User.INACTIVE);
        return userValidation.getUserRepository().save(user);
    }

    /**
     * all users list.
     *
     * @return the list
     */
    public List<User> findAllUsers() {
        return userValidation.getUserRepository().findAll();
    }

    /**
     * find user by id.
     *
     * @param id the id
     * @return the user
     */
    public Optional<User> findByIdUser(Integer id) {
        return userValidation.getUserRepository().findById(id);
    }
}