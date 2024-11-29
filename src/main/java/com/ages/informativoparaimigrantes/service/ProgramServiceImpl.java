package com.ages.informativoparaimigrantes.service;

import com.ages.informativoparaimigrantes.domain.Program;
import com.ages.informativoparaimigrantes.domain.Tag;
import com.ages.informativoparaimigrantes.domain.UserData;
import com.ages.informativoparaimigrantes.dto.ProgramRequestDTO;
import com.ages.informativoparaimigrantes.dto.ProgramResponseDTO;
import com.ages.informativoparaimigrantes.dto.TagRequestDTO;
import com.ages.informativoparaimigrantes.enums.ProgramType;
import com.ages.informativoparaimigrantes.enums.Status;
import com.ages.informativoparaimigrantes.enums.UserType;
import com.ages.informativoparaimigrantes.repository.IProgramRepository;
import com.ages.informativoparaimigrantes.repository.ITagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.ages.informativoparaimigrantes.util.DateUtils.formatDate;

@Service
public class ProgramServiceImpl implements IProgramService {
    @Autowired
    private IProgramRepository repository;
    @Autowired
    private ITagRepository iTagRepository;

    @Override
    public ProgramResponseDTO save(ProgramRequestDTO program) {
        List<Tag>  tags = new ArrayList<Tag>();
        for(TagRequestDTO tagRequestDTO:program.getTags()){
            Tag aux = iTagRepository.findByLanguageAndName(tagRequestDTO.getLanguage(), tagRequestDTO.getName());
            if(aux != null){
                tags.add(aux);
            }
        }
        UserData userData = (UserData) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(userData != null && userData.isEnabled()){

            if (!userData.getEmail().equals(program.getInstitutionEmail()) && !userData.getType().equals(UserType.ADMIN)) {
                throw new RuntimeException("You can't create a program for another user");
            }
            Program programEntity = repository.save(Program.builder()
                    .institutionEmail(program.getInstitutionEmail())
                    .title(program.getTitle())
                    .description(program.getDescription())
                    .link(program.getLink())
                    .language(program.getLanguage())
                    .enrollmentInitialDate(formatDate(program.getEnrollmentInitialDate()))
                    .enrollmentEndDate(formatDate(program.getEnrollmentEndDate()))
                    .programEndDate(formatDate(program.getProgramEndDate()))
                    .programInitialDate(formatDate(program.getProgramInitialDate()))
                    .status(Status.PENDING)
                    .location(program.getLocation())
                    .tags(tags)
                    .dataFile(program.getFile())
                    .programType(program.getProgramType())
                    .feedback("")
                    .build());

            return ProgramResponseDTO.builder()
                    .id(programEntity.getId())
                    .institutionEmail(programEntity.getInstitutionEmail())
                    .title(programEntity.getTitle())
                    .description(programEntity.getDescription())
                    .link(programEntity.getLink())
                    .language(programEntity.getLanguage())
                    .enrollmentInitialDate(programEntity.getEnrollmentInitialDate())
                    .enrollmentEndDate(programEntity.getEnrollmentEndDate())
                    .programEndDate(programEntity.getProgramEndDate())
                    .programInitialDate(programEntity.getProgramInitialDate())
                    .status(programEntity.getStatus())
                    .location(programEntity.getLocation())
                    .tags(programEntity.getTags())
                    .file(programEntity.getDataFile())
                    .programType(programEntity.getProgramType())
                    .feedback(programEntity.getFeedback())
                    .build();
        }
        throw new RuntimeException("No user was found for the current request.");
    }

    @Override
    public ProgramResponseDTO save(ProgramResponseDTO program) {

        UserData userData = (UserData) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(userData != null && userData.isEnabled()) {

            if (!userData.getEmail().equals(program.getInstitutionEmail()) && !userData.getType().equals(UserType.ADMIN)) {
                throw new RuntimeException("You can't update a program for another user");
            }

            Program programEntity = repository.save(Program.builder()
                    .id(program.getId())
                    .institutionEmail(program.getInstitutionEmail())
                    .title(program.getTitle())
                    .description(program.getDescription())
                    .link(program.getLink())
                    .language(program.getLanguage())
                    .enrollmentInitialDate(program.getEnrollmentInitialDate())
                    .enrollmentEndDate(program.getEnrollmentEndDate())
                    .programEndDate(program.getProgramEndDate())
                    .programInitialDate(program.getProgramInitialDate())
                    .status(program.getStatus())
                    .location(program.getLocation())
                    .tags(program.getTags())
                    .dataFile(program.getFile())
                    .programType(program.getProgramType())
                    .build());

            return ProgramResponseDTO.builder()
                    .id(programEntity.getId())
                    .institutionEmail(programEntity.getInstitutionEmail())
                    .title(programEntity.getTitle())
                    .description(programEntity.getDescription())
                    .link(programEntity.getLink())
                    .language(programEntity.getLanguage())
                    .enrollmentInitialDate(programEntity.getEnrollmentInitialDate())
                    .enrollmentEndDate(programEntity.getEnrollmentEndDate())
                    .programEndDate(programEntity.getProgramEndDate())
                    .programInitialDate(programEntity.getProgramInitialDate())
                    .status(programEntity.getStatus())
                    .location(programEntity.getLocation())
                    .tags(programEntity.getTags())
                    .file(programEntity.getDataFile())
                    .programType(programEntity.getProgramType())
                    .build();
        }
        throw new RuntimeException("No user was found for the current request.");
    }

    public ProgramResponseDTO getById(Long id) {
        Program program = repository.getReferenceById(id);
        return ProgramResponseDTO.builder()
                .id(program.getId())
                .institutionEmail(program.getInstitutionEmail())
                .title(program.getTitle())
                .description(program.getDescription())
                .link(program.getLink())
                .language(program.getLanguage())
                .enrollmentInitialDate(program.getEnrollmentInitialDate())
                .enrollmentEndDate(program.getEnrollmentEndDate())
                .programEndDate(program.getProgramEndDate())
                .programInitialDate(program.getProgramInitialDate())
                .status(program.getStatus())
                .location(program.getLocation())
                .tags(program.getTags())
                .file(program.getDataFile())
                .programType(program.getProgramType())
                .feedback(program.getFeedback())
                .build();
    }

    public List<ProgramResponseDTO> getByInstitutionEmail(String email) {
        return convertListOfEntityToListOfResponseDTO(repository.findProgramByInstitutionEmail(email));
    }

    public List<ProgramResponseDTO> getAll () {
        return convertListOfEntityToListOfResponseDTO(repository.findAll());
    }

    public List<ProgramResponseDTO> getFiltered (Status status, String institutionEmail, ProgramType programType,
                                                 String language, String location, Boolean openSubscription, String tags) {

        List<String> tagsAsList = (tags != null) ? List.of(tags.split(",")) : List.of();

        List<ProgramResponseDTO> programs = convertListOfEntityToListOfResponseDTO(repository.findWithFilters(status, institutionEmail, programType,
                language, location, openSubscription));

        if (!tagsAsList.isEmpty()) {
            programs.removeIf(program -> Collections
                    .disjoint(program.getTags()
                            .stream()
                            .map(x -> x.getName().toLowerCase())
                    .collect(Collectors.toList()), tagsAsList
                            .stream()
                            .map(String::toLowerCase)
                            .collect(Collectors.toList())));
        }

        return programs;
    }

    public void deleteById (Long id){
        repository.deleteById(id);
    }

    private List<ProgramResponseDTO> convertListOfEntityToListOfResponseDTO (List <Program> programEntities)
    {
        return programEntities.stream()
                .map(program -> ProgramResponseDTO.builder()
                        .id(program.getId())
                        .institutionEmail(program.getInstitutionEmail())
                        .title(program.getTitle())
                        .description(program.getDescription())
                        .link(program.getLink())
                        .language(program.getLanguage())
                        .enrollmentInitialDate(program.getEnrollmentInitialDate())
                        .enrollmentEndDate(program.getEnrollmentEndDate())
                        .programEndDate(program.getProgramEndDate())
                        .programInitialDate(program.getProgramInitialDate())
                        .status(program.getStatus())
                        .location(program.getLocation())
                        .tags(program.getTags())
                        .file(program.getDataFile())
                        .programType(program.getProgramType())
                        .feedback(program.getFeedback())
                        .build())
                .collect(Collectors.toList());
    }

    public Map<String, Long> getCategoriesWithWithProgramCount ()
    {
        List<Object[]> categoriesWithProgramCount = repository.getCategoriesWithWithProgramCount();

        return categoriesWithProgramCount.stream()
                .collect(Collectors.toMap(
                        categoryWithProgramCount -> ((ProgramType) categoryWithProgramCount[0]).name(),
                        categoryWithProgramCount -> (Long) categoryWithProgramCount[1]
                ));
    }

    public ProgramResponseDTO updateStatus(Long id, Status status, String feedback) {
        Program program;
        try {
            program = repository.getReferenceById(id);
        }
        catch (Exception e) {
            throw new IllegalArgumentException("Program not found.");
        }
        program.setStatus(status);
        program.setFeedback(feedback);
        repository.save(program);
        return ProgramResponseDTO.builder()
                .id(program.getId())
                .institutionEmail(program.getInstitutionEmail())
                .title(program.getTitle())
                .description(program.getDescription())
                .link(program.getLink())
                .language(program.getLanguage())
                .enrollmentInitialDate(program.getEnrollmentInitialDate())
                .enrollmentEndDate(program.getEnrollmentEndDate())
                .programEndDate(program.getProgramEndDate())
                .programInitialDate(program.getProgramInitialDate())
                .status(program.getStatus())
                .location(program.getLocation())
                .tags(program.getTags())
                .file(program.getDataFile())
                .programType(program.getProgramType())
                .feedback(program.getFeedback())
                .build();
    }
    public long getCountByStatus(Status status) {
        return repository.countByStatus(status);
    }

    public List<ProgramResponseDTO> getProgramsSortedByEnrollmentDate() {
        List<Program> programs = repository.findAll();

        // Ordena primeiro pela data de inscrição e, se for null, pela ordem alfabética do nome
        programs.sort((p1, p2) -> {
            if (p1.getEnrollmentInitialDate() == null && p2.getEnrollmentInitialDate() == null) {
                // Ambos sem data, ordena por nome
                return p1.getTitle().compareTo(p2.getTitle());
            } else if (p1.getEnrollmentInitialDate() == null) {
                return 1; // p1 vai para o final
            } else if (p2.getEnrollmentInitialDate() == null) {
                return -1; // p2 vai para o final
            }
            // Caso haja data, ordena por data
            return p2.getEnrollmentInitialDate().compareTo(p1.getEnrollmentInitialDate());
        });

        // Converte a lista de Program para ProgramResponseDTO
        return programs.stream()
                .map(program -> new ProgramResponseDTO(
                        program.getId(),
                        program.getTitle(),
                        program.getDescription(),
                        program.getLink(),
                        program.getLanguage(),
                        program.getProgramInitialDate(),
                        program.getProgramEndDate(),
                        program.getEnrollmentInitialDate(),
                        program.getEnrollmentEndDate(),
                        program.getStatus(),
                        program.getInstitutionEmail(),
                        program.getLocation(),
                        program.getTags(),
                        program.getDataFile(),
                        program.getProgramType(),
                        program.getFeedback()
                ))
                .collect(Collectors.toList());
    }
}
