package com.ages.informativoparaimigrantes.service;

import com.ages.informativoparaimigrantes.domain.Immigrant;

import java.util.List;

public interface IImmigrantService {

    void save(Immigrant immigrant);

    void delete(String email);

    List<Immigrant> getAll();
}
