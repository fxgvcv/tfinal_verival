package com.ages.informativoparaimigrantes.repository;

import com.ages.informativoparaimigrantes.domain.Immigrant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IImmigrantRepository extends JpaRepository<Immigrant, String> {

    @Query(value = "SELECT i FROM Immigrant i")
    List<Immigrant> getAll();

}