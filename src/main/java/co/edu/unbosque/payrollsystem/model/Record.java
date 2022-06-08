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
@Entity(name = "Record")
@Table(name = "record")
public class Record implements Serializable {

    @Serial
    private static final long serialVersionUID = 1324342L;
    public final static String POST = "POST";
    public final static String GET = "GET";
    public final static String PUT = "PUT";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "type_of_request", nullable = false, length = 15)
    private String typeOfRequest;

    @Column(name = "name", nullable = false, length = 80, unique = true)
    private String name;

    @JsonIgnore
    @Column(name = "state", nullable = false, length = 1)
    private String state = "A";

    public Record(String typeOfRequest, String name) {
        this.typeOfRequest = typeOfRequest;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Record record = (Record) o;
        return id != null && Objects.equals(id, record.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
