package com.eleks.academy.pharmagator.dataproviders;

import com.eleks.academy.pharmagator.dataproviders.dto.MedicineDto;
import com.eleks.academy.pharmagator.dataproviders.dto.anc.ANCMedicineDto;
import com.eleks.academy.pharmagator.dataproviders.dto.anc.ANCMedicinesResponse;
import com.eleks.academy.pharmagator.dataproviders.dto.anc.ANCSubcategoryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Qualifier("pharmacyANCDataProvider")
public class PharmacyANCDataProvider implements DataProvider {

    @Qualifier("pharmacyANCWebClient")
    private final WebClient ancClient;

    @Value("${pharmagator.data-providers.pharmacy-anc.category-fetch-url}")
    private String categoriesFetchUrl;

    @Value("${pharmagator.data-providers.pharmacy-anc.page-size}")
    private Long pageSize;

    @Value("${pharmagator.data-providers.pharmacy-anc.pharmacy-name}")
    private String pharmacyName;

    @Override
    public Stream<MedicineDto> loadData() {
        return this.fetchCategories().stream()
                .map(ANCSubcategoryDto::getSubcategories)
                .flatMap(Collection::stream)
                .map(ANCSubcategoryDto::getLink)
                .flatMap(this::fetchProductsByCategory);
    }

    private List<ANCSubcategoryDto> fetchCategories() {
        return this.ancClient.get().uri(categoriesFetchUrl + "/medikamenty-1")
                .retrieve().bodyToMono(new ParameterizedTypeReference<ANCSubcategoryDto>() {
                }).map(ANCSubcategoryDto::getSubcategories).block();
    }

    private Stream<MedicineDto> fetchProductsByCategory(String category) {
        ANCMedicinesResponse firstANCMedicinesResponse = getMedicineResponseByCategoryAndPage(category, 0L);

        return LongStream
                .range(1, firstANCMedicinesResponse.getTotal() / pageSize +
                        (firstANCMedicinesResponse.getTotal() % pageSize) != 0 ? 1 : 0)
                .mapToObj(page -> getMedicineResponseByCategoryAndPage(category, page))
                .collect(Collectors.toCollection(() -> Arrays.asList(firstANCMedicinesResponse)))
                .stream()
                .map(ANCMedicinesResponse::getProducts)
                .flatMap(Collection::stream)
                .map(this::mapToMedicineDto);
    }

    private ANCMedicinesResponse getMedicineResponseByCategoryAndPage(String category, Long page) {
        return this.ancClient.get()
                .uri(builder -> builder
                        .path(categoriesFetchUrl + "/" + category)
                        .queryParam("p", page)
                        .queryParam("s", pageSize)
                        .build())
                .retrieve()
                .bodyToMono(ANCMedicinesResponse.class)
                .block();
    }

    private MedicineDto mapToMedicineDto(ANCMedicineDto ancMedicineDTO) {
        return MedicineDto.builder()
                .externalId(ancMedicineDTO.getId())
                .price(ancMedicineDTO.getPrice())
                .title(ancMedicineDTO.getName())
                .pharmacyName(pharmacyName)
                .build();
    }

}

