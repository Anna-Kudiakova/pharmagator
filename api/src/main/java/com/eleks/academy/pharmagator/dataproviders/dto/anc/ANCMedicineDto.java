package com.eleks.academy.pharmagator.dataproviders.dto.anc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ANCMedicineDto {

    private String id;

    private String name;

    private BigDecimal price;

    private String link;

}
