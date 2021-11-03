package com.eleks.academy.pharmagator.controllers;


import com.eleks.academy.pharmagator.controllers.dto.PriceDto;
import com.eleks.academy.pharmagator.entities.Pharmacy;
import com.eleks.academy.pharmagator.entities.Price;
import com.eleks.academy.pharmagator.entities.PriceId;
import com.eleks.academy.pharmagator.repositories.PriceRepository;
import com.eleks.academy.pharmagator.services.PriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/prices")
@RequiredArgsConstructor
public class PriceController {

    private final PriceService priceService;

    @GetMapping
    public List<Price> getAll(@RequestParam(required = false) Long medicineId,
                                              @RequestParam(required = false) Long pharmacyId) {
        if (medicineId != null)
            return this.priceService.findByMedicineId(medicineId);
        else if (pharmacyId != null)
            return this.priceService.findByPharmacyId(medicineId);
        else
            return this.priceService.findAll();
    }

    @GetMapping("pharmacies/{pharmacy-id}/medicines/{medicine-id}")
    public ResponseEntity<Price> getById(@PathVariable("pharmacy-id") Long pharmacyId,
                                         @PathVariable("medicine-id") Long medicineId) {

        Optional<Price> priceData = this.priceService.findById(pharmacyId, medicineId);
        if (priceData.isPresent()) {
            return new ResponseEntity(priceData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @PostMapping
    public ResponseEntity<Price> create(@Valid @RequestBody PriceDto priceDto) {

        return new ResponseEntity<>(this.priceService.save(priceDto), HttpStatus.CREATED);
    }


    @PutMapping("pharmacies/{pharmacy-id}/medicines/{medicine-id}")
    public ResponseEntity<Price> update(@PathVariable("pharmacy-id") Long pharmacyId,
                                        @PathVariable("medicine-id") Long medicineId,
                                        @Valid @RequestBody PriceDto priceDto) {

        Optional<Price> priceData = this.priceService.update(pharmacyId, medicineId, priceDto);
        if (priceData.isPresent()) {
            return new ResponseEntity<>(priceData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("pharmacies/{pharmacy-id}/medicines/{medicine-id}")
    public void delete(@PathVariable("pharmacy-id") Long pharmacyId, @PathVariable("medicine-id") Long medicineId) {

        this.priceService.deleteById(pharmacyId, medicineId);
    }
}

