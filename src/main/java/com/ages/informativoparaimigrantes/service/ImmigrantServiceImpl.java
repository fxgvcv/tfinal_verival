package com.ages.informativoparaimigrantes.service;

import com.ages.informativoparaimigrantes.domain.Immigrant;
import com.ages.informativoparaimigrantes.dto.ImmigrantRegistrationDTO;
import com.ages.informativoparaimigrantes.repository.IImmigrantRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ImmigrantServiceImpl implements IImmigrantService {

    @Autowired
    private IImmigrantRepository repository;

    public void save(ImmigrantRegistrationDTO immigrantRegistrationDTO){
        repository.save(new Immigrant(immigrantRegistrationDTO));
    }

    public void save(Immigrant immigrant){
        repository.save(immigrant);
    }

    public Optional<Immigrant> getByEmail(String email){
        return repository.findById(email);
    }

    public void delete(String email){
        repository.deleteById(email);
    }

    public List<Immigrant> getAll(){
        return repository.getAll();
    }

}
