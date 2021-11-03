package com.eleks.academy.pharmagator.entities;

import lombok.*;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PriceId implements Serializable {

    private Long pharmacyId;

    private Long medicineId;
}
