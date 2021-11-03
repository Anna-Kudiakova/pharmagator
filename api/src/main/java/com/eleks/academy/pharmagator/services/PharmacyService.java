package com.eleks.academy.pharmagator.services;

import com.eleks.academy.pharmagator.dataproviders.dto.input.PharmacyDto;
import com.eleks.academy.pharmagator.entities.Pharmacy;

import java.util.List;
import java.util.Optional;

public interface PharmacyService {

    List<Pharmacy> findAll();

    Optional<Pharmacy> findById(Long id);

    Pharmacy save(PharmacyDto pharmacyDto);

    Optional<Pharmacy> update(Long id, PharmacyDto pharmacyDto);

    void deleteById(Long id);

}
