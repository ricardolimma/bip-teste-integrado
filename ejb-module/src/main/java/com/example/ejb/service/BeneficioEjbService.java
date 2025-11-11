package com.example.ejb.service;

import com.example.ejb.entity.Beneficio;
import com.example.ejb.exception.TransferExceptions.*;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.OptimisticLockException;
import jakarta.persistence.PersistenceContext;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Stateless
public class BeneficioEjbService {

    @PersistenceContext
    private EntityManager em;

    private static final int MAX_RETRIES = 3;

    public BeneficioEjbService() {
    }

    public void transfer(Long fromId, Long toId, BigDecimal amount) {
        validateParameters(fromId, toId, amount);

        int attempt = 0;
        while (true) {
            attempt++;
            try {
                doTransfer(fromId, toId, amount);
                return;
            } catch (OptimisticLockException ole) {
                if (attempt >= MAX_RETRIES) {
                    throw new ConcurrencyException("Conflito de concorrência ao transferir após " + attempt + " tentativas", ole);
                }
            }
        }
    }

    private void doTransfer(Long fromId, Long toId, BigDecimal amount) {
        Beneficio from = em.find(Beneficio.class, fromId);
        Beneficio to = em.find(Beneficio.class, toId);

        if (from == null) throw new AccountNotFoundException(fromId);
        if (to == null) throw new AccountNotFoundException(toId);

        BigDecimal fromValor = Objects.requireNonNullElse(from.getValor(), BigDecimal.ZERO);
        if (fromValor.compareTo(amount) < 0) {
            throw new InsufficientFundsException(fromId, "saldo: " + fromValor + ", necessário: " + amount);
        }

        from.setValor(fromValor.subtract(amount));
        to.setValor(Objects.requireNonNullElse(to.getValor(), BigDecimal.ZERO).add(amount));

        em.flush();
    }

    private void validateParameters(Long fromId, Long toId, BigDecimal amount) {
        if (fromId == null) throw new TransferException("fromId é nulo");
        if (toId == null) throw new TransferException("toId é nulo");
        if (Objects.equals(fromId, toId)) throw new TransferException("fromId e toId devem ser diferentes");
        if (amount == null) throw new TransferException("amount é nulo");
        if (amount.compareTo(BigDecimal.ZERO) <= 0) throw new TransferException("amount deve ser maior que zero");
    }

    public Beneficio create(Beneficio beneficio) {
        if (beneficio == null) throw new IllegalArgumentException("Benefício não pode ser nulo");
        if (beneficio.getNome() == null || beneficio.getNome().isBlank())
            throw new IllegalArgumentException("Nome do benefício é obrigatório");
        if (beneficio.getValor() == null || beneficio.getValor().compareTo(BigDecimal.ZERO) < 0)
            throw new IllegalArgumentException("Valor do benefício deve ser positivo");

        em.persist(beneficio);
        em.flush();
        return beneficio;
    }

    public Beneficio findById(Long id) {
        Beneficio beneficio = em.find(Beneficio.class, id);
        if (beneficio == null) throw new EntityNotFoundException("Benefício com id " + id + " não encontrado");
        return beneficio;
    }

    public List<Beneficio> findAll() {
        return em.createQuery("SELECT b FROM Beneficio b", Beneficio.class).getResultList();
    }

    public Beneficio update(Long id, Beneficio updated) {
        Beneficio existing = em.find(Beneficio.class, id);
        if (existing == null) throw new EntityNotFoundException("Benefício com id " + id + " não encontrado");

        existing.setNome(updated.getNome());
        existing.setDescricao(updated.getDescricao());
        existing.setValor(updated.getValor());
        existing.setAtivo(updated.getAtivo());

        return em.merge(existing);
    }

    public void delete(Long id) {
        Beneficio beneficio = em.find(Beneficio.class, id);
        if (beneficio == null) throw new EntityNotFoundException("Benefício com id " + id + " não encontrado");
        em.remove(beneficio);
    }
}
