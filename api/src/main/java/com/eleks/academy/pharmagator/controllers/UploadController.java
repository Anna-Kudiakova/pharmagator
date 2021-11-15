package com.eleks.academy.pharmagator.controllers;

import com.eleks.academy.pharmagator.exceptions.InvalidFileFormatException;
import com.eleks.academy.pharmagator.services.CsvService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("medicines/csv")
@RequiredArgsConstructor
public class UploadController {

    private final CsvService csvService;

    @PostMapping("/upload")
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file){
        if(file.isEmpty())
            return ResponseEntity.noContent().build();
        String message = null;
        try {
            message = csvService.save(file);
        } catch (InvalidFileFormatException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(message);
    }
}
