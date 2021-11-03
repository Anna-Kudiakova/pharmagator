package com.eleks.academy.pharmagator.dataproviders;

import com.eleks.academy.pharmagator.dataproviders.dto.MedicineDto;

import java.util.stream.Stream;

public interface DataProvider {

    Stream<MedicineDto> loadData();

}
