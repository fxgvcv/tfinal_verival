package com.ages.informativoparaimigrantes.controller;

import com.ages.informativoparaimigrantes.domain.Tag;
import com.ages.informativoparaimigrantes.dto.ProgramResponseDTO;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(ProgramController.class)
class ProgramControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private String baseEndpoint = "/program";

    @MockBean
    private ProgramServiceImpl service;

    // 1. Retorno de Lista de Programas
    @Test
    public void getProgramsShouldReturnProgramList() throws Exception {
        List<ProgramResponseDTO> mockPrograms = List.of(
                ProgramResponseDTO.builder()
                        .id(1L)
                        .title("any")
                        .description("any")
                        .link("any")
                        .language("any")
                        .programInitialDate(null)
                        .programEndDate(null)
                        .enrollmentInitialDate(null)
                        .enrollmentEndDate(null)
                        .status(Status.PENDING)
                        .institutionEmail("any")
                        .location("any")
                        .tags(List.of(
                                new Tag(1L, "Education", "PT-BR"),
                                new Tag(2L, "Work", "EN")
                        ))
                        .file("any")
                        .programType(ProgramType.HIGHER)
                        .feedback("any")
                        .build()
        );

        when(service.getFiltered(any(), any(), any(), any(), any(), any(), any())).thenReturn(mockPrograms);

        mockMvc.perform(get(baseEndpoint))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(mockPrograms)));
    }

    // 2. Filtros Sem Parâmetros
    @Test
    void testGetFiltered_withNoFilters() throws Exception {
        List<ProgramResponseDTO> mockPrograms = List.of(
                ProgramResponseDTO.builder()
                        .id(1L)
                        .title("Program 1")
                        .description("Description")
                        .status(Status.PENDING)
                        .location("São Paulo")
                        .tags(Collections.emptyList())
                        .file("file1.pdf")
                        .programType(ProgramType.HIGHER)
                        .feedback("any")
                        .build()
        );

        when(service.getFiltered(null, null, null, null, null, null, null)).thenReturn(mockPrograms);

        mockMvc.perform(get(baseEndpoint))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(mockPrograms)));
    }

    // 3. Filtros Aplicados por Status e Localização
    @Test
    void testGetFiltered_withStatusAndLocationFilters() throws Exception {
        ProgramResponseDTO mockProgram = ProgramResponseDTO.builder()
                .id(1L)
                .title("Program 1")
                .description("Description")
                .status(Status.APPROVED)
                .location("São Paulo")
                .tags(Collections.emptyList())
                .file("any")
                .programType(ProgramType.HIGHER)
                .feedback("any")
                .build();

        when(service.getFiltered(Status.APPROVED, null, null, null, "São Paulo", null, null)).thenReturn(List.of(mockProgram));

        mockMvc.perform(get(baseEndpoint + "?status=APPROVED&location=São Paulo"))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(List.of(mockProgram))));
    }

    // 4. Múltiplos Filtros Aplicados
    @Test
    void testGetFiltered_withMultipleFilters() throws Exception {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date programInitialDate = dateFormat.parse("2024-01-01");
        Date programEndDate = dateFormat.parse("2024-12-31");

        ProgramResponseDTO mockProgram = ProgramResponseDTO.builder()
                .id(1L)
                .title("Program 1")
                .description("Description")
                .programInitialDate(programInitialDate)
                .programEndDate(programEndDate)
                .status(Status.APPROVED)
                .location("São Paulo")
                .tags(Collections.emptyList())
                .file("any")
                .programType(ProgramType.HIGHER)
                .feedback("any")
                .build();

        when(service.getFiltered(Status.APPROVED, "Higher", "PT-BR", null, "São Paulo", programInitialDate, programEndDate)).thenReturn(List.of(mockProgram));

        mockMvc.perform(get(baseEndpoint + "?status=APPROVED&type=Higher&language=PT-BR&location=São Paulo&programStartDate=2024-01-01&programEndDate=2024-12-31"))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(List.of(mockProgram))));
    }

    // 5. Resposta Vazia para Filtros Sem Correspondência
    @Test
    void testGetFiltered_withNoResults() throws Exception {
        when(service.getFiltered(Status.APPROVED, null, null, null, "Nonexistent Location", null, null)).thenReturn(Collections.emptyList());

        mockMvc.perform(get(baseEndpoint + "?status=APPROVED&location=Nonexistent Location"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    // 6. Tratamento de Exceções
    @Test
    void testServiceException() throws Exception {
        when(service.getFiltered(any(), any(), any(), any(), any(), any(), any())).thenThrow(new RuntimeException("Service error"));

        mockMvc.perform(get(baseEndpoint))
                .andExpect(status().isInternalServerError());
    }

    // 7. Validação de Parâmetros Inválidos
    @Test
    void testGetFiltered_withInvalidStatus() throws Exception {
        mockMvc.perform(get(baseEndpoint + "?status=INVALID"))
                .andExpect(status().isBadRequest());
    }

    // 8. Filtros Individuais (Testando filtros isolados)
    @Test
    void testGetFiltered_withLanguageFilter() throws Exception {
        ProgramResponseDTO mockProgram = ProgramResponseDTO.builder()
                .id(1L)
                .title("Program 1")
                .description("Description")
                .language("PT-BR")
                .status(Status.PENDING)
                .location("São Paulo")
                .tags(Collections.emptyList())
                .file("any")
                .programType(ProgramType.HIGHER)
                .feedback("any")
                .build();

        when(service.getFiltered(null, null, "PT-BR", null, null, null, null)).thenReturn(List.of(mockProgram));

        mockMvc.perform(get(baseEndpoint + "?language=PT-BR"))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(List.of(mockProgram))));
    }

    // 9. Ordenação por Data de Inscrição
    @Test
    void testGetFiltered_withSortByEnrollmentDate() throws Exception {
        ProgramResponseDTO mockProgram = ProgramResponseDTO.builder()
                .id(1L)
                .title("Program 1")
                .description("Description")
                .enrollmentInitialDate(new SimpleDateFormat("yyyy-MM-dd").parse("2024-01-01"))
                .status(Status.PENDING)
                .location("São Paulo")
                .tags(Collections.emptyList())
                .file("any")
                .programType(ProgramType.HIGHER)
                .feedback("any")
                .build();

        when(service.getFiltered(any(), any(), any(), any(), any(), any(), any())).thenReturn(List.of(mockProgram));

        mockMvc.perform(get(baseEndpoint + "?sortBy=enrollmentInitialDate"))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(List.of(mockProgram))));
    }

    // 10. Exclusão de Programas com Inscrição Passada
    @Test
    void testGetProgramsWithPastEnrollmentDate() throws Exception {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date pastDate = dateFormat.parse("2023-01-01");
        Date futureDate = dateFormat.parse("2025-01-01");

        ProgramResponseDTO pastProgram = ProgramResponseDTO.builder()
                .id(1L)
                .title("Past Program")
                .description("Expired Program")
                .enrollmentInitialDate(pastDate)
                .status(Status.PENDING)
                .location("Location A")
                .programType(ProgramType.HIGHER)
                .file("any")
                .feedback("any")
                .build();

        when(service.getFiltered(any(), any(), any(), any(), any(), any(), any())).thenReturn(List.of(pastProgram));

        mockMvc.perform(get(baseEndpoint + "?programStartDate=" + futureDate))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }
}