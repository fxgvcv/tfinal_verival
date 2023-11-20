package com.ages.informativoparaimigrantes.service;

import com.ages.informativoparaimigrantes.domain.Institution;
import com.ages.informativoparaimigrantes.dto.InstitutionRegistrationDTO;
import com.ages.informativoparaimigrantes.enums.Status;
import com.ages.informativoparaimigrantes.repository.IInstitutionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InstitutionServiceImpl implements IInstitutionService {
    @Autowired
    private IInstitutionRepository repository;

    public void save(InstitutionRegistrationDTO institutionRegistrationDTO){
        repository.save(new Institution(institutionRegistrationDTO));
    }

    public void save(Institution institution) {
        repository.save(institution);
    }

    public Optional<Institution> getByEmail(String email) {
        return repository.findById(email);
    }

    public void delete(String email){
        repository.deleteById(email);
    }

    public List<Institution> getAll(){
        return repository.getAll();
    }

    public List<Institution> getByStatus(String status){
        return repository.getInstitutionsByStatus(Status.valueOf(status));
    }

    public long getCountByStatus(Status status) {
        return repository.countByStatus(status);
    }

    public Institution updateStatus(String email, Status status, String feedback) {
        Optional<Institution> institutionOpt = getByEmail(email);
        if (institutionOpt.isPresent()) {
            Institution institution = institutionOpt.get();
            institution.setStatus(status);
            institution.setFeedback(feedback);
            save(institution);
            return institution;
        } else {
            throw new IllegalArgumentException("Institution not found.");
        }
    }
}
