package com.eleks.academy.pharmagator.dataproviders.dto.anc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ANCMedicinesResponse {

    private Long total;

    private List<ANCMedicineDto> products;

}
