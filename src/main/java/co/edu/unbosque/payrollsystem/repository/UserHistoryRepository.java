package co.edu.unbosque.payrollsystem.repository;

import co.edu.unbosque.payrollsystem.model.UserHistory;
import co.edu.unbosque.payrollsystem.repository.jpa.UserHistoryJpa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository(value = "UserHistoryRepository")
public class UserHistoryRepository {

    @Autowired
    private UserHistoryJpa recordUserJpa;

    /**
     *
     * @param userHistory UserHistory
     */
    public void save(UserHistory userHistory) {
        recordUserJpa.save(userHistory);
    }

    public List<UserHistory> findAll() {
        return recordUserJpa.findUserHistoryEntitiesByOrderByDateDesc();
    }

    public List<UserHistory> findAllByUserId(Integer userId) {
        return recordUserJpa.findAllByUserIdOrderByDateDesc(userId);
    }

    public List<UserHistory> findAllDate(Date beginning, Date finalDate) {
        return recordUserJpa.findAllByDateAfterAndDateBeforeOrderByDateDesc(beginning, finalDate);
    }

    public List<UserHistory> findAllDateAndUserId(Date beginning, Date finalDate, Integer userId) {
        return recordUserJpa.findAllByDateAfterAndDateBeforeAndUserIdOrderByDateDesc(beginning, finalDate, userId);
    }
}
