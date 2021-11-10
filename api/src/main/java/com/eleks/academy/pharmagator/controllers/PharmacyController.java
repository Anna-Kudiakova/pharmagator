package com.eleks.academy.pharmagator.controllers;

import com.eleks.academy.pharmagator.dataproviders.dto.input.PharmacyDto;
import com.eleks.academy.pharmagator.services.PharmacyService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/pharmacies")
public class PharmacyController {

    private final PharmacyService pharmacyService;
    private final ModelMapper modelMapper;

    @GetMapping
    public List<PharmacyDto> getAll() {
        return pharmacyService.findAll().stream()
                .map(pharmacy -> modelMapper.map(pharmacy, PharmacyDto.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id:[\\d]+}")
    public ResponseEntity<PharmacyDto> getById(@PathVariable Long id) {
        return pharmacyService.findById(id)
                .map(pharmacy -> ResponseEntity.ok(modelMapper.map(pharmacy, PharmacyDto.class)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public PharmacyDto create(@Valid @RequestBody PharmacyDto pharmacyDto) {
        return modelMapper.map(pharmacyService.save(pharmacyDto), PharmacyDto.class);
    }

    @PutMapping("/{id:[\\d]+}")
    public ResponseEntity<PharmacyDto> update(
            @PathVariable Long id,
            @Valid @RequestBody PharmacyDto pharmacyDto) {

        return pharmacyService.update(id, pharmacyDto)
                .map(pharmacy -> ResponseEntity.ok(modelMapper.map(pharmacy, PharmacyDto.class)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id:[\\d]+}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        pharmacyService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
