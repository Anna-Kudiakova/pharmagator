package com.eleks.academy.pharmagator.dataproviders;

import com.eleks.academy.pharmagator.dataproviders.dto.MedicineDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Qualifier("pharmacyRozetkaDataProvider")
public class PharmacyRozetkaDataProvider implements DataProvider {

    private final WebClient rozetkaClient;

    @Override
    public Stream<MedicineDto> loadData() {
        return null;
    }



}
