package com.eleks.academy.pharmagator.services;

import com.eleks.academy.pharmagator.entities.Pharmacy;

import java.util.List;

public interface DummyService {

    public List<Pharmacy> findAllEven();

    public List<Pharmacy> findAllOdd();
}
