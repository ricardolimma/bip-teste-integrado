package com.example.backend.service;

import com.example.ejb.entity.Beneficio;
import com.example.ejb.service.BeneficioEjbService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BeneficioService {

    private final BeneficioEjbService beneficioEjbService = new BeneficioEjbService();

    public Beneficio create(Beneficio beneficio) {
        return beneficioEjbService.create(beneficio);
    }

    public List<Beneficio> findAll() {
        return beneficioEjbService.findAll();
    }

    public Beneficio findById(Long id) {
        return beneficioEjbService.findById(id);
    }

    public Beneficio update(Beneficio beneficio, Long id) {
        return beneficioEjbService.update(id, beneficio);
    }

    public void delete(Long id) {
        beneficioEjbService.delete(id);
    }
}
