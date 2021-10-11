package com.eleks.academy.pharmagator.scheduler;

import com.eleks.academy.pharmagator.dataproviders.DataProvider;
import com.eleks.academy.pharmagator.dataproviders.dto.MedicineDto;
import com.eleks.academy.pharmagator.entities.Medicine;
import com.eleks.academy.pharmagator.entities.Price;
import com.eleks.academy.pharmagator.repositories.MedicineRepository;
import com.eleks.academy.pharmagator.repositories.PriceRepository;
import org.modelmapper.ModelMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;



import java.time.Instant;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Component
public class Scheduler {
    private final DataProvider dataProvider;
    private final PriceRepository priceRepository;
    private final ModelMapper modelMapper;
    private final MedicineRepository medicineRepository;

    @Scheduled(fixedDelay = 1, timeUnit = TimeUnit.MINUTES)
    public void schedule() {
        log.info("Scheduler started at {}", Instant.now());
        dataProvider.loadData().forEach(this::storeToDatabase);
    }

    private void storeToDatabase(MedicineDto dto) {
      Medicine medicine = modelMapper.map(dto, Medicine.class);
      Price price = modelMapper.map(dto, Price.class);
      price.setUpdatedAt(Instant.now());
      price.setPharmacyId(1); //set some pharmacy id that exists in database
      price.setMedicineId(1); //set some medicine id that exists in database
      medicineRepository.save(medicine);
      priceRepository.save(price);
    }
}
