package com.eleks.academy.pharmagator.controllers.ui;

import com.eleks.academy.pharmagator.dataproviders.dto.MedicineDto;
import com.eleks.academy.pharmagator.services.ImportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/ui/input")
public class ExternalInputController {

    private final ImportService importService;

    @GetMapping
    public String getInputPage(Model model) {
        model.addAttribute("medicineDto", new MedicineDto());
        return "addMedicine";
    }

    @PostMapping
    public String getInputResult(@Valid @ModelAttribute("medicineDto") MedicineDto medicineDto, Model model) {
        importService.storeToDatabase(medicineDto);
        model.addAttribute("medicineDto", medicineDto);
        return "result";
    }

}
