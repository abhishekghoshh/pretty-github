package com.pretty.github.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

@Service
public class RestCall {
	private RestTemplate restTemplate = new RestTemplate();

	public <T> ResponseEntity<T> get(String url, Class<T> type) {
		try {
			return restTemplate.getForEntity(url, type);
		} catch (HttpStatusCodeException ex) {
			return ResponseEntity.status(ex.getStatusCode()).build();
		} catch (Exception ex) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
}
