package ex.han.hanbackend.domain.user;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * User
 */
@Data
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @Column(name="user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String password;
    private String name;
    private String socialId;
    private String provider;

    @Enumerated(EnumType.STRING)
    private Set<UserRole> roles = new HashSet<>();

    public User addRole(UserRole role) {
        this.roles.add(role);
        return this;
    }
}
