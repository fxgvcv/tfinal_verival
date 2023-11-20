package com.ages.informativoparaimigrantes.service;

import com.ages.informativoparaimigrantes.domain.Institution;
import com.ages.informativoparaimigrantes.enums.Status;

import java.util.List;

public interface IInstitutionService {

    void save(Institution institution);

    void delete(String email);

    List<Institution> getAll();
    Institution updateStatus(String email, Status status, String feedback);
    long getCountByStatus(Status status);
}
