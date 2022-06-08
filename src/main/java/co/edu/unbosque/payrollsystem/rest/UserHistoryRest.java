package co.edu.unbosque.payrollsystem.rest;

import co.edu.unbosque.payrollsystem.component.DateCalendarComponent;
import co.edu.unbosque.payrollsystem.dto.ReplyMessage;
import co.edu.unbosque.payrollsystem.model.User;
import co.edu.unbosque.payrollsystem.model.UserHistory;
import co.edu.unbosque.payrollsystem.service.UserHistoryServiceImpl;
import co.edu.unbosque.payrollsystem.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import co.edu.unbosque.payrollsystem.model.Record;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController(value = "UserHistoryRest")
@RequestMapping("/record/api/v1")
public class UserHistoryRest {

    @Autowired
    private UserHistoryServiceImpl userHistoryServiceImpl;

    @Autowired
    private UserServiceImpl userServiceImpl;

    @Autowired
    private DateCalendarComponent dateCalendarComponent;

    @GetMapping("/all")
    public ResponseEntity<?> getAllRecord(final Principal userLogin, final HttpServletRequest request) {
        ResponseEntity<?> response;
        try {
            List<UserHistory> recordUserList = userHistoryServiceImpl.findAll();
            userHistoryServiceImpl.save(userLogin.getName(), new Record(Record.GET, "history of all users"), null, request);//Save record
            response = recordUserList.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(recordUserList);
        } catch (Exception e) {
            response = new ResponseEntity<>(ReplyMessage.ERROR_505, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    @GetMapping("/date/{beginning}/{finalDate}")
    public ResponseEntity<?> getAllRecordByDate(final @PathVariable("beginning") String beginning, final @PathVariable("finalDate") String finalDate, final Principal userLogin, final HttpServletRequest request) {
        ResponseEntity<?> response;
        try {
            Date date1 = dateCalendarComponent.dateFormat("yy-MM-dd HH:mm:ss", beginning);
            Date date2 = dateCalendarComponent.dateFormat("yy-MM-dd HH:mm:ss", finalDate);

            if (date1.after(date2) || !date2.before(dateCalendarComponent.date())) {
                response = new ResponseEntity<>(ReplyMessage.BEGINNING_BEFORE_FINAL, HttpStatus.BAD_REQUEST);
            } else {
                List<UserHistory> recordUserList = userHistoryServiceImpl.findAllDate(date1, date2);
                userHistoryServiceImpl.save(userLogin.getName(), new Record(Record.GET, "history of all users filtered by date"),
                        "filtered by dates from " + beginning + " to " + finalDate, request);//Save record
                response = recordUserList.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(recordUserList);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(ReplyMessage.ERROR_505, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    @GetMapping("/date-and-user/{beginning}/{finalDate}/{id}")
    public ResponseEntity<?> getAllRecordByDateAndUser(final @PathVariable("beginning") String beginning, final @PathVariable("finalDate") String finalDate, final @PathVariable("id") Integer id, final Principal userLogin, final HttpServletRequest request) {
        ResponseEntity<?> response;
        try {
            if (beginning == null || finalDate == null || id == null || beginning.isEmpty() || finalDate.isEmpty()) {
                response = new ResponseEntity<>(ReplyMessage.INVALID_DATA, HttpStatus.BAD_REQUEST);
            } else {
                Optional<User> user = userServiceImpl.findByIdUser(id);
                if (user.isEmpty()) {
                    response = new ResponseEntity<>(ReplyMessage.USER_NOT_FOUND, HttpStatus.BAD_REQUEST);
                } else {
                    Date date1 = dateCalendarComponent.dateFormat("yy-MM-dd HH:mm:ss", beginning);
                    Date date2 = dateCalendarComponent.dateFormat("yy-MM-dd HH:mm:ss", finalDate);

                    if (date1.after(date2) || !date2.before(dateCalendarComponent.date())) {
                        response = new ResponseEntity<>(ReplyMessage.BEGINNING_BEFORE_FINAL, HttpStatus.BAD_REQUEST);
                    } else {
                        List<UserHistory> recordUserList = userHistoryServiceImpl.findAllDateAndUserId(date1, date2, id);

                        if (recordUserList.isEmpty()) {
                            response = ResponseEntity.noContent().build();
                        } else {
                            if (!userLogin.getName().equals(user.get().getUsername())) {
                                userHistoryServiceImpl.save(userLogin.getName(), new Record(Record.GET, "history of all users filtered by date and user"),
                                        "filtered by dates from " + beginning + " to " + finalDate + " and the user " + user.get().getUsername(), request);//Save record
                            }
                            return ResponseEntity.ok(recordUserList);
                        }
                    }
                }
            }
        } catch (Exception e) {
            response = new ResponseEntity<>(ReplyMessage.ERROR_505, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findAllByUserId(final @PathVariable("id") Integer id, final Principal userLogin, final HttpServletRequest request) {
        ResponseEntity<?> response;
        try {
            List<UserHistory> recordUserList = userHistoryServiceImpl.findAllByUserId(id);
            if (recordUserList.isEmpty()) {
                response = new ResponseEntity<>(recordUserList, HttpStatus.NO_CONTENT);
            } else {
                if (!recordUserList.get(0).getUser().getUsername().equals(userLogin.getName())) {
                    userHistoryServiceImpl.save(userLogin.getName(), new Record(Record.GET, "get history by user"),
                            recordUserList.get(0).getUser().getUsername() + " history was obtained", request);//Save record
                }
                response = new ResponseEntity<>(recordUserList, HttpStatus.OK);
            }
        } catch (Exception e) {
            response = new ResponseEntity<>(ReplyMessage.ERROR_505, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }
}


