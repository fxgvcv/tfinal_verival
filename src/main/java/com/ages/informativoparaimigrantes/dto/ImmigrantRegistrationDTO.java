package com.ages.informativoparaimigrantes.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ImmigrantRegistrationDTO {

    private String email;

    private String name;

    private String countryOfOrigin;

    private String password;
}
