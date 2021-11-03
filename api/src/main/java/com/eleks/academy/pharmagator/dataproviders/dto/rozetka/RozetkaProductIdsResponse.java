package com.eleks.academy.pharmagator.dataproviders.dto.rozetka;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class RozetkaProductIdsResponse {

    private RozetkaProductIdsResponseData data;
}
