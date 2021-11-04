package com.eleks.academy.pharmagator.dataproviders.dto.anc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ANCSubcategoryDto {

    private String name;

    private String link;

    private List<ANCSubcategoryDto> subcategories;

}
