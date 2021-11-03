package com.eleks.academy.pharmagator.controllers;

import com.eleks.academy.pharmagator.dataproviders.dto.input.MedicineDto;
import com.eleks.academy.pharmagator.entities.Medicine;
import com.eleks.academy.pharmagator.services.MedicineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/medicines")
@RequiredArgsConstructor
public class MedicineController {

    private final MedicineService medicineService;

    @GetMapping
    public List<Medicine> getAll() {
        return this.medicineService.findAll();
    }

    @GetMapping("/{id:[\\d]+}")
    public ResponseEntity<Medicine> getById(@PathVariable Long id) {
        return this.medicineService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Medicine create(@Valid @RequestBody MedicineDto medicineDto) {
        return this.medicineService.save(medicineDto);
    }

    @PutMapping("/{id:[\\d]+}")
    public ResponseEntity<Medicine> update(
            @PathVariable Long id,
            @Valid @RequestBody MedicineDto medicineDto) {

        return this.medicineService.update(id, medicineDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id:[\\d]+}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        this.medicineService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
