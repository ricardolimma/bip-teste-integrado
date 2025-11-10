package com.example.ejb.service;

import com.example.ejb.entity.Beneficio;
import com.example.ejb.exception.TransferExceptions.*;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.OptimisticLockException;
import jakarta.persistence.PersistenceContext;

import java.math.BigDecimal;
import java.util.Objects;

@Stateless
public class BeneficioEjbService {

    @PersistenceContext
    private EntityManager em;

    private static final int MAX_RETRIES = 3;

    public void transfer(Long fromId, Long toId, BigDecimal amount) {
        validateParameters(fromId, toId, amount);

        int attempt = 0;
        while (true) {
            attempt++;
            try {
                doTransfer(fromId, toId, amount);
                return; // sucesso
            } catch (OptimisticLockException ole) {
                if (attempt >= MAX_RETRIES) {
                    throw new ConcurrencyException("Conflito de concorrência ao transferir após " + attempt + " tentativas", ole);
                }
            }
        }
    }

    private void doTransfer(Long fromId, Long toId, BigDecimal amount) {
        Beneficio from = em.find(Beneficio.class, fromId);
        Beneficio to   = em.find(Beneficio.class, toId);

        if (from == null) throw new AccountNotFoundException(fromId);
        if (to == null)   throw new AccountNotFoundException(toId);

        // valida saldo
        BigDecimal fromValor = Objects.requireNonNullElse(from.getValor(), BigDecimal.ZERO);
        if (fromValor.compareTo(amount) < 0) {
            throw new InsufficientFundsException(fromId, "saldo: " + fromValor + ", necessario: " + amount);
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
}
