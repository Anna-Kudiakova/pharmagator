package com.eleks.academy.pharmagator.controllers.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PharmacyDto {

    private int id;
    private String name;
    private String medicineLinkTemplate;

}
