package com.eleks.academy.pharmagator.controllers;


import com.eleks.academy.pharmagator.entities.Medicine;
import com.eleks.academy.pharmagator.entities.Price;
import com.eleks.academy.pharmagator.entities.PriceId;
import com.eleks.academy.pharmagator.repositories.PriceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/prices")
@RequiredArgsConstructor
public class PriceController {

    private final PriceRepository priceRepository;

    @GetMapping("/list")
    public ResponseEntity<List<Price>> getAll(){
        return ResponseEntity.ok(priceRepository.findAll());
    }

    @GetMapping("/get/{pharmacyId}/{medicineId}")
    public ResponseEntity<Price> getById(@PathVariable("pharmacyId") Long pharmacyId, @PathVariable("medicineId") Long medicineId) {
        PriceId id = new PriceId(pharmacyId,medicineId);
        Optional<Price> priceData = priceRepository.findById(id);
        if (priceData.isPresent()) {
            return new ResponseEntity(priceData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @PostMapping("/save")
    public ResponseEntity<Price> create(@RequestBody Price price) {
        try {
            Price newPrice = priceRepository.save(new Price(price.getPharmacyId(), price.getMedicineId(), price.getPrice(), price.getExternalId(), price.getUpdatedAt()));
            return new ResponseEntity<>(newPrice, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PutMapping("/update/{pharmacyId}/{medicineId}")
    public ResponseEntity<Price> update(@PathVariable("pharmacyId") Long pharmacyId, @PathVariable("medicineId") Long medicineId, @RequestBody Price price) {
        PriceId id = new PriceId(pharmacyId,medicineId);
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

    @DeleteMapping("/delete/{pharmacyId}/{medicineId}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("pharmacyId") Long pharmacyId, @PathVariable("medicineId") Long medicineId) {
        try {
            PriceId id = new PriceId(pharmacyId,medicineId);
            priceRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

