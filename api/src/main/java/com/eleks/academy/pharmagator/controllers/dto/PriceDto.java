package com.eleks.academy.pharmagator.controllers.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PriceDto {

    private Long pharmacyId;
    private Long medicineId;
    private BigDecimal price;
    private String externalId;
    private Instant updatedAt;
}
