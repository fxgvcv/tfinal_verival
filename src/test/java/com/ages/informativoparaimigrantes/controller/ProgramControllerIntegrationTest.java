package com.ages.informativoparaimigrantes.controller;

import com.ages.informativoparaimigrantes.domain.Program;
import com.ages.informativoparaimigrantes.enums.ProgramType;
import com.ages.informativoparaimigrantes.enums.Status;
import com.ages.informativoparaimigrantes.repository.IProgramRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ProgramControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

//    @Autowired
//    private ProgramService programService;

    @Autowired
    private IProgramRepository programRepository; // Repositório real, se aplicável

    private Program program;

    @BeforeEach
    void setUp() throws ParseException {
        // Cria uma instância de Program antes de cada teste
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date programInitialDate = dateFormat.parse("2024-01-01");
        Date programEndDate = dateFormat.parse("2024-12-31");

        program = Program.builder()
                .title("Test Program")
                .description("Description of the test program")
                .link("https://link.com")
                .status(Status.APPROVED)
                .location("São Paulo")
                .programInitialDate(programInitialDate)
                .programEndDate(programEndDate)
                .programType(ProgramType.HIGHER)
                .feedback("Any feedback")
                .build();

        // Salva no banco de dados de teste
        programRepository.save(program);
    }

    @Test
    void testGetAllPrograms() throws Exception {
        // Testa se o controlador retorna todos os programas corretamente
        mockMvc.perform(get("/api/programs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Test Program"))
                .andExpect(jsonPath("$[0].location").value("São Paulo"));
    }

    @Test
    void testCreateProgram() throws Exception {
        // Testa a criação de um novo programa
        Program newProgram = Program.builder()
                .title("New Program")
                .description("Description of new program")
                .link("https://newlink.com")
                .status(Status.PENDING)
                .location("Rio de Janeiro")
                .programInitialDate(LocalDate.of(2024, 6, 1))
                .programEndDate(LocalDate.of(2024, 12, 31))
                .programType(ProgramType.HIGHER)
                .feedback("Feedback text")
                .build();

        mockMvc.perform(post("/api/programs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(newProgram)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("New Program"))
                .andExpect(jsonPath("$.location").value("Rio de Janeiro"));
    }

    @Test
    void testGetProgramById() throws Exception {
        // Testa se um programa específico é retornado corretamente pelo ID
        mockMvc.perform(get("/api/programs/{id}", program.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Program"))
                .andExpect(jsonPath("$.location").value("São Paulo"));
    }

    @Test
    void testUpdateProgram() throws Exception {
        // Testa a atualização de um programa existente
        program.setLocation("New Location");

        mockMvc.perform(put("/api/programs/{id}", program.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(program)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.location").value("New Location"));
    }

    @Test
    void testDeleteProgram() throws Exception {
        // Testa a exclusão de um programa
        mockMvc.perform(delete("/api/programs/{id}", program.getId()))
                .andExpect(status().isNoContent());

        // Verifica se o programa foi realmente deletado
        assertThat(programRepository.findById(program.getId())).isEmpty();
    }
}
