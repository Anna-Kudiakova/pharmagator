package com.eleks.academy.pharmagator.services;

import com.eleks.academy.pharmagator.dataproviders.dto.MedicineDto;
import com.univocity.parsers.common.processor.BeanListProcessor;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public final class CsvHelper {

    private static final String TYPE = "text/csv";

    public static boolean hasCSVFormat(MultipartFile file) {
        return TYPE.equals(file.getContentType());
    }

    public static List<MedicineDto> parse(InputStream inputStream, CsvParser parser) {
        try (Reader inputReader = new InputStreamReader(inputStream)) {
            BeanListProcessor<MedicineDto> rowProcessor = getProcessor();
            parser.parse(inputReader);
            return rowProcessor.getBeans();
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    static CsvParser getCsvParser(BeanListProcessor<MedicineDto> rowProcessor) {
        CsvParserSettings settings = new CsvParserSettings();
        settings.setHeaderExtractionEnabled(true);
        settings.setProcessor(rowProcessor);
        return new CsvParser(settings);
    }

    static BeanListProcessor<MedicineDto> getProcessor() {
        return new BeanListProcessor<>(MedicineDto.class);
    }

}