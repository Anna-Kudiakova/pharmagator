package com.eleks.academy.pharmagator.entities;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class PriceId implements Serializable {
    private long pharmacyId;
    private long medicineId;

}
