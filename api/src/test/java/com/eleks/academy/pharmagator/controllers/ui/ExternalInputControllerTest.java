package com.eleks.academy.pharmagator.controllers.ui;

import com.eleks.academy.pharmagator.dataproviders.dto.MedicineDto;
import com.eleks.academy.pharmagator.services.ImportService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@WebMvcTest(ExternalInputController.class)
class ExternalInputControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final String BASE_URI = "/ui/input";

    @MockBean
    private ImportService importService;

    @Test
    void getInputPage_isOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URI))
                .andExpect(status().isOk())
                .andExpect(view().name("addMedicine"))
                .andExpect(model().attribute("medicineDto", new MedicineDto()))
                .andDo(print());
    }

    @Test
    void getInputResult_isOk() throws Exception {
        MedicineDto medicineDto = MedicineDto.builder()
                .title("Aspirin")
                .price(new BigDecimal(46.6))
                .externalId("838949")
                .pharmacyName("Veselka")
                .build();

        doNothing().when(importService).storeToDatabase(medicineDto);

        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URI)
                        .flashAttr("medicineDto", medicineDto))
                .andExpect(status().isOk())
                .andExpect(view().name("result"))
                .andExpect(model().attribute("medicineDto", medicineDto))
                .andDo(print());

        verify(importService, times(1)).storeToDatabase(medicineDto);
    }

}
