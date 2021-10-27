package com.eleks.academy.pharmagator.services.impl;

import com.eleks.academy.pharmagator.entities.Pharmacy;
import com.eleks.academy.pharmagator.repositories.PharmacyRepository;
import com.eleks.academy.pharmagator.services.DummyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class DummyServiceImpl implements DummyService {

    private final PharmacyRepository pharmacyRepository;

    @Override
    public List<Pharmacy> findAllEven(){
        UnaryOperator<List<Pharmacy>> calculate = this.filter(p -> p.getId() % 2 ==0);
        return calculate.apply(this.pharmacyRepository.findAll());
    }

    @Override
    public List<Pharmacy> findAllOdd(){
        return /*this.pharmacyRepository.findAll()
                .stream()
                .filter(pharmacy -> pharmacy.getId()%2 == 1)
                .collect(Collectors.toList());*/
        List.of();
    }

    private UnaryOperator<List<Pharmacy>> filter(Predicate<Pharmacy> predicate){
        return collection -> collection
                .stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }


}
