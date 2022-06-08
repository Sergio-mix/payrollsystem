package co.edu.unbosque.payrollsystem.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name = "payroll_dynamic")
@Entity(name = "PayrollDynamic")
public class PayrollDynamic implements Serializable {
    public static final String ACTIVE = "A";

    public static final String INACTIVE = "I";

    @Serial
    private static final long serialVersionUID = 4333214L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @Column(name = "salary", nullable = false)
    private Integer salary = 0;

    @Column(name = "support_for", nullable = false)
    private Integer supportFor = 0;

    @Column(name = "overtime_daytime", nullable = false)
    private Integer overtimeDaytime = 0;

    @Column(name = "overtime", nullable = false)
    private Integer overtime = 0;

    @Column(name = "commissions", nullable = false)
    private Integer commissions = 0;

    @Column(name = "holidays", nullable = false)
    private Integer holidays = 0;

    @Column(name = "Mandatory_vacation", nullable = false)
    private Integer MandatoryVacation = 0;

    @Column(name = "input", nullable = false)
    private Integer input = 0;

    @Column(name = "Retirement_bonus", nullable = false)
    private Integer RetirementBonus = 0;

    @Column(name = "compensation", nullable = false)
    private Integer compensation = 0;

    @Column(name = "inability", nullable = false)
    private Integer inability = 0;

    @JsonIgnore
    @Column(name = "state", nullable = false, length = 1)
    private String state = ACTIVE;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        PayrollDynamic that = (PayrollDynamic) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
