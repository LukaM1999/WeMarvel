package com.wemarvel.wemarvel.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCrypt;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.*;


@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "username")
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class RegisteredUser implements UserDetails {

    private static final long serialVersionUID = 1L;

    @Id
    @Column
    @Getter
    @Setter
    private String email;
    @Column(unique = true)
    private String username;
    @Column
    @Getter
    @Setter
    private String firstName;
    @Column
    @Getter
    @Setter
    private String lastName;
    @Column
    @Getter
    @Setter
    private String address;
    @Column
    @Getter
    @Setter
    private String city;
    @Column
    @Getter
    @Setter
    private String country;
    @Column
    @Getter
    @Setter
    private String phone;

    @Column(name = "enabled")
    private boolean enabled;

    @Getter
    @Setter
    @Column(columnDefinition = "Boolean default false")
    private boolean deleted;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="roleName")
    private Role role;

//    @OneToMany(mappedBy = "companyOwner", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//    @Getter
//    @Setter
//    @JsonIgnore
//    private Set<Company> companies = new LinkedHashSet<>();

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
