package ua.dev.food.fast.service.models;

import java.util.Collection;
import java.util.List;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table("users")
public class User implements UserDetails {

    @Id
    private Long id;

    @Column("first_name")
    private String firstname;

    @Column("last_name")
    private String lastname;

    @Column
    private String email;

    @Column
    private String password;

    @Column("role")
    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String toString() {
        return "User(id=" + id + ", firstname=" + firstname + ", lastname=" + lastname + ", email=" + email + ", role=" + role + ")";
    }
}
