package co.edu.unbosque.payrollsystem.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity(name = "TypeDocument")
@Table(name = "type_document")
public class TypeDocument implements Serializable {

    @Serial
    private static final long serialVersionUID = 132333L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
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
        if (o == null || getClass() != o.getClass()) return false;
        TypeDocument that = (TypeDocument) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
