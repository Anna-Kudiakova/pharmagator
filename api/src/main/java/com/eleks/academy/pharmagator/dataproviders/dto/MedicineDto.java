package com.eleks.academy.pharmagator.dataproviders.dto;

import com.univocity.parsers.annotations.Parsed;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class MedicineDto {

    @Parsed(field = "name")
    private String title;

    @Parsed
    private BigDecimal price;

    @Parsed(field = "link")
    private String externalId;

    @Parsed(field = "pharmacy")
    private String pharmacyName;

}
