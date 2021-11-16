package com.eleks.academy.pharmagator.config;

import com.eleks.academy.pharmagator.dataproviders.dto.MedicineDto;
import com.univocity.parsers.common.processor.BeanListProcessor;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CsvConfig {

    @Bean(name = "rowProcessor")
    public BeanListProcessor<MedicineDto> rowProcessor() {

        return new BeanListProcessor<>(MedicineDto.class);
    }

    @Bean(name = "parser")
    public CsvParser csvParser(){

        BeanListProcessor<MedicineDto> rowProcessor = rowProcessor();
        CsvParserSettings settings = new CsvParserSettings();
        settings.setHeaderExtractionEnabled(true);
        settings.setProcessor(rowProcessor);
        return new CsvParser(settings);

    }
}
