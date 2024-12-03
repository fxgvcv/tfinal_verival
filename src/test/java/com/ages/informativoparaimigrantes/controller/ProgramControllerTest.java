package com.ages.informativoparaimigrantes.controller;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.client.RestTemplate;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
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

//    @Test
//    public void getProgramsShouldReturnProgramList() throws Exception {
//        // Arrange: Criar programas fictícios para simulação
//        Program program1 = new Program(1L, "institution1@example.com", "Program 1", "Description 1", "http://link1.com", "English",
//                null, null, null, null, Status.APPROVED, "Location 1", null, null, ProgramType.HIGHER, null);
//        Program program2 = new Program(2L, "institution2@example.com", "Program 2", "Description 2", "http://link2.com", "Spanish",
//                null, null, null, null, Status.APPROVED, "Location 2", null, null, ProgramType.HIGHER, null);
//
//        // Criando uma lista de programas simulada (mocked response)
//        List<ProgramResponseDTO> mockPrograms = List.of(
//                ProgramResponseDTO.builder()
//                        .id(1L)
//                        .title("Program 1")
//                        .description("Description of Program 1")
//                        .link("http://program1.com")
//                        .language("EN")
//                        .programInitialDate(null)
//                        .programEndDate(null)
//                        .enrollmentInitialDate(null)
//                        .enrollmentEndDate(null)
//                        .status(Status.PENDING)
//                        .institutionEmail("institution1@domain.com")
//                        .location("Location 1")
//                        .tags(List.of(
//                                new Tag(1L, "Education", "EN"),
//                                new Tag(2L, "Work", "EN")
//                        ))
//                        .file("file1.pdf")
//                        .programType(ProgramType.HIGHER)
//                        .feedback("Feedback 1")
//                        .build(),
//                ProgramResponseDTO.builder()
//                        .id(2L)
//                        .title("Program 2")
//                        .description("Description of Program 2")
//                        .link("http://program2.com")
//                        .language("PT-BR")
//                        .programInitialDate(null)
//                        .programEndDate(null)
//                        .enrollmentInitialDate(null)
//                        .enrollmentEndDate(null)
//                        .status(Status.APPROVED)
//                        .institutionEmail("institution2@domain.com")
//                        .location("Location 2")
//                        .tags(List.of(
//                                new Tag(3L, "Healthcare", "PT-BR"),
//                                new Tag(4L, "Technology", "PT-BR")
//                        ))
//                        .file("file2.pdf")
//                        .programType(ProgramType.HIGHER)
//                        .feedback("Feedback 2")
//                        .build()
//        );
//
//        // Verificando se o método getFiltered foi chamado com qualquer parâmetro
//        Mockito.when(programService.getFiltered(
//                Mockito.eq(Status.APPROVED),  // Status esperado
//                Mockito.anyString(),          // Institution email
//                Mockito.eq(ProgramType.HIGHER), // Program type esperado
//                Mockito.anyString(),          // Language
//                Mockito.anyString(),          // Location
//                Mockito.any(),                // Open subscription
//                Mockito.anyString()           // Tags
//        )).thenReturn(mockPrograms);
//
//        // Imprime a lista de programas mockados para confirmar a correspondência com a resposta
//        System.out.println("Mock Programs retornados pelo getFiltered: " + objectMapper.writeValueAsString(mockPrograms));
//
//        // Convertendo a lista de programas para JSON para comparação
//        String jsonResponse = objectMapper.writeValueAsString(mockPrograms);
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/programs"))
//                .andExpect(MockMvcResultMatchers.status().isOk())  // Verifica se o status é 200 OK
//                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(Matchers.greaterThan(0))))  // Verifica se a lista retornada tem elementos
//                .andReturn();// Obtém o resultado da requisição
//    }


//    @Test
//    public void getProgramsShouldReturnProgramList() throws Exception {
//        // Criando uma lista de programas simulada (mocked response)
//        List<ProgramResponseDTO> mockPrograms = List.of(
//                ProgramResponseDTO.builder()
//                        .id(1L)
//                        .title("Program 1")
//                        .description("Description of Program 1")
//                        .link("http://program1.com")
//                        .language("EN")
//                        .programInitialDate(null)
//                        .programEndDate(null)
//                        .enrollmentInitialDate(null)
//                        .enrollmentEndDate(null)
//                        .status(Status.PENDING)
//                        .institutionEmail("institution1@domain.com")
//                        .location("Location 1")
//                        .tags(List.of(
//                                new Tag(1L, "Education", "EN"),
//                                new Tag(2L, "Work", "EN")
//                        ))
//                        .file("file1.pdf")
//                        .programType(ProgramType.HIGHER)
//                        .feedback("Feedback 1")
//                        .build(),
//                ProgramResponseDTO.builder()
//                        .id(2L)
//                        .title("Program 2")
//                        .description("Description of Program 2")
//                        .link("http://program2.com")
//                        .language("PT-BR")
//                        .programInitialDate(null)
//                        .programEndDate(null)
//                        .enrollmentInitialDate(null)
//                        .enrollmentEndDate(null)
//                        .status(Status.APPROVED)
//                        .institutionEmail("institution2@domain.com")
//                        .location("Location 2")
//                        .tags(List.of(
//                                new Tag(3L, "Healthcare", "PT-BR"),
//                                new Tag(4L, "Technology", "PT-BR")
//                        ))
//                        .file("file2.pdf")
//                        .programType(ProgramType.HIGHER)
//                        .feedback("Feedback 2")
//                        .build()
//        );
//
//        // Convertendo a lista de programas para JSON para comparação
//        String jsonResponse = objectMapper.writeValueAsString(mockPrograms);
//
//        // Fazendo a requisição com MockMvc e validando a resposta
//        mockMvc.perform(MockMvcRequestBuilders.get("/programs"))
//                .andExpect(MockMvcResultMatchers.status().isOk())  // Verifica se o status é 200 OK
//                .andExpect(MockMvcResultMatchers.content().json(jsonResponse));  // Verifica se o corpo da resposta é igual à lista mockada
//    }
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

//    // 6. Tratamento de Exceções
//    @Test
//    void testServiceException() throws Exception {
//        // Configura o mock para lançar uma RuntimeException quando o serviço for chamado
//        when(programService.getFiltered(any(), any(), any(), any(), any(), any(), any()))
//                .thenThrow(new RuntimeException("Service error"));
//
//        // Realiza a requisição GET, esperando que uma exceção ocorra e o status 500 seja retornado
//        mockMvc.perform(get("/programs"))
//                .andExpect(status().isInternalServerError())  // Espera-se um status 500
//                .andExpect(content().string("Service error"));  // Opcional: Verifica se a mensagem de erro é retornada
//    }

//    // 7. Validação de Parâmetros Inválidos
//    @Test
//    void testGetFiltered_withInvalidStatus() throws Exception {
//        mockMvc.perform(get("/programs/?status=INVALID"))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().string("Status inválido"));
//    }

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
//        when(service.getFiltered(any(), any(), any(), any(), any(), any(), any())).thenReturn(List.of(mockProgram));
//
//        mockMvc.perform(get(baseEndpoint + "?sortBy=enrollmentInitialDate"))
//                .andExpect(status().isOk())
//                .andExpect(content().json(new ObjectMapper().writeValueAsString(List.of(mockProgram))));
//    }
//
//    // 10. Exclusão de Programas com Inscrição Passada
//    @Test
//    void testGetProgramsWithPastEnrollmentDate() throws Exception {
//        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        Date pastDate = dateFormat.parse("2023-01-01");
//        Date futureDate = dateFormat.parse("2025-01-01");
//
//        ProgramResponseDTO pastProgram = ProgramResponseDTO.builder()
//                .id(1L)
//                .title("Past Program")
//                .description("Expired Program")
//                .enrollmentInitialDate(pastDate)
//                .status(Status.PENDING)
//                .location("Location A")
//                .programType(ProgramType.HIGHER)
//                .file("any")
//                .feedback("any")
//                .build();
//
//        when(service.getFiltered(any(), any(), any(), any(), any(), any(), any())).thenReturn(List.of(pastProgram));
//
//        mockMvc.perform(get(baseEndpoint + "?programStartDate=" + futureDate))
//                .andExpect(status().isOk())
//                .andExpect(content().json("[]"));
//    }
}