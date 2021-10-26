package com.eleks.academy.pharmagator.controllers;

import com.eleks.academy.pharmagator.controllers.dto.PharmacyDto;
import com.eleks.academy.pharmagator.entities.Medicine;
import com.eleks.academy.pharmagator.entities.Pharmacy;
import com.eleks.academy.pharmagator.repositories.PharmacyRepository;
import com.eleks.academy.pharmagator.services.MedicineService;
import com.eleks.academy.pharmagator.services.PharmacyService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/pharmacies")
@RequiredArgsConstructor
public class PharmacyController {

    private final PharmacyService pharmacyService;

    @GetMapping
    public ResponseEntity<List<Pharmacy>> getAll() {

        return ResponseEntity.ok(this.pharmacyService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pharmacy> getById(@PathVariable("id") long id) {

        Optional<Pharmacy> pharmacyData = this.pharmacyService.findById(id);
        if (pharmacyData.isPresent()) {
            return new ResponseEntity(pharmacyData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @PostMapping
    public ResponseEntity<Pharmacy> create(@Valid @RequestBody PharmacyDto pharmacyDto) {

        return new ResponseEntity<>(this.pharmacyService.save(pharmacyDto), HttpStatus.CREATED);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Pharmacy> update(@PathVariable("id") Long id, @Valid @RequestBody PharmacyDto pharmacyDto) {

        Optional<Pharmacy> pharmacyData = this.pharmacyService.update(id, pharmacyDto);
        if (pharmacyData.isPresent()) {
            return new ResponseEntity<>(pharmacyData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") long id) {

        this.pharmacyService.deleteById(id);
    }


}

@Data
@NoArgsConstructor
@AllArgsConstructor
class PharmacyRequest {

    @NotEmpty
    private String name;

    @NotEmpty
    private String medicineLinkTemplate;

}
