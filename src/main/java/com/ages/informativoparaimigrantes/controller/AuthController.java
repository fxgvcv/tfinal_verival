package com.ages.informativoparaimigrantes.controller;

import com.ages.informativoparaimigrantes.domain.UserData;
import com.ages.informativoparaimigrantes.dto.AuthenticationRequestDTO;
import com.ages.informativoparaimigrantes.dto.AuthenticationResponseDTO;
import com.ages.informativoparaimigrantes.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthenticationRequestDTO authenticationDTO) {
        Authentication auth = this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationDTO.getEmail(), authenticationDTO.getPassword()));

        String token = tokenService.generateToken((UserData) auth.getPrincipal());

        AuthenticationResponseDTO response = AuthenticationResponseDTO.builder()
                .token(token)
                .user(AuthenticationResponseDTO.UserDTO.builder()
                        .email(((UserData) auth.getPrincipal()).getEmail())
                        .type(((UserData) auth.getPrincipal()).getType())
                        .build())
                .build();

        return ResponseEntity.ok(response);
    }
}