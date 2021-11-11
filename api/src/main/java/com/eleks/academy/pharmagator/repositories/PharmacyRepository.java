package com.eleks.academy.pharmagator.repositories;

import com.eleks.academy.pharmagator.entities.Pharmacy;
import com.eleks.academy.pharmagator.projections.PharmacyLight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PharmacyRepository extends JpaRepository<Pharmacy, Long> {

    @Query("SELECT pharmacy FROM Pharmacy pharmacy")
    List<PharmacyLight> findAllLight();

    Optional<Pharmacy> findByName(String name);

}
