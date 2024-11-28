//package com.ages.informativoparaimigrantes.controller;
//
//import com.ages.informativoparaimigrantes.domain.Immigrant;
//import com.ages.informativoparaimigrantes.service.ImmigrantServiceImpl;
//import com.ages.informativoparaimigrantes.service.UserServiceImpl;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.web.servlet.MockMvc;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import java.util.Collections;
//import java.util.List;
//
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//import java.util.Optional;
//
//@ExtendWith(MockitoExtension.class)
//@WebMvcTest(ImmigrantController.class)
//public class ImmigrantControllerTest {
//
//    private final String baseEndpoint = "/immigrants";
//
//    @MockBean
//    private ImmigrantServiceImpl immigrantServiceImpl;
//
//    @MockBean
//    private UserServiceImpl userServiceImpl;
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    private final ObjectMapper objectMapper = new ObjectMapper();
//
//    private final Immigrant MOCK_IMMIGRANT1 = new Immigrant("email1", "name1", "country1");
//
//    private final Immigrant MOCK_IMMIGRANT2 = new Immigrant("email2", "name2", "country2");
//
//    @Test
//    public void getAllShouldReturnList() throws Exception {
//        List<Immigrant> immigrantList = List.of(MOCK_IMMIGRANT1, MOCK_IMMIGRANT2);
//
//        when(immigrantServiceImpl.getAll()).thenReturn(immigrantList);
//
//        mockMvc.perform(get(baseEndpoint))
//                .andExpect(status().isOk())
//                .andExpect(content().json(objectMapper.writeValueAsString(immigrantList)));
//    }
//
//    @Test
//    public void getAllShouldReturnEmptyListAnd200() throws Exception {
//        List<Immigrant> immigrantList = Collections.emptyList();
//
//        when(immigrantServiceImpl.getAll()).thenReturn(immigrantList);
//
//        mockMvc.perform(get(baseEndpoint))
//                .andExpect(status().isOk())
//                .andExpect(content().json("[]"));
//    }
//
//    @Test
//    public void getByIdShouldReturnList() throws Exception {
//        Optional<Immigrant> immigrantOptional = Optional.of(MOCK_IMMIGRANT1);
//
//        when(immigrantServiceImpl.getByEmail("email")).thenReturn(immigrantOptional);
//
//        mockMvc.perform(get(baseEndpoint + "/email"))
//                .andExpect(status().isOk())
//                .andExpect(content().json(objectMapper.writeValueAsString(MOCK_IMMIGRANT1)));
//    }
//
//    @Test
//    public void getByIdShouldReturn404() throws Exception {
//        when(immigrantServiceImpl.getByEmail("email")).thenReturn(Optional.empty());
//
//        mockMvc.perform(get(baseEndpoint + "/email"))
//                .andExpect(status().isNotFound());
//    }
//}
