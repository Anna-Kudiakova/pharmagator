package com.eleks.academy.pharmagator.dataproviders.dto.liki24;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Liki24MedicineDto {

    private String productId;
    private String name;
    private BigDecimal price;
    private String manufacturer;

}
