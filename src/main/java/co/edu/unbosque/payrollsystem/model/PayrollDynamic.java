package co.edu.unbosque.payrollsystem.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
    private static final long serialVersionUID = 4424314L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(cascade = CascadeType.ALL)
    @JsonIgnoreProperties({"payrollDynamic", "state"})
    @JoinColumn(name = "fk_payroll_data", nullable = false, referencedColumnName = "id")
    private PayrollData payrollData;

    @Column(name = "minimum_wage", nullable = false)
    private Float minimumWage = 0F;

    @Column(name = "support", nullable = false)
    private Float support = 0F;

    @Column(name = "overtime_hour", nullable = false)
    private Float overtimeHour = 0F;

    @Column(name = "overtime_hour_fa", nullable = false)
    private Float overtimeHourFa = 0f;

    @Column(name = "commissions", nullable = false)
    private Float commissions = 0F;

    @Column(name = "holidays", nullable = false)
    private Float holidays = 0F;

    @Column(name = "required_holiday", nullable = false)
    private Float requiredHoliday = 0F;

    @Column(name = "aj_apor_ins", nullable = false)
    private Float ajAporIns = 0F;

    @Column(name = "withdrawal_bonus", nullable = false)
    private Float withdrawalBonus = 0F;

    @Column(name = "compensation", nullable = false)
    private Float compensation = 0F;

    @Column(name = "inability", nullable = false)
    private Float inability = 0F;

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
