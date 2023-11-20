package com.ages.informativoparaimigrantes.service;

import com.ages.informativoparaimigrantes.domain.UserData;
import com.ages.informativoparaimigrantes.repository.IUserRepository;
import com.ages.informativoparaimigrantes.security.HashUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private IUserRepository IUserRepository;

    @Autowired
    private HashUtils hashUtils;


    public void save(UserData user){
        user.setPassword(hashUtils.encode(user.getPassword()));
        IUserRepository.save(user);
    }

    public void delete(String email){
        IUserRepository.deleteById(email);
    }

}
