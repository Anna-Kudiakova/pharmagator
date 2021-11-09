package com.eleks.academy.pharmagator.controllers;

import com.eleks.academy.pharmagator.dataproviders.dto.input.PharmacyDto;
import com.eleks.academy.pharmagator.entities.Pharmacy;
import com.eleks.academy.pharmagator.services.PharmacyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = PharmacyController.class)
@ActiveProfiles("test")
public class PharmacyControllerTest {

    private final String urlTemplate = "/pharmacies";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private static ObjectMapper objectMapper;

    @MockBean
    private PharmacyService pharmacyService;

    private static Pharmacy testPharmacy;
    private static ModelMapper modelMapper;



    @BeforeAll
    static void setUp() {
        objectMapper = new ObjectMapper();
        modelMapper = new ModelMapper();
        testPharmacy = Pharmacy.builder()
                .id(1L)
                .name("Askorbinka")
                .medicineLinkTemplate("https://askorbinka.com.ua/ua")
                .build();
    }

    @Test
    void findAllPharmacies_isOk() throws Exception{

        List<Pharmacy> pharmacyList = List.of(testPharmacy);
        when(pharmacyService.findAll()).thenReturn(pharmacyList);

        this.mockMvc.perform(get(urlTemplate))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(pharmacyList.size())))
                .andExpect(jsonPath("$[0].name").value(pharmacyList.get(0).getName()));

        verify(pharmacyService, times(1)).findAll();

    }

    @Test
    void findPharmacyById_isOk() throws Exception{

        final Long pharmacyId = 1L;
        when(pharmacyService.findById(pharmacyId)).thenReturn(Optional.ofNullable(testPharmacy));

        this.mockMvc.perform(get(urlTemplate+"/{id}", pharmacyId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(testPharmacy.getName()))
                .andExpect(jsonPath("$.medicineLinkTemplate").value(testPharmacy.getMedicineLinkTemplate()));

        verify(pharmacyService, times(1)).findById(pharmacyId);
    }

    @Test
    void findPharmacyById_isNotFound() throws Exception {

        final Long pharmacyId = 2L;
        when(pharmacyService.findById(pharmacyId)).thenReturn(Optional.empty());

        this.mockMvc.perform(get(urlTemplate+"/{id}", pharmacyId))
                .andExpect(status().isNotFound());

        verify(pharmacyService, times(1)).findById(pharmacyId);
    }



    @Test
    void createPharmacy_isOk() throws Exception{

        when(pharmacyService.save(Mockito.any(PharmacyDto.class))).thenReturn(testPharmacy);

        this.mockMvc.perform(post(urlTemplate)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testPharmacy)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testPharmacy.getId()))
                .andExpect(jsonPath("$.name").value(testPharmacy.getName()))
                .andExpect(jsonPath("$.medicineLinkTemplate").value(testPharmacy.getMedicineLinkTemplate()));

        verify(pharmacyService, times(1)).save(modelMapper.map(testPharmacy, PharmacyDto.class));

    }

    @Test
    void updatePharmacy_isOk() throws Exception{

        final Long pharmacyId = 1L;
        when(pharmacyService.update(eq(pharmacyId), any(PharmacyDto.class))).thenReturn(Optional.ofNullable(testPharmacy));

        this.mockMvc.perform(put(urlTemplate+"/{id}", pharmacyId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testPharmacy)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testPharmacy.getId()))
                .andExpect(jsonPath("$.name").value(testPharmacy.getName()))
                .andExpect(jsonPath("$.medicineLinkTemplate").value(testPharmacy.getMedicineLinkTemplate()));

        verify(pharmacyService, times(1))
                .update(pharmacyId, modelMapper.map(testPharmacy, PharmacyDto.class));
    }

    @Test
    void updatePharmacy_isNotFound() throws Exception{

        final Long pharmacyId = 2L;
        when(pharmacyService.findById(pharmacyId)).thenReturn(Optional.empty());

        Pharmacy testPharmacy2 = Pharmacy.builder()
                .id(2L)
                .name("Tabletka")
                .medicineLinkTemplate("https://tabletka.com.ua/ua")
                .build();

        this.mockMvc.perform(put(urlTemplate+"/{id}", pharmacyId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testPharmacy2)))
                .andExpect(status().isNotFound());

        verify(pharmacyService, times(1))
                .update(pharmacyId, modelMapper.map(testPharmacy2, PharmacyDto.class));
    }

    @Test
    void deletePharmacyById_isNoContent() throws Exception {

        final Long pharmacyId = 1L;

        doNothing().when(pharmacyService).deleteById(pharmacyId);
        mockMvc.perform(delete(urlTemplate+"/{id}", pharmacyId))
                .andExpect(status().isNoContent());

        verify(pharmacyService, times(1)).deleteById(pharmacyId);

    }



}
