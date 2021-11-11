package com.eleks.academy.pharmagator.dataproviders;

import com.eleks.academy.pharmagator.dataproviders.dto.MedicineDto;
import com.eleks.academy.pharmagator.dataproviders.dto.ds.CategoryDto;
import com.eleks.academy.pharmagator.dataproviders.dto.ds.DSMedicineDto;
import com.eleks.academy.pharmagator.dataproviders.dto.ds.DSMedicinesResponse;
import com.eleks.academy.pharmagator.dataproviders.dto.ds.FilterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Qualifier("pharmacyDSDataProvider")
public class PharmacyDSDataProvider implements DataProvider {

    @Qualifier("pharmacyDSWebClient")
    private final WebClient dsClient;

    @Value("${pharmagator.data-providers.apteka-ds.category-fetch-url}")
    private String categoriesFetchUrl;

    @Value("${pharmagator.data-providers.apteka-ds.category-path}")
    private String categoryPath;

    @Value("${pharmagator.data-providers.apteka-ds.pharmacy-name}")
    private String pharmacyName;

    @Override
    public Stream<MedicineDto> loadData() {
        return this.fetchCategories().stream()
                .filter(categoryDto -> categoryDto.getName().equals("Медикаменти"))
                .map(CategoryDto::getChildren)
                .flatMap(Collection::stream)
                .map(CategoryDto::getSlug)
                .flatMap(this::fetchMedicinesByCategory);
    }

    private List<CategoryDto> fetchCategories() {
        return this.dsClient.get().uri(categoriesFetchUrl)
                .retrieve().bodyToMono(new ParameterizedTypeReference<List<CategoryDto>>() {
                }).block();
    }

    private Stream<MedicineDto> fetchMedicinesByCategory(String category) {

        Long pageSize = 100L;

        FilterRequest filterRequest = FilterRequest.builder()
                .page(1L)
                .per(100L)
                .build();

        DSMedicinesResponse dsMedicinesResponse = this.dsClient.post()
                .uri(categoryPath + "/" + category)
                .body(Mono.just(filterRequest), FilterRequest.class)
                .retrieve()
                .bodyToMono(DSMedicinesResponse.class)
                .block();

        Long total;
        if (dsMedicinesResponse != null) {
            total = dsMedicinesResponse.getTotal();
            long pageCount = total / pageSize;

            List<DSMedicinesResponse> responseList = new ArrayList<>();
            long page = 1L;
            while (page <= pageCount) {
                DSMedicinesResponse medicinesResponse = this.dsClient.post()
                        .uri(categoryPath + "/" + category)
                        .body(Mono.just(FilterRequest.builder()
                                .page(page)
                                .per(pageSize)
                                .build()), FilterRequest.class)
                        .retrieve()
                        .bodyToMono(DSMedicinesResponse.class)
                        .block();
                responseList.add(medicinesResponse);
                page++;
            }
            return responseList.stream().map(DSMedicinesResponse::getProducts)
                    .flatMap(Collection::stream)
                    .map(this::mapToMedicineDto);
        }
        return Stream.of();


    }

    private MedicineDto mapToMedicineDto(DSMedicineDto dsMedicineDto) {
        return MedicineDto.builder()
                .externalId(dsMedicineDto.getId())
                .price(dsMedicineDto.getPrice())
                .title(dsMedicineDto.getName())
                .pharmacyName(pharmacyName)
                .build();
    }

}
