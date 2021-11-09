package com.eleks.academy.pharmagator.controllers;

import com.eleks.academy.pharmagator.dataproviders.dto.input.MedicineDto;
import com.eleks.academy.pharmagator.entities.Medicine;
import com.eleks.academy.pharmagator.services.MedicineService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = MedicineController.class)
@ActiveProfiles("test")
public class MedicineControllerTest {

    private final String urlTemplate = "/medicines";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private static ObjectMapper objectMapper;

    @MockBean
    private MedicineService medicineService;

    private static Medicine testMedicine;
    private static ModelMapper modelMapper;



    @BeforeAll
    static void setUp() {
        objectMapper = new ObjectMapper();
        modelMapper = new ModelMapper();
        testMedicine = Medicine.builder()
                .id(1L)
                .title("Atoksil")
                .build();
    }

    @Test
    void findAllMedicine_isOk() throws Exception{

        List<Medicine> medicineList = List.of(testMedicine);
        when(medicineService.findAll()).thenReturn(medicineList);

        this.mockMvc.perform(get(urlTemplate))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(medicineList.size())))
                .andExpect(jsonPath("$[0].title").value(medicineList.get(0).getTitle()));

        verify(medicineService, times(1)).findAll();
    }

    @Test
    void findMedicineById_isOk() throws Exception{

        final Long pharmacyId = 1L;
        when(medicineService.findById(pharmacyId)).thenReturn(Optional.ofNullable(testMedicine));

        this.mockMvc.perform(get(urlTemplate+"/{id}", pharmacyId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(testMedicine.getTitle()));

        verify(medicineService, times(1)).findById(pharmacyId);

    }

    @Test
    void findMedicineById_isNotFound() throws Exception {

        final Long medicineId = 2L;
        when(medicineService.findById(medicineId)).thenReturn(Optional.empty());

        this.mockMvc.perform(get(urlTemplate+"/{id}", medicineId))
                .andExpect(status().isNotFound());

        verify(medicineService, times(1)).findById(2L);
    }



    @Test
    void createMedicine_isOk() throws Exception{

        when(medicineService.save(Mockito.any(MedicineDto.class))).thenReturn(testMedicine);

        this.mockMvc.perform(post(urlTemplate)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testMedicine)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testMedicine.getId()))
                .andExpect(jsonPath("$.title").value(testMedicine.getTitle()));

        verify(medicineService, times(1)).save(modelMapper.map(testMedicine, MedicineDto.class));

    }

    @Test
    void updateMedicineById_isOk() throws Exception{

        final Long medicineId = 1L;
        when(medicineService.update(eq(medicineId), any(MedicineDto.class))).thenReturn(Optional.ofNullable(testMedicine));

        this.mockMvc.perform(put(urlTemplate+"/{id}", medicineId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testMedicine)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testMedicine.getId()))
                .andExpect(jsonPath("$.title").value(testMedicine.getTitle()));

        verify(medicineService, times(1))
                .update(medicineId, modelMapper.map(testMedicine, MedicineDto.class));
    }

    @Test
    void updateMedicineById_isNotFound() throws Exception{

        final Long medicineId = 2L;
        when(medicineService.findById(medicineId)).thenReturn(Optional.empty());

        Medicine testMedicine2 = Medicine.builder()
                .id(2L)
                .title("Citramon")
                .build();

        this.mockMvc.perform(put(urlTemplate+"/{id}", medicineId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testMedicine2)))
                .andExpect(status().isNotFound());

        verify(medicineService, times(1))
                .update(medicineId, modelMapper.map(testMedicine2, MedicineDto.class));
    }

    @Test
    void deleteMedicineById_isNoContent() throws Exception {

        final Long medicineId = 1L;

        doNothing().when(medicineService).delete(medicineId);
        mockMvc.perform(delete(urlTemplate+"/{id}", medicineId))
                .andExpect(status().isNoContent());

        verify(medicineService, times(1)).delete(medicineId);

    }



}
