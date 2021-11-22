package com.eleks.academy.pharmagator.services;


import com.eleks.academy.pharmagator.dataproviders.dto.MedicineDto;
import com.eleks.academy.pharmagator.entities.Medicine;
import com.eleks.academy.pharmagator.entities.Pharmacy;
import com.eleks.academy.pharmagator.entities.Price;
import com.eleks.academy.pharmagator.repositories.MedicineRepository;
import com.eleks.academy.pharmagator.repositories.PharmacyRepository;
import com.eleks.academy.pharmagator.repositories.PriceRepository;
import com.univocity.parsers.common.processor.BeanListProcessor;
import com.univocity.parsers.csv.CsvParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class CsvServiceImpl implements CsvService {

    private final CsvParser parser;

    private final BeanListProcessor<MedicineDto> rowProcessor;

    private final static String TYPE = "text/csv";

    private final ModelMapper modelMapper;

    private final MedicineRepository medicineRepository;
    private final PriceRepository priceRepository;
    private final PharmacyRepository pharmacyRepository;



    private List<MedicineDto> parse(InputStream inputStream) {
        parser.parse(inputStream);
        return rowProcessor.getBeans();

    }

    @Override
    public String save(MultipartFile file) throws IllegalArgumentException {
        if (Objects.equals(file.getContentType(), TYPE)) {
            try {
                InputStream inputStream = file.getInputStream();
                parse(inputStream).forEach(this::storeToDatabase);
                return "File saved successfully";
            } catch (IOException e) {
                log.error("Failed to read file", e);
            }
        }
        throw new IllegalArgumentException("Invalid file format");
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
