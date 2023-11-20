package com.ages.informativoparaimigrantes.service;

import com.ages.informativoparaimigrantes.domain.UserData;

public interface IUserService {

    void save(UserData user);

    void delete(String email);
}
