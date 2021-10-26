package com.eleks.academy.pharmagator.controllers;


import com.eleks.academy.pharmagator.entities.Price;
import com.eleks.academy.pharmagator.entities.PriceId;
import com.eleks.academy.pharmagator.repositories.PriceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/prices")
@RequiredArgsConstructor
public class PriceController {

    private final PriceRepository priceRepository;

    @GetMapping
    public ResponseEntity<List<Price>> getAll(@RequestParam(required = false) Long medicineId,
                                              @RequestParam(required = false) Long pharmacyId) {
        if (medicineId != null && pharmacyId != null)
            return ResponseEntity.ok(priceRepository.findByMedicineIdAndPharmacyId(medicineId, pharmacyId));
        else if (medicineId != null)
            return ResponseEntity.ok(priceRepository.findByMedicineId(medicineId));
        else if (pharmacyId != null)
            return ResponseEntity.ok(priceRepository.findByPharmacyId(medicineId));
        else
            return ResponseEntity.ok(priceRepository.findAll());
    }

    @GetMapping("pharmacies/{pharmacy-id}/medicines/{medicine-id}")
    public ResponseEntity<Price> getById(@PathVariable("pharmacy-id") Long pharmacyId, @PathVariable("medicine-id") Long medicineId) {
        PriceId id = new PriceId(pharmacyId, medicineId);
        Optional<Price> priceData = priceRepository.findById(id);
        if (priceData.isPresent()) {
            return new ResponseEntity(priceData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @PostMapping
    public ResponseEntity<Price> create(@Valid  @RequestBody Price price) {
        try {
            Price newPrice = priceRepository.save(new Price(price.getPharmacyId(), price.getMedicineId(), price.getPrice(), price.getExternalId(), price.getUpdatedAt()));
            return new ResponseEntity<>(newPrice, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PutMapping("pharmacies/{pharmacy-id}/medicines/{medicine-id}")
    public ResponseEntity<Price> update(@PathVariable("pharmacy-id") Long pharmacyId, @PathVariable("medicine-id") Long medicineId, @Valid @RequestBody Price price) {
        PriceId id = new PriceId(pharmacyId, medicineId);
        Optional<Price> priceData = priceRepository.findById(id);
        if (priceData.isPresent()) {
            Price updatedPrice = priceData.get();
            updatedPrice.setPharmacyId(price.getPharmacyId());
            updatedPrice.setMedicineId(price.getMedicineId());
            updatedPrice.setPrice(price.getPrice());
            updatedPrice.setExternalId(price.getExternalId());
            updatedPrice.setUpdatedAt(price.getUpdatedAt());
            return new ResponseEntity<>(priceRepository.save(updatedPrice), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("pharmacies/{pharmacy-id}/medicines/{medicine-id}")
    public void delete(@PathVariable("pharmacy-id") Long pharmacyId, @PathVariable("medicine-id") Long medicineId) {
        PriceId id = new PriceId(pharmacyId, medicineId);
        priceRepository.deleteById(id);
    }
}

