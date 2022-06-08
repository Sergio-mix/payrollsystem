package co.edu.unbosque.payrollsystem.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity(name = "CountryCode")
@Table(name = "country_code")
public class CountryCode implements Serializable {

    @Serial
    private static final long serialVersionUID = 1123432L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "code", nullable = false, unique = true)
    private Integer code;

    @Column(name = "country", nullable = false, unique = true)
    private String country;

    @JsonIgnore
    @Column(name = "state", nullable = false, length = 1)
    private String state = "A";

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CountryCode that = (CountryCode) o;
        return Objects.equals(id, that.id) && Objects.equals(code, that.code) && Objects.equals(country, that.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code, country);
    }
}
