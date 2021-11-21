package com.eleks.academy.pharmagator.controllers;

import com.eleks.academy.pharmagator.services.ExportCsvService;
import com.eleks.academy.pharmagator.services.ExportService;
import com.opencsv.CSVWriter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/export")
public class ExportController {

    private final ExportService exportService;

    private final ExportCsvService exportCsvService;

    @SneakyThrows
    @GetMapping("/excel")
    public void export(HttpServletResponse response) {
        XSSFWorkbook workbook = exportService.getExportData();
        ServletOutputStream outputStream = response.getOutputStream();

        response.addHeader("Content-Disposition", "attachment; filename=export.xlsx");

        workbook.write(outputStream);

        workbook.close();
    }

    @GetMapping("/csv")
    public void exportToCsv(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv; charset=UTF-8");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=export.csv";
        response.setHeader(headerKey, headerValue);

        List<String[]> records = exportCsvService.getCsvExportData();
        try {
            CSVWriter writer = new CSVWriter(response.getWriter());
            writer.writeAll(records);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
