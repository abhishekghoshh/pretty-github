package com.pretty.github.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

@Service
public class RestCall {
	
	@Autowired
	private RestTemplate restTemplate;

	public <T> ResponseEntity<T> get(String url, Class<T> type) {
		try {
			return restTemplate.getForEntity(url, type);
		} catch (HttpStatusCodeException ex) {
			return ResponseEntity.status(ex.getStatusCode()).build();
		} catch (Exception ex) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	public <T> ResponseEntity<T> get(String url, HttpEntity<?> httpEntity,
			ParameterizedTypeReference<T> parameterizedTypeReference) {
		try {
			return restTemplate.exchange(url, HttpMethod.GET, httpEntity, parameterizedTypeReference);
		} catch (HttpStatusCodeException ex) {
			return ResponseEntity.status(ex.getStatusCode()).build();
		} catch (Exception ex) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	
}
