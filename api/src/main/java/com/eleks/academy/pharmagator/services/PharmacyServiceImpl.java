package com.eleks.academy.pharmagator.services;

import com.eleks.academy.pharmagator.dataproviders.dto.input.PharmacyDto;
import com.eleks.academy.pharmagator.entities.Pharmacy;
import com.eleks.academy.pharmagator.repositories.PharmacyRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PharmacyServiceImpl implements PharmacyService {

    private final PharmacyRepository pharmacyRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<Pharmacy> findAll() {
        return pharmacyRepository.findAll();
    }

    @Override
    public Optional<Pharmacy> findById(Long id) {
        return pharmacyRepository.findById(id);
    }

    @Override
    public Pharmacy save(PharmacyDto pharmacyDto) {
        Pharmacy pharmacy = modelMapper.map(pharmacyDto, Pharmacy.class);
        return pharmacyRepository.save(pharmacy);
    }

    @Override
    public Optional<Pharmacy> update(Long id, PharmacyDto pharmacyDto) {
        return pharmacyRepository.findById(id)
                .map(ph ->{
                    Pharmacy pharmacy = modelMapper.map(pharmacyDto, Pharmacy.class);
                    pharmacy.setId(id);
                    return pharmacyRepository.save(pharmacy);
                });
    }

    @Override
    public void deleteById(Long id) {
        pharmacyRepository.deleteById(id);
    }

}
