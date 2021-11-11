package com.eleks.academy.pharmagator.repositories;

import com.eleks.academy.pharmagator.entities.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MedicineRepository extends JpaRepository<Medicine, Long> {

    Optional<Medicine> findByTitle(String title);

}
