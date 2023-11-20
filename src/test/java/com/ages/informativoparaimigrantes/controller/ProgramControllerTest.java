package com.ages.informativoparaimigrantes.controller;

import com.ages.informativoparaimigrantes.domain.Institution;
import com.ages.informativoparaimigrantes.domain.Program;
import com.ages.informativoparaimigrantes.dto.ProgramResponseDTO;
import com.ages.informativoparaimigrantes.enums.InstitutionType;
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

    @Test
    public void getProgramsShouldReturnProgramList() throws Exception {
        List<ProgramResponseDTO> mockPrograms = List.of(
                new ProgramResponseDTO(1L, "any", "any", "any", "any", null, null, null, null, Status.PENDING, "any", "any", null, "any", null, "any"));

        when(service.getFiltered(any(), any(), any(), any(), any(), any(), any())).thenReturn(mockPrograms);

        mockMvc.perform(get(baseEndpoint))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(mockPrograms)));
    }
}
