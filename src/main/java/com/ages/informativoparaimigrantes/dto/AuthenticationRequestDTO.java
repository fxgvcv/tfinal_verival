package com.ages.informativoparaimigrantes.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthenticationRequestDTO {
    private String email;

    private String password;
}
