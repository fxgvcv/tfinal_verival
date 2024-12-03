package com.ages.informativoparaimigrantes.controller;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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

import static org.hamcrest.Matchers.hasSize;
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
        System.out.println("Esse é o retorno da requisição: " + responseContent);

        // Verifica se o conteúdo da resposta é o esperado
        mockMvc.perform(get("/programs")
                        .param("status", "APPROVED")
                        .param("location", "São Paulo"))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(mockPrograms)));
    }

//
////  4. Múltiplos Filtros Aplicados
//    @Test
//    void testGetFiltered_withMultipleFilters() throws Exception {
//
//        ProgramResponseDTO mockProgram = ProgramResponseDTO.builder()
//                .id(1L)
//                .title("Program 1")
//                .description("Description")
//                .programInitialDate(null)
//                .programEndDate(null)
//                .status(Status.APPROVED)
//                .location("São Paulo")
//                .tags(Collections.emptyList()) // Se houver tags, ajuste aqui
//                .file("any")
//                .programType(ProgramType.HIGHER)
//                .feedback("any")
//                .build();
//
//        // Mock do comportamento do serviço
//        when(programService.getFiltered(Status.APPROVED, null, ProgramType.HIGHER, null, "Porto Alegre", true, null))
//                .thenReturn(List.of(mockProgram));
//
//        // Realiza a requisição com todos os parâmetros corretamente passados
//        mockMvc.perform(get("/programs")  // Ajuste a URL para o caminho correto da sua API
//                        .param("status", "APPROVED")  // Passando 'APPROVED' para o status
//                        .param("type", "HIGHER")      // Passando 'HIGHER' para o type (enum)
//                        .param("institutionEmail", "email.com")      // Passando 'HIGHER' para o type (enum)
//                        .param("language", "PT-BR")   // Passando 'PT-BR' para o language
//                        .param("location", "São Paulo") // Passando 'São Paulo' para a location// Passando data de fim
//                        .param("openSubscription", "true"))  // Se esse parâmetro for necessário, forneça um valor (como 'true' ou 'false')
//                .andExpect(status().isOk())  // Espera-se um status 200 OK
//                .andExpect(content().json(new ObjectMapper().writeValueAsString(List.of(mockProgram)))); // Espera-se o retorno do mockProgram
//    }


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


//    // 9. Ordenação por Data de Inscrição
//    @Test
//    void testGetFiltered_withSortByEnrollmentDate() throws Exception {
//        // Criando o programa simulado com uma data de inscrição específica
//        ProgramResponseDTO mockProgram = ProgramResponseDTO.builder()
//                .id(1L)
//                .title("Program 1")
//                .description("Description")
//                .enrollmentInitialDate(new SimpleDateFormat("yyyy-MM-dd").parse("2024-01-01"))
//                .status(Status.PENDING)
//                .location("São Paulo")
//                .tags(Collections.emptyList())
//                .file("any")
//                .programType(ProgramType.HIGHER)
//                .feedback("any")
//                .build();
//
//        // Mock do serviço retornando o programa simulado
//        when(programService.getFiltered(any(), any(), any(), any(), any(), any(), any()))
//                .thenReturn(List.of(mockProgram));  // Retorna a lista com o mockProgram
//
//        // Realizando a requisição GET com o parâmetro "sortBy=enrollmentInitialDate"
//        mockMvc.perform(get("/programs")  // Certifique-se de que o "baseEndpoint" esteja correto
//                .andExpect(status().isOk())  // Espera-se um status 200 OK
//                .andExpect(content().json(new ObjectMapper().writeValueAsString(List.of(mockProgram))));  // Espera-se que o conteúdo seja o programa simulado
//    }
//
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
            .status(Status.PENDING)
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
            .status(Status.PENDING)
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
            .status(Status.PENDING)
            .location("Location C")
            .programType(ProgramType.HIGHER)
            .file("any")
            .feedback("any")
            .build();

    // Simulando o comportamento do serviço para retornar os programas
    when(programService.getFiltered(any(), any(), any(), any(), any(), any(), any()))
            .thenReturn(List.of(pastProgram, activeProgram, anotherActiveProgram));

    // Realizando a chamada do MockMvc para testar// Veri
    mockMvc.perform(get("/programs") // Base endpoint da API
                    .param("status", "APPROVED") // Status
                    .param("openSubscription", "true") // Apenas inscrições abertas
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk()) // Verifica status HTTP 200
            .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath("$.length()").value(2)); // Verifica o número de elementos diretamente
    }
}