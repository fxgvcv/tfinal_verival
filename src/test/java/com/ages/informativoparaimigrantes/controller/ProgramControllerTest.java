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
public class ProgramControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private String baseEndpoint = "/program";

    @MockBean
    private ProgramServiceImpl service;

    // Testa o retorno de uma lista de programas
    @Test
    public void getProgramsShouldReturnProgramList() throws Exception {
        // Criação de uma lista mockada de ProgramResponseDTO
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

        // Mock do serviço
        when(service.getFiltered(any(), any(), any(), any(), any(), any(), any())).thenReturn(mockPrograms);

        // Executa a requisição e verifica o retorno
        mockMvc.perform(get(baseEndpoint))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(mockPrograms)));
    }

    // Testa comportamento sem filtros aplicados
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

    // Testa os filtros com status e localização
    @Test
    void testGetFiltered_withStatusAndLocationFilters() throws Exception {
        ProgramResponseDTO mockProgram = ProgramResponseDTO.builder()
                .id(1L)
                .title("Program 1")
                .description("Description")
                .link("https://link1.com")
                .language("any")
                .programInitialDate(null)
                .programEndDate(null)
                .enrollmentInitialDate(null)
                .enrollmentEndDate(null)
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

    // Testa múltiplos filtros aplicados ao mesmo tempo
    @Test
    void testGetFiltered_withMultipleFilters() throws Exception {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date programInitialDate = dateFormat.parse("2024-01-01");
        Date programEndDate = dateFormat.parse("2024-12-31");

        ProgramResponseDTO mockProgram = ProgramResponseDTO.builder()
                .id(1L)
                .title("Program 1")
                .description("Description")
                .link("https://link1.com")
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

    // Testa quando não há resultados para os filtros
    @Test
    void testGetFiltered_withNoResults() throws Exception {
        when(service.getFiltered(Status.APPROVED, null, null, null, "Nonexistent Location", null, null)).thenReturn(Collections.emptyList());

        mockMvc.perform(get(baseEndpoint + "?status=APPROVED&location=Nonexistent Location"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    // Testa comportamento de exceção do serviço
    @Test
    void testServiceException() throws Exception {
        when(service.getFiltered(any(), any(), any(), any(), any(), any(), any())).thenThrow(new RuntimeException("Service error"));

        mockMvc.perform(get(baseEndpoint))
                .andExpect(status().isInternalServerError());
    }

    // Testa comportamento com parâmetros inválidos
    @Test
    void testGetFiltered_withInvalidStatus() throws Exception {
        mockMvc.perform(get(baseEndpoint + "?status=INVALID"))
                .andExpect(status().isBadRequest());
    }

    // Teste com vários parametros
    @Test
    void testGetFiltered_withMultipleFilters_combined() throws Exception {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date programInitialDate = dateFormat.parse("2024-01-01");
        Date programEndDate = dateFormat.parse("2024-12-31");

        ProgramResponseDTO mockProgram = ProgramResponseDTO.builder()
                .id(1L)
                .title("Program 1")
                .description("Description")
                .link("https://link1.com")
                .programInitialDate(programInitialDate)
                .programEndDate(programEndDate)
                .status(Status.APPROVED)
                .location("São Paulo")
                .tags(Collections.emptyList())
                .file("any")
                .programType(ProgramType.HIGHER)
                .feedback("any")
                .build();

        when(service.getFiltered(Status.APPROVED, "Higher", "PT-BR", "São Paulo", programInitialDate, programEndDate)).thenReturn(List.of(mockProgram));

        mockMvc.perform(get(baseEndpoint + "?status=APPROVED&type=Higher&language=PT-BR&location=São Paulo&programStartDate=2024-01-01&programEndDate=2024-12-31"))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(List.of(mockProgram))));
    }

    // Teste com datas invalidas
    @Test
    void testGetFiltered_withInvalidDateFormat() throws Exception {
        mockMvc.perform(get(baseEndpoint + "?status=APPROVED&programStartDate=invalid-date&programEndDate=2024-12-31"))
                .andExpect(status().isBadRequest());
    }

    // Testes individuais

    // Filtro por linguagem
    @Test
    void testGetFiltered_withLanguageFilter() throws Exception {
        // Criando um programa mockado com linguagem "PT-BR"
        ProgramResponseDTO mockProgram = ProgramResponseDTO.builder()
                .id(1L)
                .title("Program 1")
                .description("Description")
                .link("https://link1.com")
                .language("PT-BR")
                .status(Status.PENDING)
                .location("São Paulo")
                .programType(ProgramType.HIGHER)
                .file("file1.pdf")
                .feedback("any")
                .build();

        when(service.getFiltered(null, "PT-BR", null, null, null, null, null))
                .thenReturn(List.of(mockProgram));

        mockMvc.perform(get(baseEndpoint + "?language=PT-BR"))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(List.of(mockProgram))));
    }

    // Filtro por status
    @Test
    void testGetFiltered_withStatusFilter() throws Exception {
        // Criando um programa mockado com status "APPROVED"
        ProgramResponseDTO mockProgram = ProgramResponseDTO.builder()
                .id(1L)
                .title("Program 1")
                .description("Description")
                .link("https://link1.com")
                .status(Status.APPROVED)
                .location("São Paulo")
                .programType(ProgramType.HIGHER)
                .file("file1.pdf")
                .feedback("any")
                .build();

        when(service.getFiltered(Status.APPROVED, null, null, null, null, null, null))
                .thenReturn(List.of(mockProgram));

        mockMvc.perform(get(baseEndpoint + "?status=APPROVED"))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(List.of(mockProgram))));
    }

    // Filtro por localização
    @Test
    void testGetFiltered_withLocationFilter() throws Exception {
        // Criando um programa mockado com localização "São Paulo"
        ProgramResponseDTO mockProgram = ProgramResponseDTO.builder()
                .id(1L)
                .title("Program 1")
                .description("Description")
                .link("https://link1.com")
                .status(Status.PENDING)
                .location("São Paulo")
                .programType(ProgramType.HIGHER)
                .file("file1.pdf")
                .feedback("any")
                .build();

        when(service.getFiltered(null, null, null, null, "São Paulo", null, null))
                .thenReturn(List.of(mockProgram));

        mockMvc.perform(get(baseEndpoint + "?location=São Paulo"))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(List.of(mockProgram))));
    }

    // Filtro por duração
    @Test
    void testGetFiltered_withDurationFilter() throws Exception {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date programInitialDate = dateFormat.parse("2024-01-01");
        Date programEndDate = dateFormat.parse("2024-12-31");

        // Criando um programa mockado com datas de início e fim
        ProgramResponseDTO mockProgram = ProgramResponseDTO.builder()
                .id(1L)
                .title("Program 1")
                .description("Description")
                .link("https://link1.com")
                .programInitialDate(programInitialDate)
                .programEndDate(programEndDate)
                .status(Status.PENDING)
                .location("São Paulo")
                .programType(ProgramType.HIGHER)
                .file("file1.pdf")
                .feedback("any")
                .build();

        when(service.getFiltered(null, null, null, null, null, programInitialDate, programEndDate))
                .thenReturn(List.of(mockProgram));

        mockMvc.perform(get(baseEndpoint + "?programStartDate=2024-01-01&programEndDate=2024-12-31"))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(List.of(mockProgram))));
    }

    // Testa a listagem de programas por tipo
    @Test
    void testGetFiltered_withProgramType() throws Exception {
        ProgramResponseDTO mockProgram = ProgramResponseDTO.builder()
                .id(1L)
                .title("Program 1")
                .description("Description")
                .link("https://link1.com")
                .programType(ProgramType.HIGHER)
                .status(Status.PENDING)
                .location("any")
                .tags(Collections.emptyList())
                .file("file1.pdf")
                .feedback("any")
                .build();

        when(service.getFiltered(null, null, "Higher", null, null, null, null)).thenReturn(List.of(mockProgram));

        mockMvc.perform(get(baseEndpoint + "?type=Higher"))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(List.of(mockProgram))));
    }
}