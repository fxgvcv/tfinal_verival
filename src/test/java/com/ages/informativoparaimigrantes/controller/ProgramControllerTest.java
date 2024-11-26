package com.ages.informativoparaimigrantes.controller;

import com.ages.informativoparaimigrantes.domain.Institution;
import com.ages.informativoparaimigrantes.domain.Program;
import com.ages.informativoparaimigrantes.domain.Tag;
import com.ages.informativoparaimigrantes.dto.ProgramResponseDTO;
import com.ages.informativoparaimigrantes.enums.InstitutionType;
import com.ages.informativoparaimigrantes.enums.ProgramType;
import com.ages.informativoparaimigrantes.enums.Status;
import com.ages.informativoparaimigrantes.service.ProgramServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(ProgramController.class)
public class ProgramControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private String baseEndpoint = "/program";

    @MockBean
    private ProgramServiceImpl service;

//    @Test
//    public void getProgramsShouldReturnProgramList() throws Exception {
//        List<ProgramResponseDTO> mockPrograms = List.of(
//                new ProgramResponseDTO(1L, "any", "any", "any", "any", null, null, null, null, Status.PENDING, "any", "any", null, "any", null, "any"));
//
//        when(service.getFiltered(any(), any(), any(), any(), any(), any(), any())).thenReturn(mockPrograms);
//
//        mockMvc.perform(get(baseEndpoint))
//                .andExpect(status().isOk())
//                .andExpect(content().json(new ObjectMapper().writeValueAsString(mockPrograms)));
//    }
//
//    @Test
//    void testGetFiltered_withStatusAndLocationFilters() throws Exception {
//        // Configuração de datas
//        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        Date programInitialDate = dateFormat.parse("2024-01-01");
//        Date programEndDate = dateFormat.parse("2024-12-31");
//        Date enrollmentInitialDate = dateFormat.parse("2023-12-01");
//        Date enrollmentEndDate = dateFormat.parse("2023-12-31");
//
//        // Mock do repositório
//        List<Program> mockPrograms = List.of(
//                Program.builder()
//                        .id(1L)
//                        .title("Program 1")
//                        .description("Descrição 1")
//                        .institutionEmail("email1@domain.com")
//                        .link("https://link1.com")
//                        .programInitialDate(programInitialDate)
//                        .programEndDate(programEndDate)
//                        .language("PT-BR")
//                        .enrollmentInitialDate(enrollmentInitialDate)
//                        .enrollmentEndDate(enrollmentEndDate)
//                        .status(Status.APPROVED)
//                        .location("São Paulo")
//                        .tags(List.of(new Tag(1L, "Educação", "PT-BR")))
//                        .programType(ProgramType.HIGHER)
//                        .dataFile("file1.pdf")
//                        .build(),
//                Program.builder()
//                        .id(2L)
//                        .title("Program 2")
//                        .description("Descrição 2")
//                        .institutionEmail("email2@domain.com")
//                        .link("https://link2.com")
//                        .programInitialDate(dateFormat.parse("2024-02-01"))
//                        .programEndDate(dateFormat.parse("2024-11-30"))
//                        .language("PT-BR")
//                        .enrollmentInitialDate(dateFormat.parse("2023-11-01"))
//                        .enrollmentEndDate(dateFormat.parse("2023-11-30"))
//                        .status(Status.APPROVED)
//                        .location("São Paulo")
//                        .tags(List.of(new Tag(2L, "Saúde", "PT-BR")))
//                        .programType(ProgramType.HIGHER)
//                        .dataFile("file2.pdf")
//                        .build()
//        );
//
//        Mockito.when(repository.findWithFilters(Status.APPROVED, null, null, null, "São Paulo", null, null))
//                .thenReturn(mockPrograms);
//
//        // Chamada do método
//        List<ProgramResponseDTO> result = service.getFiltered(Status.APPROVED, null, null, null, "São Paulo", null, null);
//
//        // Verificações
//        assertEquals(2, result.size());
//        assertEquals("Program 1", result.get(0).getTitle());
//        assertEquals("Program 2", result.get(1).getTitle());
//    }

}

