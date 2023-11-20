package com.ages.informativoparaimigrantes.domain;

import com.ages.informativoparaimigrantes.dto.ImmigrantRegistrationDTO;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Immigrant {

    @Column(name = "email")
    @Id
    private String email;

    @Column(name = "name")
    @NonNull
    private String name;

    @Column(name = "countryOfOrigin", nullable = true)
    private String countryOfOrigin;

    public Immigrant(ImmigrantRegistrationDTO dto){
        this.email = dto.getEmail();
        this.name = dto.getName();
        this.countryOfOrigin = dto.getCountryOfOrigin();
    }

}
