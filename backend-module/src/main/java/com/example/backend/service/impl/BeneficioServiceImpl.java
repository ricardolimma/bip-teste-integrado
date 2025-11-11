package com.example.backend.service.impl;

import com.example.backend.service.BeneficioService;
import com.example.ejb.entity.Beneficio;
import com.example.ejb.service.BeneficioEjbService;
import jakarta.ejb.EJB;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BeneficioServiceImpl implements BeneficioService {

    @EJB(lookup = "java:global/ejb-module/BeneficioEjbService!com.example.ejb.service.BeneficioEjbService")
    private BeneficioEjbService ejb;

    public BeneficioServiceImpl(BeneficioEjbService ejb) {
        this.ejb = ejb;
    }

    @Override
    public Beneficio create(Beneficio beneficio) {
        return ejb.create(beneficio);
    }

    @Override
    public Beneficio findById(Long id) {
        return ejb.findById(id);
    }

    @Override
    public List<Beneficio> findAll() {
        return ejb.findAll();
    }

    @Override
    public Beneficio update(Beneficio beneficio, Long id) {
        return ejb.update(id, beneficio);
    }

    @Override
    public void delete(Long id) {
        ejb.delete(id);
    }
}
