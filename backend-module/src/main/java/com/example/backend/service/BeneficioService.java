package com.example.backend.service;

import com.example.ejb.entity.Beneficio;
import java.util.List;

public interface BeneficioService {
    List<Beneficio> findAll();
    Beneficio findById(Long id);
    Beneficio create(Beneficio beneficio);
    Beneficio update(Beneficio beneficio, Long id);
    void delete(Long id);
}
