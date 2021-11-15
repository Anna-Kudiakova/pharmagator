package com.eleks.academy.pharmagator.services;


import com.eleks.academy.pharmagator.dataproviders.dto.MedicineDto;
import com.eleks.academy.pharmagator.exceptions.InvalidFileFormatException;
import com.univocity.parsers.common.processor.BeanListProcessor;
import com.univocity.parsers.csv.CsvParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class CsvServiceImpl implements CsvService {

    private final CsvParser parser;

    private final BeanListProcessor<MedicineDto> rowProcessor;

    private final static String TYPE = "text/csv";


    private List<MedicineDto> parse(InputStream inputStream) {
        try (Reader inputReader = new InputStreamReader(inputStream)) {
            parser.parse(inputReader);
            return rowProcessor.getBeans();
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public String save(MultipartFile file) throws InvalidFileFormatException {
        if (Objects.equals(file.getContentType(), TYPE)) {
            try {
                InputStream inputStream = file.getInputStream();
                parse(inputStream).forEach(this::storeToDatabase);
                return "File saved successfully";
            } catch (IOException e) {
                log.error("Failed to read file", e);
            }
        }
        throw new InvalidFileFormatException("Invalid file format");
    }

    private void storeToDatabase(MedicineDto medicineDto) {
        System.out.println(medicineDto+" was uploaded");
    }
}
