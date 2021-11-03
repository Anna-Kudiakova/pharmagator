package com.eleks.academy.pharmagator.entities;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "prices")
@IdClass(PriceId.class)
public class Price {

    @Id
    private Long pharmacyId;

    @Id
    private Long medicineId;

    private BigDecimal price;

    private String externalId;

    @Column(insertable = false, updatable = false)
    private Instant updatedAt;

}
