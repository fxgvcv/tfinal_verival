package com.ages.informativoparaimigrantes.repository;

import com.ages.informativoparaimigrantes.domain.Program;
import com.ages.informativoparaimigrantes.enums.ProgramType;
import com.ages.informativoparaimigrantes.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface IProgramRepository extends JpaRepository<Program, Long> {

    @Query("SELECT p FROM Program p" +
            " WHERE (:institutionEmail IS NULL OR p.institutionEmail = :institutionEmail) " +
            " AND (:status IS NULL OR p.status = :status)" +
            " AND (:programType IS NULL OR p.programType = :programType)" +
            " AND (:language IS NULL OR LOWER(p.language) = LOWER(:language))" +
            " AND (:location IS NULL OR LOWER(p.location) = LOWER(:location)) " +
            " AND (:openSubscription IS NULL OR " +
            "   (:openSubscription = true AND p.enrollmentEndDate >= CURRENT_DATE AND p.enrollmentInitialDate <= CURRENT_DATE) OR " +
            "   (:openSubscription = false AND (p.enrollmentEndDate <= CURRENT_DATE OR p.enrollmentInitialDate >= CURRENT_DATE)))")
    List<Program> findWithFilters(@Param("status") Status status,
                                  @Param("institutionEmail") String institutionEmail,
                                  @Param("programType") ProgramType programType,
                                  @Param("language") String language,
                                  @Param("location") String location,
                                  @Param("openSubscription") Boolean openSubscription);


    @Query( " SELECT p.programType, COUNT(p.programType)" +
            " FROM Program p " +
            " WHERE p.status = 'APPROVED'" +
            " GROUP BY p.programType")
    List<Object[]> getCategoriesWithWithProgramCount();

    List<Program> findProgramByInstitutionEmail(String email);

    long countByStatus(Status status);
}
