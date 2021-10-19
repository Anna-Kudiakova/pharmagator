package com.eleks.academy.pharmagator.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.codec.CodecCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsonorg.JsonOrgModule;


import java.util.Collection;

@Configuration
public class DataProvidersConfig {

	@Value("${pharmagator.data-providers.apteka-ds.url}")
	private String pharmacyDSBaseUrl;

	@Value("${pharmagator.data-providers.apteka-rozetka.url}")
	private String pharmacyRozetkaBaseUrl;

	@Bean(name = "pharmacyDSWebClient")
	public WebClient pharmacyDSWebClient() {
		ObjectMapper mapper = new ObjectMapper().registerModule(new JsonOrgModule());
		return WebClient.builder()
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
				.baseUrl(pharmacyDSBaseUrl)
				.build();

	}

	@Bean(name = "pharmacyRozetkaWebClient")
    //@Primary
	public WebClient pharmacyRozetkaWebClient() {
		return WebClient.builder()
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
				.baseUrl(pharmacyRozetkaBaseUrl)
				.build();
	}


}
