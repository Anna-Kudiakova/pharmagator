package com.eleks.academy.pharmagator.dataproviders.dto.ds;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
public class FilterRequest {

	private Long page;
	private Long per;

}
