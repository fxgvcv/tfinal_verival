package com.ages.informativoparaimigrantes.controller;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ages.informativoparaimigrantes.domain.Program;
import com.ages.informativoparaimigrantes.domain.Tag;
import com.ages.informativoparaimigrantes.dto.ProgramResponseDTO;
import com.ages.informativoparaimigrantes.enums.ProgramType;
import com.ages.informativoparaimigrantes.enums.Status;
import com.ages.informativoparaimigrantes.repository.IUserRepository;
import com.ages.informativoparaimigrantes.service.ProgramServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.client.RestTemplate;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc// Para inicializar o servidor com uma porta aleatória
public class ProgramControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProgramServiceImpl programService;

    @Autowired
    private ObjectMapper objectMapper;  // Para conversão de objetos para JSON

    @Test
    public void getProgramsShouldReturnProgramList() throws Exception {
        // Faz a requisição GET e obtém a resposta
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/programs"))
                .andExpect(MockMvcResultMatchers.status().isOk())  // Verifica se o status é 200 OK
                .andExpect(MockMvcResultMatchers.jsonPath("$").exists()) // Verifica se o corpo contém dados
                .andReturn();  // Obtém o resultado da requisição

        // Imprime o conteúdo da resposta no console
        System.out.println("Resposta da requisição: " + result.getResponse().getContentAsString());
    }

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

        when(programService.getFiltered(null, null, null, null, null, null, null)).thenReturn(mockPrograms);

        // Realiza a requisição e captura o resultado
        MvcResult result = mockMvc.perform(get("/programs"))
                .andExpect(status().isOk())  // Verifica se o status é 200 OK
                .andReturn();  // Captura o resultado da requisição

        // Obtém o corpo da resposta como uma string
        String responseContent = result.getResponse().getContentAsString();

        // Imprime o conteúdo da resposta no console
        System.out.println("Esse é o retorno da requisição: " + responseContent);

        // Verifica se o conteúdo da resposta é o esperado
        mockMvc.perform(get("/programs"))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(mockPrograms)));
    }

//  4 - Com vários filtros
    @Test
    void testGetFiltered_withMultipleFilters() throws Exception {
        // Cria uma lista de programas simulados
        List<ProgramResponseDTO> mockPrograms = List.of(
                ProgramResponseDTO.builder()
                        .id(1L)
                        .title("Program 1")
                        .description("Program focused on AI.")
                        .programInitialDate(null)
                        .programEndDate(null)
                        .status(Status.APPROVED)
                        .location("São Paulo")
                        .tags(Collections.emptyList()) // Sem tags
                        .file("file1.pdf")
                        .programType(ProgramType.HIGHER)
                        .feedback("Excellent program!")
                        .build(),

                ProgramResponseDTO.builder()
                        .id(2L)
                        .title("Program 2")
                        .description("Short course in web development.")
                        .programInitialDate(null)
                        .programEndDate(null)
                        .status(Status.REJECTED)
                        .location("Rio de Janeiro")
                        .tags(Collections.emptyList())
                        .file("file2.pdf")
                        .programType(ProgramType.HIGHER)
                        .feedback("Well-structured course.")
                        .build(),

                ProgramResponseDTO.builder()
                        .id(3L)
                        .title("Program 3")
                        .description("Full-stack bootcamp.")
                        .programInitialDate(null)
                        .programEndDate(null)
                        .status(Status.APPROVED)
                        .location("Belo Horizonte")
                        .tags(Collections.emptyList())
                        .file("file3.pdf")
                        .programType(ProgramType.HIGHER)
                        .feedback("Great experience!")
                        .build()
        );

        // Mock do comportamento do serviço
        when(programService.getFiltered(Status.APPROVED, null, ProgramType.HIGHER, null, "São Paulo", true, null))
                .thenReturn(List.of(mockPrograms.get(0))); // Apenas o primeiro programa corresponde

        // Realiza a requisição com vários parâmetros
        MvcResult result = mockMvc.perform(get("/programs")
                        .param("status", "APPROVED")
                        .param("type", "HIGHER")
                        .param("location", "São Paulo")
                        .param("openSubscription", "true"))
                .andExpect(status().isOk())  // Espera-se um status 200 OK
                .andReturn();                // Captura o resultado da requisição

        // Imprime o status HTTP da resposta
        System.out.println("HTTP Status: " + result.getResponse().getStatus());

        // Obtém o corpo da resposta como uma string
        String rawResponseContent = result.getResponse().getContentAsString();
        System.out.println("Conteúdo da Resposta (Raw): " + rawResponseContent);

        // Converte o conteúdo da resposta para um objeto JSON para inspeção detalhada
        ObjectMapper objectMapper = new ObjectMapper();
        List<ProgramResponseDTO> responsePrograms = objectMapper.readValue(
                rawResponseContent,
                new TypeReference<List<ProgramResponseDTO>>() {}
        );

        // Imprime cada programa retornado
        System.out.println("Programas Retornados:");
        for (ProgramResponseDTO program : responsePrograms) {
            System.out.println(program);
        }

        // Verifica o resultado final com os parâmetros
        mockMvc.perform(get("/programs")
                        .param("status", "APPROVED")
                        .param("type", "HIGHER")
                        .param("location", "São Paulo")
                        .param("openSubscription", "true"))
                .andExpect(status().isOk())  // Verifica se o status é 200
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(mockPrograms.get(0))))); // Verifica o conteúdo
    }

    // 3. Filtros Aplicados por Status e Localização
    @Test
    void testGetFiltered_withStatusAndLocationFilters() throws Exception {
        // Cria uma lista de programas simulados
        List<ProgramResponseDTO> mockPrograms = List.of(
                ProgramResponseDTO.builder()
                        .id(1L)
                        .title("Program 1")
                        .description("A higher education program focused on technology and innovation.")
                        .status(Status.APPROVED)
                        .location("São Paulo")
                        .tags(Collections.emptyList())  // Não há tags definidas
                        .file("file1.pdf")
                        .programType(ProgramType.HIGHER)
                        .feedback("Feedback from participants: Excellent learning experience!")
                        .build(),

                ProgramResponseDTO.builder()
                        .id(2L)
                        .title("Program 2")
                        .description("A short course on digital marketing strategies.")
                        .status(Status.APPROVED)
                        .location("Rio de Janeiro")
                        .tags(Collections.emptyList())  // Não há tags definidas
                        .file("file2.pdf")
                        .programType(ProgramType.HIGHER)
                        .feedback("Participants found the course very insightful and practical.")
                        .build(),

                ProgramResponseDTO.builder()
                        .id(3L)
                        .title("Program 3")
                        .description("An intensive bootcamp for full-stack web development.")
                        .status(Status.APPROVED)
                        .location("Belo Horizonte")
                        .tags(Collections.emptyList())  // Não há tags definidas
                        .file("file3.pdf")
                        .programType(ProgramType.HIGHER)
                        .feedback("Great course! It equipped me with the skills to enter the tech industry.")
                        .build()
        );

        // Mock do comportamento do serviço
        when(programService.getFiltered(Status.APPROVED, null, null, null, "São Paulo", null, null))
                .thenReturn(mockPrograms);

        // Realiza a requisição GET com os parâmetros na URL
        MvcResult result = mockMvc.perform(get("/programs")
                        .param("status", "APPROVED")
                        .param("location", "São Paulo"))
                .andExpect(status().isOk())  // Verifica se o status é 200 OK
                .andReturn();  // Captura o resultado da requisição

        // Obtém o corpo da resposta como uma string
        String responseContent = result.getResponse().getContentAsString();

        // Imprime o conteúdo da resposta no console
        System.out.println("Esse é o retorno da requisição por status e localizacao: " + responseContent);

        // Verifica se o conteúdo da resposta é o esperado
        mockMvc.perform(get("/programs")
                        .param("status", "APPROVED")
                        .param("location", "São Paulo"))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(mockPrograms)));
    }

// 4 - Teste com vários filtros


    // 5. Resposta Vazia para Filtros Sem Correspondência
    @Test
    void testGetFiltered_withNoResults() throws Exception {
        // Mock do comportamento do serviço, retornando uma lista vazia para o filtro com localização inexistente
        when(programService.getFiltered(Status.APPROVED, null, null, null, "Nonexistent Location", null, null))
                .thenReturn(Collections.emptyList());

        // Realiza a requisição GET com os filtros que não devem retornar resultados
        mockMvc.perform(get("/programs")  // Certifique-se de que baseEndpoint está corretamente definido
                        .param("status", "APPROVED")
                        .param("location", "Nonexistent Location"))
                .andExpect(status().isOk())  // Espera-se um status 200 OK
                .andExpect(content().json("[]"));  // Espera-se uma resposta vazia (lista vazia)
    }

    // 8. Filtros Individuais (Testando filtros isolados)
    @Test
    void testGetFiltered_withLanguageFilter() throws Exception {
        // Criando um programa simulado com o idioma PT-BR
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

        // Mock do serviço para retornar o programa simulado quando o filtro "language" for "PT-BR"
        when(programService.getFiltered(null, null, null, "PT-BR", null, null, null)).thenReturn(List.of(mockProgram));

        // Realizando a requisição GET com o parâmetro "language=PT-BR"
        mockMvc.perform(get("/programs")  // Certifique-se de que o endpoint esteja correto
                        .param("language", "PT-BR"))  // Passando o filtro "language"
                .andExpect(status().isOk())  // Espera-se um status 200 OK
                .andExpect(content().json(new ObjectMapper().writeValueAsString(List.of(mockProgram))));  // Espera-se que o conteúdo da resposta seja igual ao mockProgram
    }

        // 10. Exclusão de Programas com Inscrição Passada
    @Test
    void testGetProgramsWithPastEnrollmentDate() throws Exception {
        // Definir datas
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date pastDate = dateFormat.parse("2023-01-01"); // Data no passado
        Date futureDate = dateFormat.parse("2025-01-01"); // Data futura (programa ativo)
        Date anotherFutureDate = dateFormat.parse("2026-01-01"); // Outra data futura

        // Criando programas mockados
        ProgramResponseDTO pastProgram = ProgramResponseDTO.builder()
                .id(1L)
                .title("Past Program")
                .description("Expired Program")
                .enrollmentInitialDate(pastDate)
                .enrollmentEndDate(pastDate)  // Data de término no passado
                .status(Status.APPROVED)
                .location("Location A")
                .programType(ProgramType.HIGHER)
                .file("any")
                .feedback("any")
                .build();

        ProgramResponseDTO activeProgram = ProgramResponseDTO.builder()
                .id(2L)
                .title("Active Program")
                .description("Active Program")
                .enrollmentInitialDate(futureDate)
                .enrollmentEndDate(futureDate)  // Data de término no futuro
                .status(Status.APPROVED)
                .location("Location B")
                .programType(ProgramType.HIGHER)
                .file("any")
                .feedback("any")
                .build();

        ProgramResponseDTO anotherActiveProgram = ProgramResponseDTO.builder()
                .id(3L)
                .title("Another Active Program")
                .description("Another Active Program")
                .enrollmentInitialDate(anotherFutureDate)
                .enrollmentEndDate(anotherFutureDate)  // Data de término no futuro
                .status(Status.APPROVED)
                .location("Location C")
                .programType(ProgramType.HIGHER)
                .file("any")
                .feedback("any")
                .build();

        // Simulando o comportamento do serviço para retornar os programas filtrados
        when(programService.getFiltered(any(), any(), any(), any(), any(), eq(true), any()))
                .thenReturn(List.of(activeProgram, anotherActiveProgram));

        // Realizando a chamada do MockMvc para testar a API
        MvcResult result = mockMvc.perform(get("/programs") // Base endpoint da API
                        .param("status", Status.APPROVED.name())
                        .param("language", "English")
                        .param("openSubscription", "true")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Verifica status HTTP 200
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath("$.length()").value(2))  // Espera dois programas ativos
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", equalTo(2)))  // Verifica o ID do primeiro programa como Long
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id", equalTo(3)))
                .andReturn();

        // Imprime o conteúdo da resposta
        System.out.println("Response: " + result.getResponse().getContentAsString());

        // Certificando que o programa passado com data expirada não foi retornado
        verify(programService).getFiltered(any(), any(), any(), eq("English"), any(), eq(true), any());
    }
}