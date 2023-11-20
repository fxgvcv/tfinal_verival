package com.ages.informativoparaimigrantes.dto;

import com.ages.informativoparaimigrantes.enums.UserType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class AuthenticationResponseDTO {
    private String token;
    private UserDTO user;

    @Getter
    @AllArgsConstructor
    @Builder
    public static class UserDTO {
        private String email;
        private UserType type;
    }
}
