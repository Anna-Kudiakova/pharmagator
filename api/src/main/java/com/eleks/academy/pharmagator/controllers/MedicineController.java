package com.eleks.academy.pharmagator.controllers;


import com.eleks.academy.pharmagator.entities.Medicine;
import com.eleks.academy.pharmagator.entities.Pharmacy;
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

    @GetMapping("/list")
    public ResponseEntity<List<Medicine>> getAll(){
        return ResponseEntity.ok(medicineRepository.findAll());
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Medicine> getById(@PathVariable("id") long id) {
        Optional<Medicine> medicineData = medicineRepository.findById(id);
        if (medicineData.isPresent()) {
            return new ResponseEntity(medicineData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @PostMapping("/save")
    public ResponseEntity<Medicine> create(@RequestBody Medicine medicine) {
        try {
            Medicine newMedicine = medicineRepository.save(new Medicine(medicine.getId(), medicine.getTitle()));
            return new ResponseEntity<>(newMedicine, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<Medicine> update(@PathVariable("id") long id, @RequestBody Medicine medicine) {
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

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") long id) {
        try {
            medicineRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
