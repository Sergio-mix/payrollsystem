package co.edu.unbosque.payrollsystem.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name = "auth_user")
@Entity(name = "User")
public class User implements Serializable, UserDetails {

    public static final String ACTIVE = "A";

    public static final String INACTIVE = "I";

    @Serial
    private static final long serialVersionUID = 132425L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name", nullable = false, length = 25)
    private String names;

    @Column(name = "last_name", nullable = false, length = 25)
    private String lastNames;

    @ManyToOne
    @JoinColumn(name = "fk_country_code", nullable = false, referencedColumnName = "id")
    private CountryCode countryCode;

    @Column(name = "cell_phone", nullable = false)
    private String cellPhone;

    @ManyToOne
    @JoinColumn(name = "fk_type_document", nullable = false, referencedColumnName = "id")
    private TypeDocument typeDocument;

    @Column(name = "document_number", nullable = false, length = 25)
    private String documentNumber;
    @Column(name = "username", unique = true, nullable = false, length = 8)
    private String username;

    @Column(name = "email", unique = true, nullable = false, length = 50)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "days_to_expire", nullable = false)
    private Integer daysToExpire = 30;

    @Column(name = "password_expiration", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date passwordExpiration;

    @JsonIgnore
    @Column(name = "key_attempts", nullable = false)
    private Integer keyAttempts = 0;

    @JsonIgnore
    @Column(name = "recovery_code", length = 12)
    private String recoveryCode;

    @JsonIgnore
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "recovery_code_expiration")
    private Date recoveryCodeExpiration;
    @Column(name = "enabled", nullable = false)
    private Boolean enabled = true;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "user_authority", joinColumns = @JoinColumn(referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(referencedColumnName = "id"))
    private List<Authority> authorities;

    @JsonIgnore
    @Column(name = "state", nullable = false, length = 1)
    private String state = ACTIVE;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return enabled;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return enabled;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return enabled;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;
        return id != null && Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}