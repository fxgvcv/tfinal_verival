package com.ages.informativoparaimigrantes.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    SecurityFilter securityFilter;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        return httpSecurity
                .csrf(csrf -> csrf.disable())
                .headers().frameOptions().disable().and()
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .antMatchers(HttpMethod.POST, "/programs/**").hasAnyRole("ADMIN", "INSTITUTION")
                        .antMatchers(HttpMethod.PATCH, "/programs/**/status").hasRole("ADMIN")
                        .antMatchers(HttpMethod.POST, "/articles/**").hasRole("ADMIN")
                        .antMatchers(HttpMethod.POST, "/immigrants").permitAll()
                        .antMatchers(HttpMethod.POST, "/institutions").permitAll()
                        .antMatchers(HttpMethod.PATCH, "/institutions/**/status").hasRole("ADMIN")
                        .antMatchers(HttpMethod.PATCH, "/institutions/**/status").hasRole("ADMIN")
                        .antMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .antMatchers("/swagger-ui/**").permitAll()
                        .antMatchers("/swagger-resources/**").permitAll()
                        //.anyRequest().authenticated()
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
