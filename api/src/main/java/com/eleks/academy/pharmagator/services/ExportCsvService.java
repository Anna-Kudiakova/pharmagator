package com.eleks.academy.pharmagator.services;

import com.eleks.academy.pharmagator.entities.Pharmacy;
import com.eleks.academy.pharmagator.projections.MedicinePrice;
import com.eleks.academy.pharmagator.repositories.PharmacyRepository;
import com.eleks.academy.pharmagator.repositories.PriceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ExportCsvService {

    private final PriceRepository priceRepository;
    private final PharmacyRepository pharmacyRepository;

    public List<String[]> getCsvExportData(){

        Map<String, Map<Long, BigDecimal>> prices = priceRepository.findAllMedicinesPrices()
                .stream()
                .collect(Collectors.groupingBy(MedicinePrice::getTitle,
                        Collectors.toMap(MedicinePrice::getPharmacyId, MedicinePrice::getPrice)));

        List<Pharmacy> pharmacies = pharmacyRepository.findAll();

        HashMap<Long, Integer> pharmacyColumnMapping = new HashMap<>(pharmacies.size());

        List<String[]> records = new ArrayList<>();

        String[] headers = getCsvHeader(pharmacies, pharmacyColumnMapping);
        records.add(headers);

        prices.forEach((medicineTitle, phs) -> {
            String[] record = getCsvRecord(pharmacies.size(),pharmacyColumnMapping, medicineTitle, phs);
            records.add(record);
        });

        return records;

    }

    private String[] getCsvHeader(List<Pharmacy> pharmacies, HashMap<Long, Integer> pharmacyColumnMapping){

        AtomicInteger index = new AtomicInteger(1);

        pharmacies.forEach(pharmacy -> pharmacyColumnMapping.put(pharmacy.getId(), index.getAndIncrement()));

        String[] headers = pharmacies.stream()
                .map(Pharmacy::getName)
                .collect(Collectors.toList())
                .toArray(new String[0]);

        return headers;
    }

    private String[] getCsvRecord(int pharmaciesQuantity, HashMap<Long, Integer> pharmacyColumnMapping, String medicineTitle, Map<Long, BigDecimal> phs){
        String[] record = new String[pharmaciesQuantity+1];
        record[0] = medicineTitle;
        phs.forEach((pharmacyId, price) -> {
            record[pharmacyColumnMapping.get(pharmacyId)] = String.valueOf(price);
        });
        return record;
    }

}
