package co.edu.unbosque.payrollsystem.rest;

import co.edu.unbosque.payrollsystem.dto.ReplyMessage;
import co.edu.unbosque.payrollsystem.dto.Usernames;
import co.edu.unbosque.payrollsystem.exception.UserException;
import co.edu.unbosque.payrollsystem.model.User;
import co.edu.unbosque.payrollsystem.response.UserEnable;
import co.edu.unbosque.payrollsystem.service.EmailSenderServiceImpl;
import co.edu.unbosque.payrollsystem.service.UserHistoryServiceImpl;
import co.edu.unbosque.payrollsystem.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import co.edu.unbosque.payrollsystem.model.Record;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController("UserRest")
@RequestMapping(value = "/user/api/v1")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserRest {

    @Autowired
    private UserServiceImpl userServiceImpl;

    @Autowired
    private EmailSenderServiceImpl mailService;

    @Autowired
    private UserHistoryServiceImpl userHistoryServiceImpl;

    @PostMapping(value = "/save")
    @Transactional
    public ResponseEntity<?> saveUser(@RequestBody User user, Principal userLogin, HttpServletRequest request) {
        ResponseEntity<?> response;
        try {
            String password = user.getPassword();//password
            Optional<User> us = userServiceImpl.saveUser(user);//save user

            if (us.isEmpty()) {
                response = new ResponseEntity<>("Failed to create user", HttpStatus.BAD_REQUEST);
            } else {
                userHistoryServiceImpl.save(userLogin.getName(), new Record(Record.POST, "register user"),
                        "added to the user " + us.get().getUsername(), request);//Save record
                mailService.sendMailCredentials(user.getEmail(), password, user.getUsername(), user.getNames(), request);//send mail
                response = new ResponseEntity<>("Successfully registered user", HttpStatus.CREATED);
            }
        } catch (UserException e) {
            response = new ResponseEntity<>(e.getErrors(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            response = new ResponseEntity<>(ReplyMessage.ERROR_505, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    @PutMapping(value = "/update")
    @Transactional
    public ResponseEntity<?> updateUser(@RequestBody User user, Principal userLogin, HttpServletRequest request) {
        ResponseEntity<?> response;
        try {
            Optional<User> currentUser = userServiceImpl.findByIdUser(user.getId());

            if (currentUser.isEmpty()) {
                response = new ResponseEntity<>(ReplyMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
            } else {
                Optional<User> us = userServiceImpl.updateUser(user, currentUser.get());//Update user

                if (us.isEmpty()) {
                    response = new ResponseEntity<>(ReplyMessage.FAILED_UPDATE_USER, HttpStatus.BAD_REQUEST);
                } else {
                    userHistoryServiceImpl.save(userLogin.getName(), new Record(Record.PUT, "update user"),
                            "updated the user " + us.get().getUsername(), request);//Save record
                    response = new ResponseEntity<>(ReplyMessage.UPDATE_USER, HttpStatus.CREATED);
                }
            }
        } catch (UserException e) {
            response = new ResponseEntity<>(e.getErrors(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            response = new ResponseEntity<>(ReplyMessage.ERROR_505, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    @GetMapping(value = "/all")
    public ResponseEntity<?> getAllUsers(final Principal userLogin, final HttpServletRequest request) {
        ResponseEntity<?> response;
        try {
            userHistoryServiceImpl.save(userLogin.getName(), new Record(Record.GET, "list user"), null, request);//Save record
            List<User> users = userServiceImpl.findAllUsers();
            response = !users.isEmpty() ? new ResponseEntity<>(users, HttpStatus.OK) : new ResponseEntity<>(users, HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            response = new ResponseEntity<>(ReplyMessage.ERROR_505, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    @GetMapping(value = "/usernames")
    public ResponseEntity<?> getAllUsersName() {
        ResponseEntity<?> response;
        try {
            List<Usernames> users = userServiceImpl.findByUsernames();
            response = !users.isEmpty() ? new ResponseEntity<>(users, HttpStatus.OK) : new ResponseEntity<>(users, HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            response = new ResponseEntity<>(ReplyMessage.ERROR_505, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getUserId(@PathVariable("id") Integer id) {
        ResponseEntity<?> response;
        try {
            Optional<User> user = userServiceImpl.findByIdUser(id);
            if (user.isPresent()) {
                response = new ResponseEntity<>(user, HttpStatus.OK);
            } else {
                response = new ResponseEntity<>(ReplyMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            response = new ResponseEntity<>(ReplyMessage.ERROR_505, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    @PutMapping(value = "/enable")
    @Transactional
    public ResponseEntity<?> enableUser(@RequestBody Integer userId, Principal userLogin, HttpServletRequest request) {
        ResponseEntity<?> response;
        try {
            if (userId == null) {
                response = new ResponseEntity<>(ReplyMessage.INVALID_DATA, HttpStatus.NOT_FOUND);
            } else {
                Optional<User> user = userServiceImpl.findByIdUser(userId);

                if (user.isEmpty()) {
                    response = new ResponseEntity<>(ReplyMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
                } else {
                    if (userLogin.getName().equals(user.get().getUsername())) {
                        response = new ResponseEntity<>(ReplyMessage.NOT_ENABLE, HttpStatus.BAD_REQUEST);
                    } else {
                        if (userServiceImpl.statusUser(user.get()).isEmpty()) {
                            response = new ResponseEntity<>(ReplyMessage.USER_NOT_FOUND, HttpStatus.BAD_REQUEST);
                        } else {
                            String action = user.get().getEnabled() ? "enabled" : "disabled";
                            userHistoryServiceImpl.save(userLogin.getName(), new Record(Record.PUT, "user status"),
                                    "the " + user.get().getUsername() + " user was " + action, request);//Save record
                            response = new ResponseEntity<>(new UserEnable("User " + action, user.get().getEnabled()), HttpStatus.OK);
                        }
                    }
                }
            }
        } catch (Exception e) {
            response = new ResponseEntity<>(ReplyMessage.ERROR_505, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }
}
