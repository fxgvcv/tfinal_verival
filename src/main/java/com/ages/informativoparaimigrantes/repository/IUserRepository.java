package com.ages.informativoparaimigrantes.repository;

import com.ages.informativoparaimigrantes.domain.UserData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserRepository extends JpaRepository<UserData, String> {
    UserDetails findByEmail(String email);
}

