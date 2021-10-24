package com.eleks.academy.pharmagator.dataproviders;


import com.eleks.academy.pharmagator.dataproviders.dto.MedicineDto;
import com.eleks.academy.pharmagator.dataproviders.dto.rozetka.RozetkaMedicineDto;
import com.eleks.academy.pharmagator.dataproviders.dto.rozetka.RozetkaMedicineResponse;
import com.eleks.academy.pharmagator.dataproviders.dto.rozetka.RozetkaProductIdsResponse;
import com.eleks.academy.pharmagator.dataproviders.dto.rozetka.RozetkaProductIdsResponseData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.stream.Stream;

@Service
@Slf4j
@Qualifier("pharmacyRozetkaDataProvider")
public class PharmacyRozetkaDataProvider implements DataProvider {


    private final WebClient rozetkaClient;

    public PharmacyRozetkaDataProvider(@Qualifier("pharmacyRozetkaWebClient") WebClient rozetkaClient) {
        this.rozetkaClient = rozetkaClient;
    }

    @Value("${pharmagator.data-providers.apteka-rozetka.product-ids-fetch-url}")
    private String productIdsFetchUrl;

    @Value("${pharmagator.data-providers.apteka-rozetka.category-id}")
    private String categoryId;

    @Value("${pharmagator.data-providers.apteka-rozetka.sell-status}")
    private String sellStatus;

    @Value("${pharmagator.data-providers.apteka-rozetka.products-fetch-url}")
    private String productsPath;

    @Override
    public Stream<MedicineDto> loadData() {
        RozetkaProductIdsResponseData rozetkaProductIdsResponse;
        Stream<MedicineDto> medicineDtoStream = Stream.of();
        int page = 1;
        do {
            rozetkaProductIdsResponse = this.fetchProductIds(page);
            medicineDtoStream = Stream.concat(medicineDtoStream, this.fetchProducts(rozetkaProductIdsResponse.getIds()));
            page++;
       } while (rozetkaProductIdsResponse.getShowNext() != 0);
        return medicineDtoStream;
    }

    private RozetkaProductIdsResponseData fetchProductIds(int page) {
        RozetkaProductIdsResponse productIds = this.rozetkaClient.get().uri(u -> u
                        .path(productIdsFetchUrl)
                        .queryParam("category_id", categoryId)
                        .queryParam("sell_status", sellStatus)
                        .queryParam("page", page)
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<RozetkaProductIdsResponse>() {
                })
                .block();
        return productIds.getData();
    }

    private Stream<MedicineDto> fetchProducts(List<Long> productIdsList) {
        StringBuilder productIds = new StringBuilder();
        for (int i = 0; i < productIdsList.size(); i++) {
            productIds.append(productIdsList.get(i));
            if (i != (productIdsList.size() - 1))
                productIds.append(",");
        }
        productIds.toString();
        RozetkaMedicineResponse rozetkaMedicineResponse = this.rozetkaClient.get().uri(u -> u
                        .path(productsPath)
                        .queryParam("product_ids", productIds)
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<RozetkaMedicineResponse>() {
                })
                .block();
        if (rozetkaMedicineResponse != null)
            return rozetkaMedicineResponse.getData().stream().map(rozetkaMedicineDto -> mapToMedicineDto(rozetkaMedicineDto));
        return Stream.empty();
    }

    private MedicineDto mapToMedicineDto(RozetkaMedicineDto rozetkaMedicineDto) {
        return MedicineDto.builder()
                .externalId(rozetkaMedicineDto.getId().toString())
                .price(rozetkaMedicineDto.getPrice())
                .title(rozetkaMedicineDto.getTitle())
                .build();
    }
}
