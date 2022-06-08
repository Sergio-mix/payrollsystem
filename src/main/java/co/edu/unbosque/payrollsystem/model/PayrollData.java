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
@Table(name = "payroll_data")
@Entity(name = "PayrollData")
public class PayrollData implements Serializable {
    public static final String ACTIVE = "A";

    public static final String INACTIVE = "I";

    @Serial
    private static final long serialVersionUID = 233214L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "fk_type_document", nullable = false, referencedColumnName = "id")
    private TypeDocument typeDocument;

    @Column(name = "document_number", nullable = false, length = 25)
    private String documentNumber;

    @Column(name = "name_contributor", nullable = false, length = 80)
    private String nameOfTheContributor;

    @Column(name = "date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date date;

    @Column(name = "salary", nullable = false)
    private Integer salary = 0;

    @Column(name = "worked_days", nullable = false)
    private Integer workedDays = 0;

    @Column(name = "days_of_disability", nullable = false)
    private Integer daysOfDisability = 0;

    @Column(name = "leave_days", nullable = false)
    private Integer leaveDays = 0;

    @Column(name = "total_days", nullable = false)
    private Integer totalDays = 0;

    @Column(name = "date_of_admission", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date dateOfAdmission;

    @JsonIgnore
    @Column(name = "state", nullable = false, length = 1)
    private String state = ACTIVE;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        PayrollData that = (PayrollData) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
