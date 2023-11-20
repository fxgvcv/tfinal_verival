package com.ages.informativoparaimigrantes.repository;

import com.ages.informativoparaimigrantes.domain.Institution;
import com.ages.informativoparaimigrantes.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface IInstitutionRepository extends JpaRepository<Institution, String> {

    @Query(value = "SELECT i FROM Institution i")
    List<Institution> getAll();

    @Query(value = "SELECT i FROM Institution i WHERE i.status = :status")
    List<Institution> getInstitutionsByStatus(Status status);

    long countByStatus(Status status);
}
