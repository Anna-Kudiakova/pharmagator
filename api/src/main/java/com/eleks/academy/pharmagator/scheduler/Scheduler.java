package com.eleks.academy.pharmagator.scheduler;

import com.eleks.academy.pharmagator.dataproviders.DataProvider;
import com.eleks.academy.pharmagator.dataproviders.dto.MedicineDto;
import com.eleks.academy.pharmagator.entities.Medicine;
import com.eleks.academy.pharmagator.entities.Pharmacy;
import com.eleks.academy.pharmagator.entities.Price;
import com.eleks.academy.pharmagator.repositories.MedicineRepository;
import com.eleks.academy.pharmagator.repositories.PharmacyRepository;
import com.eleks.academy.pharmagator.repositories.PriceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Component
public class Scheduler {


    private final PriceRepository priceRepository;
    private final ModelMapper modelMapper;
    private final MedicineRepository medicineRepository;
    private final PharmacyRepository pharmacyRepository;
    private Pharmacy pharmacy;


    {
        pharmacy = new Pharmacy();
        pharmacy.setId(3L);
        pharmacy.setName("Rozetka");
    }

    private final List<DataProvider> dataProviderList;

    @Scheduled(fixedDelay = 1, timeUnit = TimeUnit.MINUTES)
    public void schedule() {
        log.info("Scheduler started at {}", Instant.now());
        dataProviderList.get(2).loadData().forEach(this::storeToDatabase);

    }

    private void storeToDatabase(MedicineDto dto) {
        Optional<Medicine> medicineIs = medicineRepository.findByTitle(dto.getTitle());
        if (medicineIs.isPresent()) {
            Medicine medicine = medicineIs.get();
            List<Price> optional = priceRepository.findByMedicineIdAndPharmacyId(medicine.getId(), pharmacy.getId());
            Optional<Price> optionalPrice = optional.stream().findFirst();
            if (optionalPrice.isPresent()) {
                Price price = optionalPrice.get();
                price.setPrice(dto.getPrice());
                price.setUpdatedAt(Instant.now());
                priceRepository.save(price);
            } else {
                Price price = modelMapper.map(dto, Price.class);
                price.setMedicineId(medicine.getId());
                price.setPharmacyId(pharmacy.getId());
                price.setUpdatedAt(Instant.now());
                priceRepository.save(price);
            }

        } else {
            Medicine medicine = modelMapper.map(dto, Medicine.class);
            medicineRepository.save(medicine);
            Price price = modelMapper.map(dto, Price.class);
            price.setMedicineId(medicine.getId());
            price.setPharmacyId(pharmacy.getId());
            price.setUpdatedAt(Instant.now());
            priceRepository.save(price);
        }

    }




}
