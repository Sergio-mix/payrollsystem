package co.edu.unbosque.payrollsystem.repository.jpa;

import co.edu.unbosque.payrollsystem.model.UserHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface UserHistoryJpa extends JpaRepository<UserHistory, Integer> {

    List<UserHistory> findUserHistoryEntitiesByOrderByDateDesc();

    List<UserHistory> findAllByUserIdOrderByDateDesc(Integer userId);

    List<UserHistory> findAllByDateAfterAndDateBeforeOrderByDateDesc(Date beginning, Date finalDate);

    List<UserHistory> findAllByDateAfterAndDateBeforeAndUserIdOrderByDateDesc(Date date1, Date date2, Integer user_id);

}
