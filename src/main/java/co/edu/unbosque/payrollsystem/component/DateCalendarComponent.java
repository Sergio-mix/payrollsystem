package co.edu.unbosque.payrollsystem.component;

import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Component
public class DateCalendarComponent {

    /**
     * add minutes to current date
     *
     * @param minutes minutes to add
     * @return date with minutes added
     */
    public Date addMinutesCurrentDate(final Integer minutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MINUTE, minutes);

        return calendar.getTime();
    }

    /**
     * add days to current date.
     *
     * @param days number of days
     * @return date + days
     */
    public Date addDaysCurrentDate(final Integer days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_YEAR, days);

        return calendar.getTime();
    }

    /**
     * Verify if date is after current date.
     *
     * @param date date to verify
     * @return true if date is after current date
     */
    public Boolean isDateExpired(final Date date) {
        return date == null || date.before(new Date());
    }

    /**
     * date.
     *
     * @return date
     */
    public Date getDate() {
        return new Date();
    }

    /**
     * Date format.
     *
     * @param format format
     * @param date   date string
     * @return date format
     * @throws ParseException exception
     */
    public Date dateFormat(final String format, final String date) throws ParseException {
        return new SimpleDateFormat(format).parse(date);
    }

}
