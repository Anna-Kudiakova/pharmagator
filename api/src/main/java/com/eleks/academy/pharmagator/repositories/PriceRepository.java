package com.eleks.academy.pharmagator.repositories;

import com.eleks.academy.pharmagator.entities.Price;
import com.eleks.academy.pharmagator.entities.PriceId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PriceRepository extends JpaRepository<Price, PriceId> {

    List<Price> findByMedicineId(Long medicineId);

    List<Price> findByPharmacyId(Long pharmacyId);

    List<Price> findByMedicineIdAndPharmacyId(Long medicineId, Long pharmacyId);
}
