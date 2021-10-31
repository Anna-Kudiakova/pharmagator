package com.eleks.academy.pharmagator.services;

import com.eleks.academy.pharmagator.entities.Pharmacy;
import com.eleks.academy.pharmagator.repositories.PharmacyRepository;
import com.eleks.academy.pharmagator.services.impl.DummyServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Collectors;
import java.util.stream.LongStream;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.when;



@ExtendWith(MockitoExtension.class)
public class DummyServiceImplTest {

    @Mock
    private PharmacyRepository pharmacyRepository;

    @InjectMocks
    private DummyServiceImpl dummyService;

    @Test
    public void test_ok() {
        when(pharmacyRepository.findAll())
                .thenReturn(
                        LongStream.range(1L,100L)
                                .mapToObj(i -> new Pharmacy(i, String.format("Name %d", i), ""))
                                .collect(Collectors.toList())
                );
        final var evenPharmacies = this.dummyService.findAllEven();

        assertThat(evenPharmacies)
                .matches(list -> list.size() == 49)
                .allMatch(record -> record.getId()%2 == 0);



    }

    @Test
    public void test_repoReturnNull_NPE() {
        when(pharmacyRepository.findAll())
                .thenReturn(null);
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> this.dummyService.findAllEven());
    }

}
