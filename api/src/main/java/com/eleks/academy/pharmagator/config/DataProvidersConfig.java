package com.eleks.academy.pharmagator.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class DataProvidersConfig {

	@Value("${pharmagator.data-providers.apteka-ds.url}")
	private String pharmacyDSBaseUrl;

	@Value("${pharmagator.data-providers.apteka-rozetka.url}")
	private String pharmacyRozetkaBaseUrl;

	@Bean
	public WebClient pharmacyDSWebClient() {
		return WebClient.builder()
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
				.baseUrl(pharmacyDSBaseUrl)
				.build();
	}

	@Bean
	@Primary
	public WebClient pharmacyRozetkaWebClient() {
		return WebClient.builder()
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
				.baseUrl(pharmacyRozetkaBaseUrl)
				.build();
	}


}
