package com.ages.informativoparaimigrantes.controller;

import com.ages.informativoparaimigrantes.dto.ProgramRequestDTO;
import com.ages.informativoparaimigrantes.dto.ProgramResponseDTO;
import com.ages.informativoparaimigrantes.dto.StatusUpdateRequestDTO;
import com.ages.informativoparaimigrantes.enums.ProgramType;
import com.ages.informativoparaimigrantes.enums.Status;
import com.ages.informativoparaimigrantes.service.ProgramServiceImpl;
import com.ages.informativoparaimigrantes.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import javax.persistence.EntityNotFoundException;
import java.util.Date;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

import static com.ages.informativoparaimigrantes.util.DateUtils.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/programs")
public class ProgramController {
    @Autowired
    private ProgramServiceImpl service;
    @PostMapping
    public ResponseEntity<?> save(@RequestBody ProgramRequestDTO program) {

        Date enrollmentStartDate = formatDate(program.getEnrollmentInitialDate());
        Date enrollmentEndDate = formatDate(program.getEnrollmentEndDate());
        Date programInitialDate = formatDate(program.getProgramInitialDate());
        Date programEndDate = formatDate(program.getProgramEndDate());

        if(!isStartBeforeEnd(enrollmentStartDate, enrollmentEndDate) ||
                !isStartBeforeEnd(programInitialDate, programEndDate) ||
                !isStartBeforeEnd(new Date(), enrollmentEndDate) ||
                !isStartBeforeEnd(new Date(), programInitialDate)) {
            return new ResponseEntity<>("Invalid dates for new program", HttpStatus.BAD_REQUEST);
        }

        ProgramResponseDTO savedProgram = this.service.save(program);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedProgram);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProgramResponseDTO> findById(@PathVariable Long id) {
        try {
            ProgramResponseDTO program = this.service.getById(id);
            return new ResponseEntity<>(program, HttpStatus.OK);
        }
        catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/categories")
    public ResponseEntity<Map<String, Long>> getCategoriesWithWithProgramCount() {
        return ResponseEntity.ok(this.service.getCategoriesWithWithProgramCount());
    }

    //Filtro por tags, filtro por linguagem, filtro por "Inscrições abertas", filtro por localização, filtro por turno
    @GetMapping
    public ResponseEntity<List<ProgramResponseDTO>> findAll(
            @RequestParam(required = false) Status status,
            @RequestParam(required = false) String institutionEmail,
            @RequestParam(required = false) ProgramType type,
            @RequestParam(required = false) String language,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) Boolean openSubscription,
            @RequestParam(required = false) String tags){
        List<ProgramResponseDTO> programs = this.service.getFiltered(status, institutionEmail, type,
                                                    language, location, openSubscription, tags);
        return ResponseEntity.ok(programs);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        try {
            this.service.getById(id);
        }
        catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        this.service.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProgram(@PathVariable Long id, @RequestBody ProgramRequestDTO program) {
        try {
            ProgramResponseDTO existingProgram = this.service.getById(id);
            // Se o título, descrição, idioma, localização ou tipo de programa forem alterados, o status deve ser definido como PENDENTE.
            if ((program.getTitle() != null && !program.getTitle().equals(existingProgram.getTitle()))
                    || (program.getDescription() != null  && !program.getDescription().equals(existingProgram.getDescription()))
                    || (program.getLanguage() != null && !program.getLanguage().equals(existingProgram.getLanguage()))
                    || (program.getLocation() != null && !program.getLocation().equals(existingProgram.getLocation()))
                    || (program.getProgramType() != null && !program.getProgramType().equals(existingProgram.getProgramType()))){
                existingProgram.setStatus(Status.PENDING);
            }

            Utils.copyNotNullProperties(program, existingProgram);

            ProgramResponseDTO updatedProgram = this.service.save(existingProgram);
            return new ResponseEntity<>(updatedProgram, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateProgramStatus(@PathVariable Long id,
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

        ProgramResponseDTO updatedProgram;
        try {
            updatedProgram = service.updateStatus(id, status, statusUpdateRequestDTO.getFeedback());
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(updatedProgram, HttpStatus.OK);
    }

    @GetMapping("/countByStatus")
    public ResponseEntity<?> getCountByStatus(@RequestParam String status) {
        Status statusEnum;
        try {
            statusEnum = Status.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("Invalid status.", HttpStatus.BAD_REQUEST);
        }

        long count = service.getCountByStatus(statusEnum);
        return new ResponseEntity<>(count, HttpStatus.OK);
    }
}
