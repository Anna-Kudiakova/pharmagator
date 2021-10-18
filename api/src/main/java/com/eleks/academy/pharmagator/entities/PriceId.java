package com.eleks.academy.pharmagator.entities;

import lombok.*;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class PriceId implements Serializable {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pharmacyId;
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long medicineId;

}
