package com.eleks.academy.pharmagator.controllers;

import com.eleks.academy.pharmagator.entities.Pharmacy;
import com.eleks.academy.pharmagator.repositories.PharmacyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/pharmacies")
@RequiredArgsConstructor
public class PharmacyController {

    private final PharmacyRepository pharmacyRepository;

    @GetMapping
    public ResponseEntity<List<Pharmacy>> getAll(){
        return ResponseEntity.ok(pharmacyRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pharmacy> getById(@PathVariable("id") long id) {
        Optional<Pharmacy> pharmacyData = pharmacyRepository.findById(id);
        if (pharmacyData.isPresent()) {
            return new ResponseEntity(pharmacyData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @PostMapping
    public ResponseEntity<Pharmacy> create(@RequestBody Pharmacy pharmacy) {
        try {
            Pharmacy newPharmacy = pharmacyRepository.save(new Pharmacy(pharmacy.getId(), pharmacy.getName(), pharmacy.getMedicineLinkTemplate()));
            return new ResponseEntity<>(newPharmacy, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<Pharmacy> update(@PathVariable("id") long id, @RequestBody Pharmacy pharmacy) {
        Optional<Pharmacy> pharmacyData = pharmacyRepository.findById(id);
        if (pharmacyData.isPresent()) {
            Pharmacy updatedPharmacy = pharmacyData.get();
            updatedPharmacy.setId(pharmacy.getId());
            updatedPharmacy.setName(pharmacy.getName());
            updatedPharmacy.setMedicineLinkTemplate(pharmacy.getMedicineLinkTemplate());
            return new ResponseEntity<>(pharmacyRepository.save(updatedPharmacy), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") long id) {
        try {
            pharmacyRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
