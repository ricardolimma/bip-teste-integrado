package com.example.backend.controller;

import com.example.backend.service.BeneficioService;
import com.example.ejb.entity.Beneficio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/beneficios")
public class BeneficioController {

    @Autowired
    private BeneficioService beneficioService;

    @PostMapping
    public ResponseEntity<Beneficio> create(@RequestBody Beneficio beneficio) {
        Beneficio created = beneficioService.create(beneficio);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @GetMapping
    public ResponseEntity<List<Beneficio>> findAll() {
        List<Beneficio> list = beneficioService.findAll();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Beneficio> findById(@PathVariable Long id) {
        Beneficio b = beneficioService.findById(id);
        return ResponseEntity.ok(b);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Beneficio> update(@PathVariable Long id, @RequestBody Beneficio beneficio) {
        beneficio.setId(id);
        Beneficio updated = beneficioService.update(beneficio, id);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        beneficioService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
