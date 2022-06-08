package co.edu.unbosque.payrollsystem.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity(name = "Contributor")
@Table(name = "Contributor")
public class Contributor implements Serializable {
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

    @Column(name = "name_contributor", nullable = false, length = 100)
    private String nameOfTheContributor;

    @JsonIgnore
    @Column(name = "state", nullable = false, length = 1)
    private String state = ACTIVE;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Contributor that = (Contributor) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
