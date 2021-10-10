package com.eleks.academy.pharmagator.controllers;


import com.eleks.academy.pharmagator.entities.Price;
import com.eleks.academy.pharmagator.repositories.PriceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/prices")
@RequiredArgsConstructor
public class PriceController {

    private final PriceRepository priceRepository;

    @GetMapping
    public ResponseEntity<List<Price>> getAll(){
        return ResponseEntity.ok(priceRepository.findAll());
    }
}

