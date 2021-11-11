package com.eleks.academy.pharmagator.controllers;

import org.dbunit.database.DatabaseDataSourceConnection;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class PriceControllerIT {

    private MockMvc mockMvc;
    private DatabaseDataSourceConnection dataSourceConnection;

    private final String uri = "/prices";

    @Autowired
    public void setComponents(final MockMvc mockMvc,
                              final DataSource dataSource) throws SQLException {
        this.mockMvc = mockMvc;
        this.dataSourceConnection = new DatabaseDataSourceConnection(dataSource);
    }

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @AfterEach
    void tearDown(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "prices", "pharmacies", "medicines");
    }

    @Test
    public void contextLoads() {
    }

    @Test
    public void findAllPrices_findIds_ok() throws Exception {
        try {
            DatabaseOperation.REFRESH.execute(this.dataSourceConnection, readDataset());

            this.mockMvc.perform(MockMvcRequestBuilders.get(uri))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(jsonPath("$[*].externalId",
                            Matchers.hasItems("2021110701","2021110702")));
        } finally {
            this.dataSourceConnection.close();
        }
    }

    @Test
    public void findPriceById_ok() throws Exception {
        final int pharmacyId = 2021110701;
        final int medicineId = 2021110701;
        try {
            DatabaseOperation.REFRESH.execute(this.dataSourceConnection, readDataset());

            this.mockMvc.perform(MockMvcRequestBuilders.get(uri +"/pharmacyId/{pharmacyId}/medicineId/{medicineId}", pharmacyId, medicineId))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(jsonPath("$.price").value("150.0"))
                    .andExpect(jsonPath("$.externalId").value("2021110701"));
        } finally {
            this.dataSourceConnection.close();
        }
    }

    @Test
    public void findPriceById_isNotFound() throws Exception {
        final int pharmacyId = 2021110703;
        final int medicineId = 2021110703;
        try {
            DatabaseOperation.CLEAN_INSERT.execute(this.dataSourceConnection, readDataset());

            this.mockMvc.perform(MockMvcRequestBuilders.get(uri +"/pharmacyId/{pharmacyId}/medicineId/{medicineId}", pharmacyId, medicineId))
                    .andExpect(MockMvcResultMatchers.status().isNotFound());
        } finally {
            this.dataSourceConnection.close();
        }
    }

    @Test
    void updatePriceById_isOk() throws Exception {
        final int pharmacyId = 2021110701;
        final int medicineId = 2021110701;
        try {
            DatabaseOperation.REFRESH.execute(this.dataSourceConnection, readDataset());

            this.mockMvc.perform(MockMvcRequestBuilders.put(uri +"/pharmacyId/{pharmacyId}/medicineId/{medicineId}", pharmacyId, medicineId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"price\": \"180.0\", \"externalId\": \"2021110701\"}"))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(jsonPath("$.price").value("180.0"));
        } finally {
            this.dataSourceConnection.close();
        }
    }

    @Test
    void deletePriceById_isNoContent() throws Exception {
        final int pharmacyId = 2021110701;
        final int medicineId = 2021110701;
        try {
            DatabaseOperation.REFRESH.execute(this.dataSourceConnection, readDataset());

            this.mockMvc.perform(MockMvcRequestBuilders.delete(uri +"/pharmacyId/{pharmacyId}/medicineId/{medicineId}", pharmacyId, medicineId))
                    .andExpect(MockMvcResultMatchers.status().isNoContent());
        } finally {
            this.dataSourceConnection.close();
        }
    }

    private IDataSet readDataset() throws DataSetException, IOException {
        try (var resource = getClass()
                .getResourceAsStream("PriceControllerIT_dataset.xml")) {
            return new FlatXmlDataSetBuilder()
                    .build(resource);
        }
    }

}