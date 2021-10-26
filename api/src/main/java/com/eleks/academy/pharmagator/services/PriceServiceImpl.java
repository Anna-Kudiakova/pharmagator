package com.eleks.academy.pharmagator.services;

import com.eleks.academy.pharmagator.controllers.dto.PriceDto;
import com.eleks.academy.pharmagator.entities.Pharmacy;
import com.eleks.academy.pharmagator.entities.Price;
import com.eleks.academy.pharmagator.entities.PriceId;
import com.eleks.academy.pharmagator.repositories.PriceRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PriceServiceImpl implements PriceService {

    private final PriceRepository priceRepository;

    private final ModelMapper modelMapper;

    @Override
    public List<Price> findAll() {

        return priceRepository.findAll();
    }

    @Override
    public Optional<Price> findById(Long pharmacyId, Long medicineId) {

        PriceId priceId = new PriceId(pharmacyId, medicineId);
        return priceRepository.findById(priceId);
    }

    @Override
    public List<Price> findByPharmacyId(Long pharmacyId) {

        return priceRepository.findByPharmacyId(pharmacyId);
    }

    @Override
    public List<Price> findByMedicineId(Long medicineId) {

        return priceRepository.findByMedicineId(medicineId);
    }

    @Override
    public Price save(PriceDto priceDto) {

        Price price = modelMapper.map(priceDto, Price.class);
        return priceRepository.save(price);
    }

    @Override
    public Optional<Price> update(Long pharmacyId, Long medicineId, PriceDto priceDto) {

        PriceId priceId = new PriceId(pharmacyId, medicineId);
        return priceRepository.findById(priceId)
                .map(pharmacy -> {
                    pharmacy = modelMapper.map(priceDto, Price.class);
                    return priceRepository.save(pharmacy);
                });
    }

    @Override
    public void deleteById(Long pharmacyId, Long medicineId) {
        PriceId priceId = new PriceId(pharmacyId, medicineId);
        priceRepository.deleteById(priceId);
    }
}
