package com.eleks.academy.pharmagator.dataproviders.dto.ds;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DSMedicinesResponse {

    private Long total;

    private List<DSMedicineDto> products;

}
