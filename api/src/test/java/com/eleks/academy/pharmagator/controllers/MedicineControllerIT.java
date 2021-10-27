package com.eleks.academy.pharmagator.controllers;

import org.dbunit.database.DatabaseDataSourceConnection;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class MedicineControllerIT {

    private MockMvc mockMvc;
    private DatabaseDataSourceConnection dataSourceConnection;

    private final String urlTemplate = "/medicines";

    @Autowired
    public void setComponents(final MockMvc mockMvc,
                              final DataSource dataSource) throws SQLException {
        this.mockMvc = mockMvc;
        this.dataSourceConnection = new DatabaseDataSourceConnection(dataSource);
    }

    @Test
    public void findAllMedicines_findIds_ok() throws Exception {
        try {
            DatabaseOperation.REFRESH.execute(this.dataSourceConnection, readDataset());

            this.mockMvc.perform(MockMvcRequestBuilders.get(urlTemplate))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(jsonPath("$[*].id",
                            Matchers.hasItems(2021103101, 2021103102)));
        } finally {
            this.dataSourceConnection.close();
        }
    }

    @Test
    public void findMedicinesById_ok() throws Exception {
        final int medicineId = 2021103101;
        try {
            DatabaseOperation.REFRESH.execute(this.dataSourceConnection, readDataset());

            this.mockMvc.perform(MockMvcRequestBuilders.get(urlTemplate+"/{id}", medicineId))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(jsonPath("title",
                            Matchers.equalToIgnoringCase("MedicineControllerIT_name1")));
        } finally {
            this.dataSourceConnection.close();
        }
    }

    @Test
    public void findMedicinesById_notExisted_notFound() throws Exception {
        final int medicineId = 2021103103;
        try {
            DatabaseOperation.REFRESH.execute(this.dataSourceConnection, readDataset());

            this.mockMvc.perform(MockMvcRequestBuilders.get(urlTemplate+"/{id}", medicineId))
                    .andExpect(MockMvcResultMatchers.status().isNotFound());
        } finally {
            this.dataSourceConnection.close();
        }
    }

    @Test
    @Transactional
    void createMedicine_isCreated() throws Exception {

        try {
            DatabaseOperation.REFRESH.execute(this.dataSourceConnection, readDataset());

            this.mockMvc.perform(MockMvcRequestBuilders.post(urlTemplate)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"title\": \"MedicineControllerIT_name3\"}"))
                    .andExpect(MockMvcResultMatchers.status().isCreated())
                    .andExpect(jsonPath("$.title").value("MedicineControllerIT_name3"));
        } finally {
            this.dataSourceConnection.close();
        }

    }

    @Test
    void updateMedicineById_isOk() throws Exception {
        final int medicineId = 2021103101;
        try {
            DatabaseOperation.REFRESH.execute(this.dataSourceConnection, readDataset());

            this.mockMvc.perform(MockMvcRequestBuilders.put(urlTemplate+"/{id}", medicineId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"title\": \"new-name\"}"))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(jsonPath("$.title").value("new-name"));
        } finally {
            this.dataSourceConnection.close();
        }
    }

    @Test
    void deleteMedicineById_isOk() throws Exception {
        final int medicineId = 2021103101;
        try {
            DatabaseOperation.REFRESH.execute(this.dataSourceConnection, readDataset());

            this.mockMvc.perform(MockMvcRequestBuilders.delete(urlTemplate+"/{id}", medicineId))
                    .andExpect(MockMvcResultMatchers.status().isOk());
        } finally {
            this.dataSourceConnection.close();
        }
    }


    private IDataSet readDataset() throws DataSetException, IOException {

        try (var resource = getClass()
                .getResourceAsStream("MedicineControllerIT_dataset.xml")) {
            return new FlatXmlDataSetBuilder()
                    .build(resource);
        }
    }

}
