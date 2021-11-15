package com.eleks.academy.pharmagator.view;

import lombok.Data;

@Data
public class CsvControllerResponseMessage {

    private String message;

    public CsvControllerResponseMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}