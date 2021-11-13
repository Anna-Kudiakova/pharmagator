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
public class PharmacyControllerIT {

    private MockMvc mockMvc;
    private DatabaseDataSourceConnection dataSourceConnection;

    private final String URI = "/pharmacies";

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
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "pharmacies");
    }

    @Test
    public void contextLoads() {
    }

    @Test
    public void findAllPharmacies_findIds_ok() throws Exception {
        try {
            DatabaseOperation.REFRESH.execute(this.dataSourceConnection, readDataset());

            this.mockMvc.perform(MockMvcRequestBuilders.get("/pharmacies"))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(jsonPath("$[*].name",
                            Matchers.hasItems("PharmacyControllerIT_name1", "PharmacyControllerIT_name2")));
        } finally {
            this.dataSourceConnection.close();
        }
    }

    @Test
    public void findPharmacyById_ok() throws Exception {
        int pharmacyId = 2021111201;
        try {
            DatabaseOperation.REFRESH.execute(this.dataSourceConnection, readDataset());

            this.mockMvc.perform(MockMvcRequestBuilders.get(URI +"/{id}", pharmacyId))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(jsonPath("$.name").value("PharmacyControllerIT_name1"))
                    .andExpect(jsonPath("$.medicineLinkTemplate").value("PharmacyControllerIT_link1"));
        } finally {
            this.dataSourceConnection.close();
        }
    }

    @Test
    public void findPharmacyById_isNotFound() throws Exception {
        int pharmacyId = 2021111203;
        try {
            DatabaseOperation.REFRESH.execute(this.dataSourceConnection, readDataset());

            this.mockMvc.perform(MockMvcRequestBuilders.get(URI +"/{id}", pharmacyId))
                    .andExpect(MockMvcResultMatchers.status().isNotFound());
        } finally {
            this.dataSourceConnection.close();
        }
    }

    @Test
    void createPharmacy_isOk() throws Exception {
        try {
            DatabaseOperation.REFRESH.execute(this.dataSourceConnection, readDataset());

            this.mockMvc.perform(MockMvcRequestBuilders.post(URI)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"name\": \"PharmacyControllerIT_name3\", \"medicineLinkTemplate\": \"PharmacyControllerIT_link3\" }"))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(jsonPath("$.name").value("PharmacyControllerIT_name3"))
                    .andExpect(jsonPath("$.medicineLinkTemplate").value("PharmacyControllerIT_link3"));
        } finally {
            this.dataSourceConnection.close();
        }

    }

    @Test
    void updatePharmacyById_isOk() throws Exception {
        int pharmacyId = 2021111201;
        try {
            DatabaseOperation.REFRESH.execute(this.dataSourceConnection, readDataset());

            this.mockMvc.perform(MockMvcRequestBuilders.put(URI +"/{id}", pharmacyId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"name\": \"new-name\"}"))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(jsonPath("$.name").value("new-name"));
        } finally {
            this.dataSourceConnection.close();
        }
    }

    @Test
    void deletePharmacyById_isNoContent() throws Exception {
        int pharmacyId = 2021111201;
        try {
            DatabaseOperation.REFRESH.execute(this.dataSourceConnection, readDataset());

            this.mockMvc.perform(MockMvcRequestBuilders.delete(URI +"/{id}", pharmacyId))
                    .andExpect(MockMvcResultMatchers.status().isNoContent());
        } finally {
            this.dataSourceConnection.close();
        }
    }
    private IDataSet readDataset() throws DataSetException, IOException {
        try (var resource = getClass()
                .getResourceAsStream("PharmacyControllerIT_dataset.xml")) {
            return new FlatXmlDataSetBuilder()
                    .build(resource);
        }
    }

}
