package com.eleks.academy.pharmagator.dataproviders.dto.rozetka;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RozetkaMedicineDto {

    private Long id;

    private String mpath;

    private String title;

    private BigDecimal price;

}
