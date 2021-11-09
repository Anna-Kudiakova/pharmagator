package com.eleks.academy.pharmagator.controllers;

import com.eleks.academy.pharmagator.dataproviders.dto.input.PriceDto;
import com.eleks.academy.pharmagator.entities.Price;
import com.eleks.academy.pharmagator.services.PriceService;
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

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = PriceController.class)
@ActiveProfiles("test")
public class PriceControllerTest {

    private final String urlTemplate = "/prices";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private static ObjectMapper objectMapper;

    @MockBean
    private PriceService priceService;

    private static Price testPrice;
    private static ModelMapper modelMapper;



    @BeforeAll
    static void setUp() {
        objectMapper = new ObjectMapper();
        modelMapper = new ModelMapper();
        testPrice = Price.builder()
                .pharmacyId(1L)
                .medicineId(1L)
                .price(new BigDecimal(50))
                .externalId("1")
                .build();
    }

    @Test
    void findAllPrices_isOk() throws Exception{

        List<Price> priceList = List.of(testPrice);
        when(priceService.findAll()).thenReturn(priceList);

        this.mockMvc.perform(get(urlTemplate))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(priceList.size())))
                .andExpect(jsonPath("$[0].pharmacyId").value(priceList.get(0).getPharmacyId()))
                .andExpect(jsonPath("$[0].medicineId").value(priceList.get(0).getMedicineId()))
                .andExpect(jsonPath("$[0].price").value(priceList.get(0).getPrice()))
                .andExpect(jsonPath("$[0].externalId").value(priceList.get(0).getExternalId()));

        verify(priceService, times(1)).findAll();

    }

    @Test
    void findPriceById_isOk() throws Exception{

        final Long pharmacyId = 1L;
        final Long medicineId = 1L;
        when(priceService.findById(pharmacyId, medicineId)).thenReturn(Optional.ofNullable(testPrice));

        this.mockMvc.perform(get(urlTemplate+"/pharmacyId/{pharmacyId}/medicineId/{medicineId}", pharmacyId, medicineId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.price").value(testPrice.getPrice()))
                .andExpect(jsonPath("$.externalId").value(testPrice.getExternalId()));

        verify(priceService, times(1)).findById(pharmacyId, medicineId);
    }

    @Test
    void findPriceById_isNotFound() throws Exception {

        final Long pharmacyId = 2L;
        final Long medicineId = 1L;
        when(priceService.findById(pharmacyId, medicineId)).thenReturn(Optional.empty());

        this.mockMvc.perform(get(urlTemplate+"/pharmacyId/{pharmacyId}/medicineId/{medicineId}", pharmacyId, medicineId))
                .andExpect(status().isNotFound());

        verify(priceService, times(1)).findById(pharmacyId, medicineId);
    }



    @Test
    void createPrice_isOk() throws Exception{

        when(priceService.save(Mockito.any(PriceDto.class))).thenReturn(testPrice);

        this.mockMvc.perform(post(urlTemplate)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testPrice)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pharmacyId").value(testPrice.getPharmacyId()))
                .andExpect(jsonPath("$.medicineId").value(testPrice.getMedicineId()))
                .andExpect(jsonPath("$.price").value(testPrice.getPrice()))
                .andExpect(jsonPath("$.externalId").value(testPrice.getExternalId()));

        verify(priceService, times(1)).save(modelMapper.map(testPrice, PriceDto.class));

    }

    @Test
    void updatePrice_isOk() throws Exception{

        final Long pharmacyId = 1L;
        final Long medicineId = 1L;
        when(priceService.update(eq(pharmacyId), eq(medicineId), any(PriceDto.class))).thenReturn(Optional.ofNullable(testPrice));

        this.mockMvc.perform(put(urlTemplate+"/pharmacyId/{pharmacyId}/medicineId/{medicineId}", pharmacyId, medicineId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testPrice)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.price").value(testPrice.getPrice()))
                .andExpect(jsonPath("$.externalId").value(testPrice.getExternalId()));

        verify(priceService, times(1))
                .update(pharmacyId, medicineId, modelMapper.map(testPrice, PriceDto.class));
    }

    @Test
    void updatePrice_isNotFound() throws Exception{

        final Long pharmacyId = 2L;
        final Long medicineId = 1L;
        when(priceService.findById(pharmacyId, medicineId)).thenReturn(Optional.empty());

        Price testPrice2 = Price.builder()
                .pharmacyId(2L)
                .medicineId(1L)
                .price(new BigDecimal(80))
                .externalId("2")
                .build();

        this.mockMvc.perform(put(urlTemplate+"/pharmacyId/{pharmacyId}/medicineId/{medicineId}", pharmacyId, medicineId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testPrice2)))
                .andExpect(status().isNotFound());

        verify(priceService, times(1))
                .update(pharmacyId, medicineId, modelMapper.map(testPrice2, PriceDto.class));
    }

    @Test
    void deletePriceById_isNoContent() throws Exception {

        final Long pharmacyId = 1L;
        final Long medicineId = 1L;

        doNothing().when(priceService).deleteById(pharmacyId, medicineId);
        mockMvc.perform(delete(urlTemplate+"/pharmacyId/{pharmacyId}/medicineId/{medicineId}", pharmacyId, medicineId))
                .andExpect(status().isNoContent());

        verify(priceService, times(1)).deleteById(pharmacyId, medicineId);

    }



}