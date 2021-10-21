package com.eleks.academy.pharmagator.controllers;


import com.eleks.academy.pharmagator.entities.Medicine;
import com.eleks.academy.pharmagator.repositories.MedicineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/medicines")
@RequiredArgsConstructor
public class MedicineController {

    private final MedicineRepository medicineRepository;

    @GetMapping
    public ResponseEntity<List<Medicine>> getAll() {
        return ResponseEntity.ok(medicineRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Medicine> getById(@PathVariable("id") Long id) {
        Optional<Medicine> medicineData = medicineRepository.findById(id);
        if (medicineData.isPresent()) {
            return new ResponseEntity(medicineData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @PostMapping
    public ResponseEntity<Medicine> create(@RequestBody Medicine medicine) {
        try {
            Medicine newMedicine = medicineRepository.save(new Medicine(medicine.getId(), medicine.getTitle()));
            return new ResponseEntity<>(newMedicine, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<Medicine> update( @PathVariable("id") Long id, @RequestBody Medicine medicine) {
        Optional<Medicine> medicineData = medicineRepository.findById(id);
        if (medicineData.isPresent()) {
            Medicine updatedMedicine = medicineData.get();
            updatedMedicine.setId(medicine.getId());
            updatedMedicine.setTitle(medicine.getTitle());
            return new ResponseEntity<>(medicineRepository.save(updatedMedicine), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") long id) {
        medicineRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
