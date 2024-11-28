//package com.ages.informativoparaimigrantes.controller;
//
//import com.ages.informativoparaimigrantes.domain.Tag;
//import com.ages.informativoparaimigrantes.dto.ProgramResponseDTO;
//import com.ages.informativoparaimigrantes.enums.Status;
//import com.ages.informativoparaimigrantes.service.ProgramServiceImpl;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.text.DateFormat;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@ExtendWith(MockitoExtension.class)
//@WebMvcTest(ProgramController.class)
//public class ProgramControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    private String baseEndpoint = "/program";
//
//    @MockBean
//    private ProgramServiceImpl service;
//
//    // 1. Testa o retorno de uma lista de programas
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
//    // 2. Testa os filtros com status e localização
//    @Test
//    void testGetFiltered_withStatusAndLocationFilters() throws Exception {
//        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        Date programInitialDate = dateFormat.parse("2024-01-01");
//        Date programEndDate = dateFormat.parse("2024-12-31");
//        Date enrollmentInitialDate = dateFormat.parse("2023-12-01");
//        Date enrollmentEndDate = dateFormat.parse("2023-12-31");
//
//        ProgramResponseDTO mockProgram = new ProgramResponseDTO(1L, "Program 1", "Description", "email1@domain.com", "https://link1.com",
//                programInitialDate, programEndDate, enrollmentInitialDate, enrollmentEndDate, Status.APPROVED, "São Paulo", "Educação", null, "file1.pdf", null, "Program Type");
//
//        when(service.getFiltered(Status.APPROVED, null, null, null, "São Paulo", null, null)).thenReturn(List.of(mockProgram));
//
//        mockMvc.perform(get(baseEndpoint + "?status=APPROVED&location=São Paulo"))
//                .andExpect(status().isOk())
//                .andExpect(content().json(new ObjectMapper().writeValueAsString(List.of(mockProgram))));
//    }
//
//    // 3. Testa quando não há resultados para os filtros
//    @Test
//    void testGetFiltered_withNoResults() throws Exception {
//        when(service.getFiltered(Status.APPROVED, null, null, null, "Nonexistent Location", null, null)).thenReturn(List.of());
//
//        mockMvc.perform(get(baseEndpoint + "?status=APPROVED&location=Nonexistent Location"))
//                .andExpect(status().isOk())
//                .andExpect(content().json("[]"));
//    }
//
//    // 4. Testa comportamento de exceção do serviço
//    @Test
//    void testServiceException() throws Exception {
//        when(service.getFiltered(any(), any(), any(), any(), any(), any(), any())).thenThrow(new RuntimeException("Service error"));
//
//        mockMvc.perform(get(baseEndpoint))
//                .andExpect(status().isInternalServerError());
//    }
//
//    // 5. Testa comportamento com parâmetros inválidos
//    @Test
//    void testGetFiltered_withInvalidStatus() throws Exception {
//        mockMvc.perform(get(baseEndpoint + "?status=INVALID"))
//                .andExpect(status().isBadRequest());
//    }
//
//    // 6. Testa comportamento sem filtros aplicados
//    @Test
//    void testGetFiltered_withNoFilters() throws Exception {
//        List<ProgramResponseDTO> mockPrograms = List.of(
//                new ProgramResponseDTO(1L, "Program 1", "Description", "email1@domain.com", "https://link1.com", null, null, null, null, Status.PENDING, "São Paulo", "Educação", null, "file1.pdf", null, "Program Type"));
//
//        when(service.getFiltered(null, null, null, null, null, null, null)).thenReturn(mockPrograms);
//
//        mockMvc.perform(get(baseEndpoint))
//                .andExpect(status().isOk())
//                .andExpect(content().json(new ObjectMapper().writeValueAsString(mockPrograms)));
//    }
//
//    // 7. Testa múltiplos filtros aplicados ao mesmo tempo
//    @Test
//    void testGetFiltered_withMultipleFilters() throws Exception {
//        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        Date programInitialDate = dateFormat.parse("2024-01-01");
//        Date programEndDate = dateFormat.parse("2024-12-31");
//        Date enrollmentInitialDate = dateFormat.parse("2023-12-01");
//        Date enrollmentEndDate = dateFormat.parse("2023-12-31");
//
//        ProgramResponseDTO mockProgram = new ProgramResponseDTO(1L, "Program 1", "Description", "email1@domain.com", "https://link1.com",
//                programInitialDate, programEndDate, enrollmentInitialDate, enrollmentEndDate, Status.APPROVED, "São Paulo", "Educação", null, "file1.pdf", null, "Program Type");
//
//        when(service.getFiltered(Status.APPROVED, "Higher", "PT-BR", null, "São Paulo", programInitialDate, programEndDate)).thenReturn(List.of(mockProgram));
//
//        mockMvc.perform(get(baseEndpoint + "?status=APPROVED&type=Higher&language=PT-BR&location=São Paulo&programStartDate=2024-01-01&programEndDate=2024-12-31"))
//                .andExpect(status().isOk())
//                .andExpect(content().json(new ObjectMapper().writeValueAsString(List.of(mockProgram))));
//    }
//
//    // 8. Testa a listagem de programas por tipo
//    @Test
//    void testGetFiltered_withProgramType() throws Exception {
//        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        Date programInitialDate = dateFormat.parse("2024-01-01");
//        Date programEndDate = dateFormat.parse("2024-12-31");
//
//        ProgramResponseDTO mockProgram = new ProgramResponseDTO(1L, "Program 1", "Description", "email1@domain.com", "https://link1.com",
//                programInitialDate, programEndDate, null, null, Status.APPROVED, null, "Educação", null, "file1.pdf", null, "Higher Education");
//
//        when(service.getFiltered(null, null, "Higher", null, null, null, null)).thenReturn(List.of(mockProgram));
//
//        mockMvc.perform(get(baseEndpoint + "?type=Higher"))
//                .andExpect(status().isOk())
//                .andExpect(content().json(new ObjectMapper().writeValueAsString(List.of(mockProgram))));
//    }
//
//    // 1. Testa filtragem apenas por Status
//    @Test
//    void testGetFiltered_withStatusOnly() throws Exception {
//        ProgramResponseDTO mockProgram = new ProgramResponseDTO(1L, "Program 1", "Description", "email1@domain.com", "https://link1.com",
//                null, null, null, null, Status.APPROVED, "São Paulo", "Educação", null, "file1.pdf", null, "Program Type");
//
//        when(service.getFiltered(Status.APPROVED, null, null, null, null, null, null)).thenReturn(List.of(mockProgram));
//
//        mockMvc.perform(get(baseEndpoint + "?status=APPROVED"))
//                .andExpect(status().isOk())
//                .andExpect(content().json(new ObjectMapper().writeValueAsString(List.of(mockProgram))));
//    }
//
//    // 2. Testa filtragem apenas por Localização
//    @Test
//    void testGetFiltered_withLocationOnly() throws Exception {
//        ProgramResponseDTO mockProgram = new ProgramResponseDTO(1L, "Program 1", "Description", "email1@domain.com", "https://link1.com",
//                null, null, null, null, Status.PENDING, "São Paulo", "Educação", null, "file1.pdf", null, "Program Type");
//
//        when(service.getFiltered(null, null, null, null, "São Paulo", null, null)).thenReturn(List.of(mockProgram));
//
//        mockMvc.perform(get(baseEndpoint + "?location=São Paulo"))
//                .andExpect(status().isOk())
//                .andExpect(content().json(new ObjectMapper().writeValueAsString(List.of(mockProgram))));
//    }
//
//    // 3. Testa filtragem apenas por Tipo de Programa
//    @Test
//    void testGetFiltered_withProgramTypeOnly() throws Exception {
//        ProgramResponseDTO mockProgram = new ProgramResponseDTO(1L, "Program 1", "Description", "email1@domain.com", "https://link1.com",
//                null, null, null, null, Status.PENDING, null, "Educação", null, "file1.pdf", null, "Program Type");
//
//        when(service.getFiltered(null, null, "Educação", null, null, null, null)).thenReturn(List.of(mockProgram));
//
//        mockMvc.perform(get(baseEndpoint + "?type=Educação"))
//                .andExpect(status().isOk())
//                .andExpect(content().json(new ObjectMapper().writeValueAsString(List.of(mockProgram))));
//    }
//
//    // 4. Testa filtragem apenas por Data de Início
//    @Test
//    void testGetFiltered_withProgramStartDateOnly() throws Exception {
//        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        Date programStartDate = dateFormat.parse("2024-01-01");
//
//        ProgramResponseDTO mockProgram = new ProgramResponseDTO(1L, "Program 1", "Description", "email1@domain.com", "https://link1.com",
//                programStartDate, null, null, null, Status.PENDING, null, "Educação", null, "file1.pdf", null, "Program Type");
//
//        when(service.getFiltered(null, null, null, programStartDate, null, null, null)).thenReturn(List.of(mockProgram));
//
//        mockMvc.perform(get(baseEndpoint + "?programStartDate=2024-01-01"))
//                .andExpect(status().isOk())
//                .andExpect(content().json(new ObjectMapper().writeValueAsString(List.of(mockProgram))));
//    }
//
//    // 5. Testa filtragem apenas por Data de Término
//    @Test
//    void testGetFiltered_withProgramEndDateOnly() throws Exception {
//        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        Date programEndDate = dateFormat.parse("2024-12-31");
//
//        ProgramResponseDTO mockProgram = new ProgramResponseDTO(1L, "Program 1", "Description", "email1@domain.com", "https://link1.com",
//                null, programEndDate, null, null, Status.PENDING, null, "Educação", null, "file1.pdf", null, "Program Type");
//
//        when(service.getFiltered(null, null, null, null, null, programEndDate, null)).thenReturn(List.of(mockProgram));
//
//        mockMvc.perform(get(baseEndpoint + "?programEndDate=2024-12-31"))
//                .andExpect(status().isOk())
//                .andExpect(content().json(new ObjectMapper().writeValueAsString(List.of(mockProgram))));
//    }
//
//}