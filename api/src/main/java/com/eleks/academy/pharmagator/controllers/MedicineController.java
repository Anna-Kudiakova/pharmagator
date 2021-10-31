package com.eleks.academy.pharmagator.controllers;


import com.eleks.academy.pharmagator.controllers.dto.MedicineDto;
import com.eleks.academy.pharmagator.entities.Medicine;
import com.eleks.academy.pharmagator.services.MedicineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/medicines")
@RequiredArgsConstructor
public class MedicineController {

    private final MedicineService medicineService;

    @GetMapping
    public List<Medicine> getAll() {

        return this.medicineService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Medicine> getById(@PathVariable("id") Long id) {

        Optional<Medicine> medicineData = this.medicineService.findById(id);
        if (medicineData.isPresent()) {
            return new ResponseEntity(medicineData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @PostMapping
    public ResponseEntity<Medicine> create(@Valid @RequestBody MedicineDto medicineDto) {

        return new ResponseEntity<>(this.medicineService.save(medicineDto), HttpStatus.CREATED);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Medicine> update( @PathVariable("id") Long id, @Valid  @RequestBody MedicineDto medicineDto) {
        Optional<Medicine> medicineData = this.medicineService.update(id, medicineDto);
        if (medicineData.isPresent()) {
            return new ResponseEntity<>(medicineData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") long id) {
        this.medicineService.deleteById(id);
    }
}
