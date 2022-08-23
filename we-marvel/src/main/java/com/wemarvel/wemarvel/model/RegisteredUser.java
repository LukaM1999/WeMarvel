package com.wemarvel.wemarvel.model;

import com.fasterxml.jackson.annotation.*;
import com.wemarvel.wemarvel.model.enums.Gender;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCrypt;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.*;


@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "username")
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class RegisteredUser implements UserDetails {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "userIdGen", sequenceName = "userIdSeq", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userIdGen")
    @Getter
    private Long id;

    @Getter
    @Setter
    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String username;

    @Getter
    @Setter
    private String location;

    @Getter
    @Setter
    @JsonFormat(pattern = "dd.MM.yyyy.")
    private LocalDate birthday;

    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Getter
    @Setter
    private String imageUrl;

    @Column(name = "enabled")
    private boolean enabled;

    @Getter
    @Setter
    @Column(columnDefinition = "Boolean default false")
    private boolean deleted;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="roleName")
    private Role role;

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<Role>(List.of(this.role));
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public RegisteredUser() {
        super();
    }

    public RegisteredUser(String email, String username) {
        this.email = email;
        this.username = username;
        this.role = new Role("USER");
        this.enabled = true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
}
