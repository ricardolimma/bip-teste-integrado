package com.example.backend.service.impl;

import com.example.backend.service.BeneficioService;
import com.example.ejb.entity.Beneficio;
import com.example.ejb.service.BeneficioEjbService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BeneficioServiceImplTest {

    @Mock
    private BeneficioEjbService ejb;

    @InjectMocks
    private BeneficioServiceImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreate() {
        Beneficio beneficio = new Beneficio();
        beneficio.setId(1L);

        when(ejb.create(beneficio)).thenReturn(beneficio);

        Beneficio result = service.create(beneficio);
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(ejb, times(1)).create(beneficio);
    }

    @Test
    void testFindById() {
        Beneficio beneficio = new Beneficio();
        beneficio.setId(2L);

        when(ejb.findById(2L)).thenReturn(beneficio);

        Beneficio result = service.findById(2L);
        assertNotNull(result);
        assertEquals(2L, result.getId());
        verify(ejb, times(1)).findById(2L);
    }

    @Test
    void testFindAll() {
        Beneficio b1 = new Beneficio();
        b1.setId(1L);
        Beneficio b2 = new Beneficio();
        b2.setId(2L);

        List<Beneficio> beneficios = Arrays.asList(b1, b2);

        when(ejb.findAll()).thenReturn(beneficios);

        List<Beneficio> result = service.findAll();
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(ejb, times(1)).findAll();
    }

    @Test
    void testUpdate() {
        Beneficio beneficio = new Beneficio();
        beneficio.setId(3L);

        when(ejb.update(3L, beneficio)).thenReturn(beneficio);

        Beneficio result = service.update(beneficio, 3L);
        assertNotNull(result);
        assertEquals(3L, result.getId());
        verify(ejb, times(1)).update(3L, beneficio);
    }

    @Test
    void testDelete() {
        doNothing().when(ejb).delete(4L);

        service.delete(4L);

        verify(ejb, times(1)).delete(4L);
    }
}
