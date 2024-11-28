//package com.ages.informativoparaimigrantes.controller;
//
//
//import com.ages.informativoparaimigrantes.enums.InstitutionType;
//import com.ages.informativoparaimigrantes.enums.Status;
//import com.ages.informativoparaimigrantes.domain.Institution;
//import com.ages.informativoparaimigrantes.domain.UserData;
//import com.ages.informativoparaimigrantes.service.InstitutionServiceImpl;
//import com.ages.informativoparaimigrantes.security.TokenService;
//import com.ages.informativoparaimigrantes.service.UserServiceImpl;
//import com.ages.informativoparaimigrantes.repository.IUserRepository;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import java.util.List;
//
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//
//@ExtendWith(MockitoExtension.class)
//@WebMvcTest(InstitutionController.class)
//public class InstitutionControllerTest {
//
//    @MockBean
//    private InstitutionServiceImpl service;
//
//    @MockBean
//    private UserServiceImpl userServiceImpl;
//
//    @MockBean
//    private TokenService tokenService;
//
//    @MockBean
//    private IUserRepository userRepository;
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    private String baseEndpoint = "/institutions";
//
//    private UserData testUser = new UserData("adm1@email.com", "password", "ADMIN");
//
//    @Test
//    public void getInstitutionsWithoutIdShouldReturnAllInstitutions() throws Exception {
//        List<Institution> mockInstitutions = List.of(
//                new Institution("any","any","any", "BASIC","any","any","any","any", "PENDING","any"));
//
//        when(service.getAll()).thenReturn(mockInstitutions);
//        String token = "testToken";
//        when(tokenService.validateToken(token)).thenReturn(testUser.getUsername());
//        when(userRepository.findByEmail(testUser.getUsername())).thenReturn(testUser);
//
//        mockMvc.perform(get(baseEndpoint)
//                    .header("Authorization", "Bearer " + token))
//                .andExpect(status().isOk())
//                .andExpect(content().json(new ObjectMapper().writeValueAsString(mockInstitutions)));
//
//    }
//}
