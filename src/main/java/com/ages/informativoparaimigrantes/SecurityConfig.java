package com.ages.informativoparaimigrantes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    SecurityFilter securityFilter;

    @Autowired
    TokenService tokenService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        return httpSecurity
                .csrf(csrf -> csrf.disable())
                .headers().frameOptions().disable().and()
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .antMatchers(HttpMethod.POST, "/programs/**").permitAll()
                        .antMatchers(HttpMethod.GET, "/programs/**").permitAll()  // Defina para GET também
                        .antMatchers(HttpMethod.PATCH, "/programs/**/status").permitAll()
                        .antMatchers(HttpMethod.POST, "/articles/**").permitAll()
                        .antMatchers(HttpMethod.POST, "/immigrants").permitAll()
                        .antMatchers(HttpMethod.POST, "/institutions").permitAll()
                        .antMatchers(HttpMethod.PATCH, "/institutions/**/status").hasRole("ADMIN")
                        .antMatchers(HttpMethod.PATCH, "/institutions/**/status").hasRole("ADMIN")
                        .antMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .antMatchers("/swagger-ui/**").permitAll()
                        .antMatchers("/swagger-resources/**").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }

    public PasswordEncoder passwordEncoder(){
        return new HashUtils();
    }
}