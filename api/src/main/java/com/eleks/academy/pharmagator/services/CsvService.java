package com.eleks.academy.pharmagator.services;

import com.eleks.academy.pharmagator.exceptions.InvalidFileFormatException;
import org.springframework.web.multipart.MultipartFile;

public interface CsvService {

    String save(MultipartFile file) throws InvalidFileFormatException;
}
