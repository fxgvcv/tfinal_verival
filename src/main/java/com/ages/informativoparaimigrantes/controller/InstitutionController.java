package com.ages.informativoparaimigrantes.controller;

import com.ages.informativoparaimigrantes.domain.Institution;
import com.ages.informativoparaimigrantes.dto.InstitutionRegistrationDTO;
import com.ages.informativoparaimigrantes.domain.UserData;
import com.ages.informativoparaimigrantes.dto.InstitutionUserDTO;
import com.ages.informativoparaimigrantes.dto.StatusUpdateRequestDTO;
import com.ages.informativoparaimigrantes.enums.Status;
import com.ages.informativoparaimigrantes.exceptions.EncryptionException;
import com.ages.informativoparaimigrantes.HashUtils;
import com.ages.informativoparaimigrantes.service.InstitutionServiceImpl;
import com.ages.informativoparaimigrantes.service.UserServiceImpl;
import com.ages.informativoparaimigrantes.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/institutions")
public class InstitutionController {

    @Autowired
    private InstitutionServiceImpl institutionServiceImpl;

    @Autowired
    private UserServiceImpl userServiceImpl;

    @Autowired
    private HashUtils hashUtils;

    @DeleteMapping("/{email}")
    public ResponseEntity<?> deleteInstitution(@PathVariable String email)
    {
        Optional<Institution> institution = institutionServiceImpl.getByEmail(email);

        if (institution.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        institutionServiceImpl.delete(email);
        userServiceImpl.delete(institution.get().getInstitutionEmail());

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping()
    public ResponseEntity<?> getAllInstitutions(@RequestParam(required = false) String status)
    {
        if (status == null){
            return new ResponseEntity<>(institutionServiceImpl.getAll(), HttpStatus.OK);
        }

        try {
            Status statusEnum = Status.valueOf(status.toUpperCase());
        }catch (IllegalArgumentException e){
            return new ResponseEntity<>("Invalid status.",HttpStatus.BAD_REQUEST);
        }

        List<Institution> institutions = institutionServiceImpl.getByStatus(status.toUpperCase());

        return new ResponseEntity<>(institutions, HttpStatus.OK);
    }

    @GetMapping("/{email}")
    public ResponseEntity<Institution> getInstitutionById(@PathVariable String email)
    {
        return institutionServiceImpl.getByEmail(email)
                .map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));

    }

    @PutMapping("/{email}")
    public ResponseEntity<?> editInstitution(@PathVariable String email, @RequestBody InstitutionUserDTO institutionUserDTO)
    {
        Optional<Institution> localInstitution = institutionServiceImpl.getByEmail(email);

        UserData userData = (UserData) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!Utils.passwordIsValid(institutionUserDTO.getPasswordOld()) || !hashUtils.decode(userData.getPassword()).equals(institutionUserDTO.getPasswordOld())){
            return new ResponseEntity<>("Invalid password.", HttpStatus.BAD_REQUEST);
        }

        if (institutionUserDTO.getCnpj() != null && !Utils.cnpjIsValid(institutionUserDTO.getCnpj())){
            return new ResponseEntity<>("Invalid CNPJ.", HttpStatus.BAD_REQUEST);
        }
        if(institutionUserDTO.getRegistrantCpf() != null && !Utils.cpfIsValid(institutionUserDTO.getRegistrantCpf())){
            return new ResponseEntity<>("Invalid CPF.", HttpStatus.BAD_REQUEST);
        }

        if (localInstitution.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (institutionUserDTO.getEmail() != null) {
            return new ResponseEntity<>("The email can't be altered", HttpStatus.BAD_REQUEST);
        }
        if(Utils.passwordIsValid(institutionUserDTO.getPasswordNew())){
            userData.setPassword(institutionUserDTO.getPasswordNew());
            userServiceImpl.save(userData);
        }

        Institution localInstitutionGet = localInstitution.get();

        Utils.copyNotNullProperties(institutionUserDTO, localInstitutionGet);

        if ((institutionUserDTO.getInstitutionName() != null && !institutionUserDTO.getInstitutionName().equals(localInstitutionGet.getInstitutionName()))
                || (institutionUserDTO.getType() != null  && !institutionUserDTO.getType().equals(localInstitutionGet.getType()))
                || (institutionUserDTO.getCnpj() != null && !institutionUserDTO.getCnpj().equals(localInstitutionGet.getCnpj()))){
            localInstitutionGet.setStatus(Status.PENDING);
        }

        institutionServiceImpl.save(localInstitutionGet);

        return new ResponseEntity<>(localInstitutionGet, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createInstitution(@RequestBody InstitutionRegistrationDTO institution)
    {
        if (!Utils.cnpjIsValid(institution.getCnpj())){
            return new ResponseEntity<>("Invalid CNPJ.", HttpStatus.BAD_REQUEST);
        }
        if(!Utils.cpfIsValid(institution.getRegistrantCpf())){
            return new ResponseEntity<>("Invalid CPF.", HttpStatus.BAD_REQUEST);
        }
        if (!Utils.emailIsValid(institution.getEmail())){
            return new ResponseEntity<>("Invalid email.", HttpStatus.BAD_REQUEST);
        }
        if (!Utils.passwordIsValid(institution.getPassword())){
            return new ResponseEntity<>("Invalid password.", HttpStatus.BAD_REQUEST);
        }

        Optional<Institution> localInstitution = institutionServiceImpl.getByEmail(institution.getEmail());


        if (localInstitution.isPresent()){
            return new ResponseEntity<>("Email in use.", HttpStatus.CONFLICT);
        }

        Institution institutionEntity = new Institution(institution);
        institutionEntity.setStatus(Status.PENDING);

        try{
            userServiceImpl.save(new UserData(institution));
        } catch (EncryptionException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        institutionServiceImpl.save(institutionEntity);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @PatchMapping("/{email}/status")
    public ResponseEntity<?> updateInstitutionStatus(@PathVariable String email,
                                                     @RequestBody @Valid StatusUpdateRequestDTO statusUpdateRequestDTO) {
        String statusStr = statusUpdateRequestDTO.getStatus();
        if (statusStr == null || statusStr.isBlank()) {
            return new ResponseEntity<>("Status is required.", HttpStatus.BAD_REQUEST);
        }
        Status status;
        try {
            status = Status.valueOf(statusStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("Invalid status.", HttpStatus.BAD_REQUEST);
        }

        try {
            Institution institution = institutionServiceImpl.updateStatus(email, status, statusUpdateRequestDTO.getFeedback());
            return new ResponseEntity<>(institution, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/countByStatus")
    public ResponseEntity<?> getCountByStatus(@RequestParam String status) {
        Status statusEnum;
        try {
            statusEnum = Status.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("Invalid status.", HttpStatus.BAD_REQUEST);
        }

        long count = institutionServiceImpl.getCountByStatus(statusEnum);
        return new ResponseEntity<>(count, HttpStatus.OK);
    }
}
