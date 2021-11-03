package com.eleks.academy.pharmagator.dataproviders.dto.input;

import lombok.Data;

import javax.validation.constraints.Min;
import java.math.BigDecimal;

@Data
public class PriceDto {

    @Min(value = 0)
    private BigDecimal price;

    private String externalId;

}
