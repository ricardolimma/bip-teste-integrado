package com.example.backend.controller;

import com.example.backend.service.impl.BeneficioServiceImpl;
import com.example.ejb.entity.Beneficio;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BeneficioController.class)
class BeneficioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BeneficioServiceImpl beneficioServiceImpl;

    @Autowired
    private ObjectMapper objectMapper;

    private Beneficio beneficio1;
    private Beneficio beneficio2;

    @BeforeEach
    void setUp() {
        beneficio1 = new Beneficio();
        beneficio1.setId(1L);

        beneficio2 = new Beneficio();
        beneficio2.setId(2L);
    }

    @Test
    void testCreate() throws Exception {
        when(beneficioServiceImpl.create(any(Beneficio.class))).thenReturn(beneficio1);

        mockMvc.perform(post("/api/v1/beneficios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beneficio1)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/api/v1/beneficios/1"))
                .andExpect(jsonPath("$.id").value(1L));

        verify(beneficioServiceImpl, times(1)).create(any(Beneficio.class));
    }

    @Test
    void testFindAll() throws Exception {
        List<Beneficio> beneficios = Arrays.asList(beneficio1, beneficio2);
        when(beneficioServiceImpl.findAll()).thenReturn(beneficios);

        mockMvc.perform(get("/api/v1/beneficios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[1].id").value(2L));

        verify(beneficioServiceImpl, times(1)).findAll();
    }

    @Test
    void testFindById() throws Exception {
        when(beneficioServiceImpl.findById(1L)).thenReturn(beneficio1);

        mockMvc.perform(get("/api/v1/beneficios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        verify(beneficioServiceImpl, times(1)).findById(1L);
    }

    @Test
    void testUpdate() throws Exception {
        beneficio1.setId(1L);
        when(beneficioServiceImpl.update(any(Beneficio.class), eq(1L))).thenReturn(beneficio1);

        mockMvc.perform(put("/api/v1/beneficios/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beneficio1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        verify(beneficioServiceImpl, times(1)).update(any(Beneficio.class), eq(1L));
    }

    @Test
    void testDelete() throws Exception {
        doNothing().when(beneficioServiceImpl).delete(1L);

        mockMvc.perform(delete("/api/v1/beneficios/1"))
                .andExpect(status().isNoContent());

        verify(beneficioServiceImpl, times(1)).delete(1L);
    }
}
