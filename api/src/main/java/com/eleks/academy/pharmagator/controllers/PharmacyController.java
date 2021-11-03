package com.eleks.academy.pharmagator.controllers;

import com.eleks.academy.pharmagator.dataproviders.dto.input.PharmacyDto;
import com.eleks.academy.pharmagator.entities.Pharmacy;
import com.eleks.academy.pharmagator.services.PharmacyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/pharmacies")
public class PharmacyController {

    private final PharmacyService pharmacyService;

    @GetMapping
    public List<Pharmacy> getAll() {
        return this.pharmacyService.findAll();
    }

    @GetMapping("/{id:[\\d]+}")
    public ResponseEntity<Pharmacy> getById(@PathVariable Long id) {

        return this.pharmacyService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Pharmacy> create(@Valid @RequestBody PharmacyDto pharmacyDto) {
        return ResponseEntity.ok(this.pharmacyService.save(pharmacyDto));
    }

    @PutMapping("/{id:[\\d]+}")
    public ResponseEntity<Pharmacy> update(
            @PathVariable Long id,
            @Valid @RequestBody PharmacyDto pharmacyDto) {

        return this.pharmacyService.update(id, pharmacyDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id:[\\d]+}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        this.pharmacyService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
