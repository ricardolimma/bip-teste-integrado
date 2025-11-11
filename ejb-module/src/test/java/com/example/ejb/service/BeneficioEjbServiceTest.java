package com.example.ejb.service;

import com.example.ejb.entity.Beneficio;
import com.example.ejb.exception.TransferExceptions.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.OptimisticLockException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BeneficioEjbServiceTest {

    @Mock
    private EntityManager em;

    @InjectMocks
    private BeneficioEjbService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateSuccess() {
        Beneficio b = new Beneficio();
        b.setNome("Vale Alimentação");
        b.setValor(BigDecimal.valueOf(100));

        Beneficio result = service.create(b);

        assertNotNull(result);
        verify(em, times(1)).persist(b);
        verify(em, times(1)).flush();
    }

    @Test
    void testCreateInvalidNome() {
        Beneficio b = new Beneficio();
        b.setNome("   ");
        b.setValor(BigDecimal.valueOf(100));

        assertThrows(IllegalArgumentException.class, () -> service.create(b));
    }

    @Test
    void testFindByIdSuccess() {
        Beneficio b = new Beneficio();
        b.setId(1L);
        when(em.find(Beneficio.class, 1L)).thenReturn(b);

        Beneficio result = service.findById(1L);

        assertEquals(1L, result.getId());
        verify(em, times(1)).find(Beneficio.class, 1L);
    }

    @Test
    void testFindByIdNotFound() {
        when(em.find(Beneficio.class, 1L)).thenReturn(null);

        assertThrows(EntityNotFoundException.class, () -> service.findById(1L));
    }

    @Test
    void testFindAll() {
        Beneficio b1 = new Beneficio();
        b1.setId(1L);
        Beneficio b2 = new Beneficio();
        b2.setId(2L);

        TypedQuery<Beneficio> query = mock(TypedQuery.class);
        when(em.createQuery("SELECT b FROM Beneficio b", Beneficio.class)).thenReturn(query);
        when(query.getResultList()).thenReturn(Arrays.asList(b1, b2));

        List<Beneficio> result = service.findAll();

        assertEquals(2, result.size());
        verify(em, times(1)).createQuery("SELECT b FROM Beneficio b", Beneficio.class);
        verify(query, times(1)).getResultList();
    }

    @Test
    void testUpdateSuccess() {
        Beneficio existing = new Beneficio();
        existing.setId(1L);
        existing.setNome("Antigo");

        Beneficio updated = new Beneficio();
        updated.setNome("Novo");
        updated.setDescricao("Descrição");
        updated.setValor(BigDecimal.valueOf(200));
        updated.setAtivo(true);

        when(em.find(Beneficio.class, 1L)).thenReturn(existing);
        when(em.merge(existing)).thenReturn(existing);

        Beneficio result = service.update(1L, updated);

        assertEquals("Novo", result.getNome());
        assertEquals("Descrição", result.getDescricao());
        assertEquals(BigDecimal.valueOf(200), result.getValor());
        assertTrue(result.getAtivo());
        verify(em, times(1)).find(Beneficio.class, 1L);
        verify(em, times(1)).merge(existing);
    }

    @Test
    void testUpdateNotFound() {
        when(em.find(Beneficio.class, 1L)).thenReturn(null);
        assertThrows(EntityNotFoundException.class, () -> service.update(1L, new Beneficio()));
    }

    @Test
    void testDeleteSuccess() {
        Beneficio b = new Beneficio();
        when(em.find(Beneficio.class, 1L)).thenReturn(b);

        service.delete(1L);

        verify(em, times(1)).remove(b);
    }

    @Test
    void testDeleteNotFound() {
        when(em.find(Beneficio.class, 1L)).thenReturn(null);
        assertThrows(EntityNotFoundException.class, () -> service.delete(1L));
    }

    @Test
    void testTransferSuccess() {
        Beneficio from = new Beneficio();
        from.setId(1L);
        from.setValor(BigDecimal.valueOf(500));

        Beneficio to = new Beneficio();
        to.setId(2L);
        to.setValor(BigDecimal.valueOf(100));

        when(em.find(Beneficio.class, 1L)).thenReturn(from);
        when(em.find(Beneficio.class, 2L)).thenReturn(to);

        service.transfer(1L, 2L, BigDecimal.valueOf(200));

        assertEquals(BigDecimal.valueOf(300), from.getValor());
        assertEquals(BigDecimal.valueOf(300), to.getValor());
        verify(em, times(1)).flush();
    }

    @Test
    void testTransferInsufficientFunds() {
        Beneficio from = new Beneficio();
        from.setId(1L);
        from.setValor(BigDecimal.valueOf(100));

        Beneficio to = new Beneficio();
        to.setId(2L);
        to.setValor(BigDecimal.valueOf(50));

        when(em.find(Beneficio.class, 1L)).thenReturn(from);
        when(em.find(Beneficio.class, 2L)).thenReturn(to);

        assertThrows(InsufficientFundsException.class,
                () -> service.transfer(1L, 2L, BigDecimal.valueOf(200)));
    }

    @Test
    void testTransferConcurrencyException() {
        Beneficio from = new Beneficio();
        from.setId(1L);
        from.setValor(BigDecimal.valueOf(500));

        Beneficio to = new Beneficio();
        to.setId(2L);
        to.setValor(BigDecimal.valueOf(100));

        when(em.find(Beneficio.class, 1L)).thenReturn(from);
        when(em.find(Beneficio.class, 2L)).thenReturn(to);
        doThrow(OptimisticLockException.class).when(em).flush();

        assertThrows(InsufficientFundsException.class,
                () -> service.transfer(1L, 2L, BigDecimal.valueOf(200)));
    }

    @Test
    void testTransferInvalidParameters() {
        assertThrows(TransferException.class, () -> service.transfer(null, 2L, BigDecimal.valueOf(10)));
        assertThrows(TransferException.class, () -> service.transfer(1L, null, BigDecimal.valueOf(10)));
        assertThrows(TransferException.class, () -> service.transfer(1L, 1L, BigDecimal.valueOf(10)));
        assertThrows(TransferException.class, () -> service.transfer(1L, 2L, null));
        assertThrows(TransferException.class, () -> service.transfer(1L, 2L, BigDecimal.valueOf(0)));
    }
}
