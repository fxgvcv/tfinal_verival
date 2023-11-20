package com.ages.informativoparaimigrantes.domain;

import com.ages.informativoparaimigrantes.dto.ImmigrantRegistrationDTO;
import com.ages.informativoparaimigrantes.dto.InstitutionRegistrationDTO;
import com.ages.informativoparaimigrantes.enums.UserType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class UserData implements UserDetails {

    @Id
    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private UserType type;

    public UserData(String email, String password, String type){
        this.email = email;
        this.password = password;
        this.type = UserType.valueOf(type);
    }

    public UserData(InstitutionRegistrationDTO dto){
        this.type = UserType.INSTITUTION;
        this.email = dto.getEmail();
        this.password = dto.getPassword();
    }

    public UserData(ImmigrantRegistrationDTO dto){
        this.type = UserType.IMMIGRANT;
        this.email = dto.getEmail();
        this.password = dto.getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.type == UserType.ADMIN) return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"));
        else if (this.type == UserType.INSTITUTION) return List.of(new SimpleGrantedAuthority("ROLE_INSTITUTION"));
        else return List.of(new SimpleGrantedAuthority("ROLE_IMMIGRANT"));
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
}
