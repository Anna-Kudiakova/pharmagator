package com.eleks.academy.pharmagator.services;

import com.eleks.academy.pharmagator.dataproviders.dto.MedicineDto;
import com.eleks.academy.pharmagator.entities.Medicine;
import com.eleks.academy.pharmagator.entities.Pharmacy;
import com.eleks.academy.pharmagator.entities.Price;
import com.eleks.academy.pharmagator.exceptions.InvalidFileFormatException;
import com.eleks.academy.pharmagator.repositories.MedicineRepository;
import com.eleks.academy.pharmagator.repositories.PharmacyRepository;
import com.eleks.academy.pharmagator.repositories.PriceRepository;
import com.univocity.parsers.csv.CsvParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;

@Service
@RequiredArgsConstructor
@Slf4j
public class CsvService {

    private final MedicineRepository medicineRepository;
    private final PriceRepository priceRepository;
    private final PharmacyRepository pharmacyRepository;

    private final ModelMapper modelMapper;

    private final CsvParser parser = CsvHelper.getCsvParser(CsvHelper.getProcessor());

    public void save(MultipartFile file) throws InvalidFileFormatException {
        if (!CsvHelper.hasCSVFormat(file)) {
            throw new InvalidFileFormatException("Wrong file format!");
        } else {
            try {
                CsvHelper.parse(file.getInputStream(), parser).forEach(this::storeToDatabase);
            } catch (IOException e) {
                log.error("File reading error", e);
            }
        }
    }

    private void storeToDatabase(MedicineDto medicineDto) {
        Medicine medicine = modelMapper.map(medicineDto, Medicine.class);
        Price price = modelMapper.map(medicineDto, Price.class);

        String pharmacyName = medicineDto.getPharmacyName();
        Pharmacy pharmacyFromDb = pharmacyRepository.findByName(pharmacyName).orElseGet(() -> {
            Pharmacy pharmacy = new Pharmacy();
            pharmacy.setName(pharmacyName);
            return pharmacyRepository.save(pharmacy);
        });

        Medicine medicineFromDb = medicineRepository.findByTitle(medicine.getTitle()).orElseGet(() -> medicineRepository.save(medicine));

        price.setMedicineId(medicineFromDb.getId());
        price.setPharmacyId(pharmacyFromDb.getId());
        price.setUpdatedAt(Instant.now());
        priceRepository.save(price);
    }

}