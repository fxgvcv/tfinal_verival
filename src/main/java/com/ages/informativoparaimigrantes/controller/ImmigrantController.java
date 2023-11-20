package com.ages.informativoparaimigrantes.controller;

import com.ages.informativoparaimigrantes.domain.Immigrant;
import com.ages.informativoparaimigrantes.dto.ImmigrantRegistrationDTO;
import com.ages.informativoparaimigrantes.domain.UserData;
import com.ages.informativoparaimigrantes.dto.ImmigrantUserDTO;
import com.ages.informativoparaimigrantes.exceptions.EncryptionException;
import com.ages.informativoparaimigrantes.security.HashUtils;
import com.ages.informativoparaimigrantes.service.ImmigrantServiceImpl;
import com.ages.informativoparaimigrantes.service.UserServiceImpl;
import com.ages.informativoparaimigrantes.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/immigrants")
public class ImmigrantController
{
    @Autowired
    private ImmigrantServiceImpl immigrantServiceImpl;

    @Autowired
    private UserServiceImpl userServiceImpl;

    @Autowired
    private HashUtils hashUtils;

    @DeleteMapping("/{email}")
    public ResponseEntity<?> deleteImmigrant(@PathVariable String email)
    {
        Optional<Immigrant> immigrant = immigrantServiceImpl.getByEmail(email);

        if (immigrant.isEmpty()){
            return new ResponseEntity<>("Immigrant not found.", HttpStatus.NOT_FOUND);
        }

        immigrantServiceImpl.delete(email);
        userServiceImpl.delete(immigrant.get().getEmail());

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping()
    public ResponseEntity<List<Immigrant>> getAllImmigrants()
    {
        List<Immigrant> immigrants = immigrantServiceImpl.getAll();

        //No 404 validation because there is no target ID. Returning an empty list is valid.
        return new ResponseEntity<>(immigrants, HttpStatus.OK);
    }

    @GetMapping("/{email}")
    public ResponseEntity<Immigrant> getImmigrantById(@PathVariable String email)
    {
        return immigrantServiceImpl.getByEmail(email)
                .map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));

    }

    @PatchMapping("/{email}")
    public ResponseEntity<?> editImmigrant(@PathVariable String email, @RequestBody ImmigrantUserDTO immigrantUserDTO)
    {
        Optional<Immigrant> localImmigrant = immigrantServiceImpl.getByEmail(email);
        UserData userData = (UserData) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!Utils.passwordIsValid(immigrantUserDTO.getPasswordOld()) || !hashUtils.decode(userData.getPassword()).equals(immigrantUserDTO.getPasswordOld())){
            return new ResponseEntity<>("Invalid password.", HttpStatus.BAD_REQUEST);
        }

        if (localImmigrant.isEmpty()){
            return new ResponseEntity<>("Immigrant not found.", HttpStatus.NOT_FOUND);
        }
        if (immigrantUserDTO.getEmail() != null) {
            return new ResponseEntity<>("The email can't be altered", HttpStatus.BAD_REQUEST);
        }
        if(Utils.passwordIsValid(immigrantUserDTO.getPasswordNew())){
            userData.setPassword(immigrantUserDTO.getPasswordNew());
            userServiceImpl.save(userData);
        }

        Immigrant localImmigrantGet = localImmigrant.get();

        Utils.copyNotNullProperties(immigrantUserDTO, localImmigrantGet);

        immigrantServiceImpl.save(localImmigrantGet);

        return new ResponseEntity<>(localImmigrantGet, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createImmigrant(@RequestBody ImmigrantRegistrationDTO immigrant)
    {

        if (!Utils.emailIsValid(immigrant.getEmail())){
            return new ResponseEntity<>("Invalid email.", HttpStatus.BAD_REQUEST);
        }
        if (!Utils.passwordIsValid(immigrant.getPassword())){
            return new ResponseEntity<>("Invalid password.", HttpStatus.BAD_REQUEST);
        }
        if (immigrant.getName().isEmpty()){
            return new ResponseEntity<>("Invalid name.", HttpStatus.BAD_REQUEST);
        }

        Optional<Immigrant> localImmigrant = immigrantServiceImpl.getByEmail(immigrant.getEmail());

        if (localImmigrant.isPresent()){
            return new ResponseEntity<>("Email in use.", HttpStatus.CONFLICT);
        }

        try{
            userServiceImpl.save(new UserData(immigrant));
        } catch (EncryptionException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        immigrantServiceImpl.save(immigrant);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
