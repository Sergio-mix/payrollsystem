package co.edu.unbosque.payrollsystem.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;


@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity(name = "TypeOfRequest")
@Table(name = "type_of_request")
public class TypeOfRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 234332322L;
    @Id
    private Integer id;

    @Column(name = "name", nullable = false, length = 8, unique = true)
    private String name;

    @Column(name = "description", nullable = false, length = 50)
    private String description;

    @JsonIgnore
    @Column(name = "state", nullable = false, length = 1)
    private String state = "A";

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        TypeOfRequest that = (TypeOfRequest) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
