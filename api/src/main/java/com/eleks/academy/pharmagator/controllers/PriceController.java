package com.eleks.academy.pharmagator.controllers;

import com.eleks.academy.pharmagator.dataproviders.dto.input.PriceDto;
import com.eleks.academy.pharmagator.services.PriceService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/prices")
@RequiredArgsConstructor
public class PriceController {

    private final PriceService priceService;
    private final ModelMapper modelMapper;

    @GetMapping
    public List<PriceDto> getAll() {
        return priceService.findAll().stream()
                .map(price -> modelMapper.map(price, PriceDto.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/pharmacyId/{pharmacyId:[\\d]+}/medicineId/{medicineId:[\\d]+}")
    public ResponseEntity<PriceDto> getById(
            @PathVariable Long pharmacyId,
            @PathVariable Long medicineId) {

        return priceService.findById(pharmacyId, medicineId)
                .map(price -> ResponseEntity.ok(modelMapper.map(price, PriceDto.class)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/pharmacyId/{pharmacyId:[\\d]+}/medicineId/{medicineId:[\\d]+}")
    public ResponseEntity<PriceDto> update(
            @Valid @RequestBody PriceDto priceDto,
            @PathVariable Long pharmacyId,
            @PathVariable Long medicineId) {

        return priceService.update(pharmacyId, medicineId, priceDto)
                .map(price -> ResponseEntity.ok(modelMapper.map(price, PriceDto.class)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/pharmacyId/{pharmacyId:[\\d]+}/medicineId/{medicineId:[\\d]+}")
    public ResponseEntity<?> delete(
            @PathVariable Long pharmacyId,
            @PathVariable Long medicineId) {

        priceService.deleteById(pharmacyId, medicineId);
        return ResponseEntity.noContent().build();
    }

}
