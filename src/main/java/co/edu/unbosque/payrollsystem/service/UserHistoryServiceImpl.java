package co.edu.unbosque.payrollsystem.service;

import co.edu.unbosque.payrollsystem.component.UrlComponent;
import co.edu.unbosque.payrollsystem.model.Record;
import co.edu.unbosque.payrollsystem.model.UserHistory;
import co.edu.unbosque.payrollsystem.repository.RecordRepository;
import co.edu.unbosque.payrollsystem.repository.UserHistoryRepository;
import co.edu.unbosque.payrollsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service(value = "UserHistoryServiceImpl")
public class UserHistoryServiceImpl {

    @Autowired
    private UserHistoryRepository userHistoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RecordRepository recordRepository;

    @Autowired
    private UrlComponent urlComponent;

    public void save(final String user, final Record record, final String description, final HttpServletRequest request) {
        Optional<Record> recordOptional = recordRepository.getByName(record.getName());

        if (recordOptional.isEmpty()) {
            recordOptional = recordRepository.save(new Record(record.getTypeOfRequest(), record.getName()));
        }

        recordOptional.ifPresent(value -> userHistoryRepository.save(new UserHistory(
                userRepository.findByUsername(user).get(), value, description, new Date(),
                urlComponent.getIPAddress(request))));
    }

    public List<UserHistory> findAll() {
        return userHistoryRepository.findAll();
    }

    public List<UserHistory> findAllByUserId(Integer id) {
        return userHistoryRepository.findAllByUserId(id);
    }

    public List<UserHistory> findAllDate(Date beginning, Date finalDate) {
        return userHistoryRepository.findAllDate(beginning, finalDate);
    }

    public List<UserHistory> findAllDateAndUserId(Date beginning, Date finalDate, Integer userId) {
        return userHistoryRepository.findAllDateAndUserId(beginning, finalDate, userId);
    }
}
