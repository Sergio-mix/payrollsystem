package co.edu.unbosque.payrollsystem.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity(name = "Authority")
@Table(name = "auth_authority")
public class Authority implements Serializable, GrantedAuthority {

    @Serial
    private static final long serialVersionUID = 12342340L;
    public final static String ADMIN = "ADMIN";
    public final static String USER = "USER";
    public final static String COORDINAROR = "COORDINAROR";
    public static final String ACTIVE = "A";

    public static final String INACTIVE = "I";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "role_code", length = 50, nullable = false, unique = true)
    private String roleCode;

    @Column(name = "description", length = 100, nullable = false)
    private String description;

    @JsonIgnore
    @Column(name = "state", nullable = false, length = 1)
    private String state = ACTIVE;

    @Override
    public String getAuthority() {
        return roleCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Authority authority = (Authority) o;
        return id != null && Objects.equals(id, authority.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
