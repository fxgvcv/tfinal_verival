package com.ages.informativoparaimigrantes.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ImmigrantUserDTO {
    String countryOfOrigin;
    String name;
    String passwordNew;
    String passwordOld;
    String email;
}
