package com.ages.informativoparaimigrantes.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import com.ages.informativoparaimigrantes.domain.Program;
import com.ages.informativoparaimigrantes.dto.ProgramResponseDTO;
import com.ages.informativoparaimigrantes.enums.ProgramType;
import com.ages.informativoparaimigrantes.enums.Status;
import com.ages.informativoparaimigrantes.repository.IProgramRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class ProgramServiceTest {

    @Autowired
    private ProgramServiceImpl programService;  // O serviço que estamos testando

    @MockBean
    private IProgramRepository programRepository;  // Repositório mockado

    @Test
    public void testGetFilteredPrograms_withLanguageFilter() {
        // Arrange: Criar programas fictícios para simulação
        Program program1 = new Program(1L, "institution1@example.com", "Program 1", "Description 1", "http://link1.com", "English",
                null, null, null, null, Status.APPROVED, "Location 1", null, null, ProgramType.HIGHER, null);
        Program program2 = new Program(2L, "institution2@example.com", "Program 2", "Description 2", "http://link2.com", "Spanish",
                null, null, null, null, Status.APPROVED, "Location 2", null, null, ProgramType.HIGHER, null);

        // Simula o comportamento do repositório para quando o filtro de idioma é "English"
        when(programRepository.findWithFilters(
                eq(Status.APPROVED),     // status
                eq(null),                // institutionEmail
                eq(null),                // programType
                eq("English"),           // language
                eq(null),                // location
                eq(null)                 // openSubscription
        )).thenReturn(Arrays.asList(program1));

        // Act: Chama o serviço para obter programas filtrados por idioma
        List<ProgramResponseDTO> filteredPrograms = programService.getFiltered(
                Status.APPROVED,  // Status
                null,              // institutionEmail
                null,              // programType
                "English",        // language
                null,              // location
                null,              // openSubscription
                null               // tags
        );

        // Assert: Verifica se o filtro está funcionando corretamente
        assertEquals(1, filteredPrograms.size());
        assertEquals("Program 1", filteredPrograms.get(0).getTitle());
        assertEquals("English", filteredPrograms.get(0).getLanguage());  // Verificação adicional
    }

    // Teste para filtro de status
    @Test
    public void testGetFilteredPrograms_withStatusFilter() {
        // Arrange: Criar programas fictícios para simulação
        Program program1 = new Program(1L, "institution1@example.com", "Program 1", "Description 1", "http://link1.com", "English",
                null, null, null, null, Status.APPROVED, "Location 1", null, null, ProgramType.HIGHER, null);
        Program program2 = new Program(2L, "institution2@example.com", "Program 2", "Description 2", "http://link2.com", "English",
                null, null, null, null, Status.PENDING, "Location 2", null, null, ProgramType.HIGHER, null);

        // Simula o comportamento do repositório para quando o filtro de status é "APPROVED"
        when(programRepository.findWithFilters(
                eq(Status.APPROVED),  // status
                eq(null),             // institutionEmail
                eq(null),             // programType
                eq(null),             // language
                eq(null),             // location
                eq(null)              // openSubscription
        )).thenReturn(Arrays.asList(program1));

        // Act: Chama o serviço para obter programas filtrados por status
        List<ProgramResponseDTO> filteredPrograms = programService.getFiltered(
                Status.APPROVED,  // Status
                null,             // institutionEmail
                null,             // programType
                null,             // language
                null,             // location
                null,             // openSubscription
                null              // tags
        );

        // Assert: Verifica se o filtro está funcionando corretamente
        assertEquals(1, filteredPrograms.size());  // Espera 1 programa
        assertEquals("Program 1", filteredPrograms.get(0).getTitle());  // Programa correto
        assertEquals(Status.APPROVED, filteredPrograms.get(0).getStatus());  // Verifica o status

        // Verifica que o programa com status "PENDING" não foi incluído
        assertTrue(filteredPrograms.stream().noneMatch(program -> Status.PENDING.equals(program.getStatus())));
    }

    @Test
    public void testGetFilteredPrograms_withProgramTypeFilter() {
        // Arrange: Criar programas fictícios para simulação
        Program program1 = new Program(1L, "institution1@example.com", "Program 1", "Description 1", "http://link1.com", "English",
                null, null, null, null, Status.APPROVED, "Location 1", null, null, ProgramType.HIGHER, null);
        Program program2 = new Program(2L, "institution2@example.com", "Program 2", "Description 2", "http://link2.com", "English",
                null, null, null, null, Status.APPROVED, "Location 2", null, null, ProgramType.BASIC, null);

        // Simula o comportamento do repositório para quando o filtro de tipo de programa é "HIGHER"
        when(programRepository.findWithFilters(
                eq(Status.APPROVED),       // status
                eq(null),                  // institutionEmail
                eq(ProgramType.HIGHER),    // programType
                eq(null),                  // language
                eq(null),                  // location
                eq(null)                   // openSubscription
        )).thenReturn(Arrays.asList(program1));  // Retorna apenas program1, que tem o tipo "HIGHER"

        // Act: Chama o serviço para obter programas filtrados por tipo de programa
        List<ProgramResponseDTO> filteredPrograms = programService.getFiltered(
                Status.APPROVED,       // Status
                null,                  // institutionEmail
                ProgramType.HIGHER,    // ProgramType
                null,                  // Language
                null,                  // Location
                null,                  // openSubscription
                null                   // Tags
        );

        // Log para verificar o conteúdo retornado
        System.out.println("Filtered programs: " + filteredPrograms);

        // Assert: Verifica se o filtro de tipo de programa está funcionando corretamente
        assertEquals(1, filteredPrograms.size());  // Espera 1 programa com o tipo "HIGHER"
        assertEquals("Program 1", filteredPrograms.get(0).getTitle());  // O título do programa deve ser "Program 1"
        assertEquals(ProgramType.HIGHER, filteredPrograms.get(0).getProgramType());  // Verifica se o tipo do programa é "HIGHER"
    }

    // Teste para filtro de localização
    @Test
    public void testGetFilteredPrograms_withLocationFilter() {
        // Arrange: Criar programas fictícios para simulação
        Program program1 = new Program(1L, "institution1@example.com", "Program 1", "Description 1", "http://link1.com", "English",
                null, null, null, null, Status.APPROVED, "Location 1", null, null, ProgramType.HIGHER, null);
        Program program2 = new Program(2L, "institution2@example.com", "Program 2", "Description 2", "http://link2.com", "Spanish",
                null, null, null, null, Status.APPROVED, "Location 2", null, null, ProgramType.HIGHER, null);

        // Simula o comportamento do repositório para quando o filtro de localização for "Location 1"
        when(programRepository.findWithFilters(
                eq(Status.APPROVED),  // status
                eq(null),              // institutionEmail (não utilizado no filtro)
                eq(null),              // programType (não utilizado no filtro)
                eq(null),              // language (não utilizado no filtro)
                eq("Location 1"),      // location
                eq(null)               // openSubscription (não utilizado no filtro)
        )).thenReturn(Arrays.asList(program1));  // Retorna apenas o program1, que tem a localização "Location 1"

        // Act: Chama o serviço para obter programas filtrados por localização
        List<ProgramResponseDTO> filteredPrograms = programService.getFiltered(
                Status.APPROVED,   // Status
                null,              // institutionEmail
                null,              // programType
                null,              // language
                "Location 1",      // Location
                null,              // openSubscription
                null               // Tags
        );

        // Assert: Verifica se o filtro de localização está funcionando corretamente
        assertEquals(1, filteredPrograms.size());  // Espera 1 programa com a localização "Location 1"
        assertEquals("Program 1", filteredPrograms.get(0).getTitle());  // O título do programa deve ser "Program 1"
        assertEquals("Location 1", filteredPrograms.get(0).getLocation()); // Verifica se a localização é "Location 1"

        // Verifica se o programa com localização diferente ("Location 2") não foi incluído
        assertTrue(filteredPrograms.stream().noneMatch(program -> "Location 2".equals(program.getLocation())));
    }


    @Test
    public void testGetFilteredPrograms_withMultipleFilters() {
        Program program1 = new Program(1L, "institution1@example.com", "Program 1", "Description 1", "http://link1.com", "English",
                null, null, null, null, Status.APPROVED, "Location 1", null, null, ProgramType.HIGHER, null);

        // Simula o comportamento do repositório com múltiplos filtros aplicados
        when(programRepository.findWithFilters(
                eq(Status.APPROVED),      // Status
                eq("institution1@example.com"), // institutionEmail
                eq(ProgramType.HIGHER),   // ProgramType
                eq("English"),            // Language
                eq("Location 1"),         // Location
                eq(null)                  // openSubscription (como foi passado como null no teste)
        )).thenReturn(Arrays.asList(program1));

        // Act: Chama o serviço com múltiplos filtros
        List<ProgramResponseDTO> filteredPrograms = programService.getFiltered(
                Status.APPROVED,            // Status
                "institution1@example.com", // Institution Email
                ProgramType.HIGHER,         // Program Type
                "English",                  // Language
                "Location 1",               // Location
                null,                       // openSubscription
                null                        // Tags
        );

        // Assert: Verifica se o filtro com múltiplos parâmetros está funcionando corretamente
        assertEquals(1, filteredPrograms.size());  // Espera 1 programa sendo filtrado
        assertEquals("Program 1", filteredPrograms.get(0).getTitle());  // O título do programa deve ser "Program 1"
    }

    @Test
    public void testProgramsSortingByEnrollmentDate() {
        // Dado que temos dois programas, um com data de inscrição e outro sem
        Date dateWithEnrollment = Date.from(LocalDate.of(2024, 12, 1).atStartOfDay(ZoneId.systemDefault()).toInstant());

        Program programWithDate = new Program(1L, "institution1@example.com", "Program 1", "Description 1", "http://link1.com", "English",
                null, null, dateWithEnrollment, null, Status.APPROVED, "Location 1", null, null, ProgramType.HIGHER, null);

        Program programWithoutDate = new Program(2L, "institution1@example.com", "Program 2", "Description 2", "http://link2.com", "English",
                null, null, null, null, Status.APPROVED, "Location 2", null, null, ProgramType.HIGHER, null);

        // Simulando o comportamento do repositório (findAll ou qualquer método usado no serviço)
        when(programRepository.findAll()).thenReturn(Arrays.asList(programWithDate, programWithoutDate));

        // Chama o serviço para recuperar os programas
        List<ProgramResponseDTO> sortedPrograms = programService.getProgramsSortedByEnrollmentDate();

        // Verificando se o programa com data de inscrição está em primeiro
        assertEquals("Program 1", sortedPrograms.get(0).getTitle());
        assertEquals("Program 2", sortedPrograms.get(1).getTitle());
    }

    @Test
    public void testProgramsWithoutEnrollmentDateSortingByName() {
        Program programOne = new Program(1L, "institution1@example.com", "ZNX Program", "Description 1", "http://link1.com", "English",
                null, null, null, null, Status.APPROVED, "Location 1", null, null, ProgramType.HIGHER, null);

        Program programTwo = new Program(2L, "institution1@example.com", "ABCedário", "Description 2", "http://link2.com", "English",
                null, null, null, null, Status.APPROVED, "Location 2", null, null, ProgramType.HIGHER, null);

        // Simulando o comportamento do repositório
        when(programRepository.findAll()).thenReturn(Arrays.asList(programOne, programTwo));

        // Chama o serviço para recuperar os programas ordenados
        List<ProgramResponseDTO> sortedPrograms = programService.getProgramsSortedByEnrollmentDate();

        // Verifica se a ordenação foi feita corretamente pelo nome
        assertEquals("ABCedário", sortedPrograms.get(0).getTitle());  // "ABCedário" deve vir primeiro
        assertEquals("ZNX Program", sortedPrograms.get(1).getTitle());  // "ZNX Program" deve vir depois
    }

//    @Test
//    public void testProgramsWithoutEnrollmentDateSortingByName() {
//        // Dado que temos dois programas sem data de inscrição, mas com nomes diferentes
//        Program programWithoutDate1 = new Program("Program Z", null, "Description 1");
//        Program programWithoutDate2 = new Program("Program A", null, "Description 2");
//
//        // Adicionando-os à lista
//        List<Program> programs = Arrays.asList(programWithoutDate1, programWithoutDate2);
//
//        // Ordenando a lista de programas pelo nome
//        programs.sort((p1, p2) -> p1.getName().compareTo(p2.getName()));
//
//        // Validando se a ordem está correta
//        assertEquals("Program A", programs.get(0).getName());  // "Program A" deve vir antes de "Program Z"
//        assertEquals("Program Z", programs.get(1).getName());
//    }
//
//    @Test
//    public void testProgramsWithAndWithoutEnrollmentDateSortedByDateThenName() {
//        // Criando programas com e sem data de inscrição
//        Program programWithDate = new Program("Program 1", LocalDate.of(2024, 12, 1), "Description 1");
//        Program programWithoutDate1 = new Program("Program Z", null, "Description 1");
//        Program programWithoutDate2 = new Program("Program A", null, "Description 2");
//
//        // Adicionando-os à lista
//        List<Program> programs = Arrays.asList(programWithDate, programWithoutDate1, programWithoutDate2);
//
//        // Ordenando a lista de programas, primeiro pela data de inscrição, depois por nome
//        programs.sort((p1, p2) -> {
//            if (p1.getEnrollmentDate() == null) return 1; // coloca os programas sem data no final
//            if (p2.getEnrollmentDate() == null) return -1;
//            int dateComparison = p1.getEnrollmentDate().compareTo(p2.getEnrollmentDate());
//            if (dateComparison != 0) return dateComparison;
//
//            // Se as datas forem iguais, ordenar por nome
//            return p1.getName().compareTo(p2.getName());
//        });
//
//        // Validando se a ordem está correta
//        assertEquals("Program 1", programs.get(0).getName());  // O programa com data de inscrição deve vir primeiro
//        assertEquals("Program A", programs.get(1).getName());  // O programa sem data de inscrição, mas com nome A, deve vir depois
//        assertEquals("Program Z", programs.get(2).getName());  // O programa sem data de inscrição, mas com nome Z, deve vir por último
//    }
//
//    @Test
//    public void testProgramsWithExpiredEnrollmentDate() {
//        // Criar programas com data de inscrição no passado
//        Program expiredProgram = new Program("Expired Program", LocalDate.of(2023, 12, 1), "Description 1");
//        Program activeProgram = new Program("Active Program", LocalDate.of(2024, 12, 1), "Description 2");
//
//        // Adicionando-os à lista
//        List<Program> programs = Arrays.asList(expiredProgram, activeProgram);
//
//        // Simulando o repositório para retornar programas
//        when(programRepository.findWithFilters(
//                eq(null),  // status
//                eq(null),  // institutionEmail
//                eq(null),  // programType
//                eq(null),  // language
//                eq(null),  // location
//                eq(true)   // openSubscription
//        )).thenReturn(programs.stream()
//                .filter(p -> p.getEnrollmentDate() != null && !p.getEnrollmentDate().isBefore(LocalDate.now()))  // Filtrando por data válida
//                .collect(Collectors.toList()));
//
//        // Act: Chama o serviço para obter programas
//        List<ProgramResponseDTO> filteredPrograms = programService.getFiltered(
//                Status.APPROVED,  // Status
//                null,             // institutionEmail
//                null,             // programType
//                null,             // language
//                null,             // location
//                null,             // openSubscription
//                null              // tags
//        );
//
//        // Assert: Verifica se o programa expirado foi excluído
//        assertEquals(1, filteredPrograms.size());
//        assertEquals("Active Program", filteredPrograms.get(0).getTitle());
//    }
}