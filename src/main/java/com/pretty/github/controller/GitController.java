package com.pretty.github.controller;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@Deprecated
public class GitController {
	private RestTemplate restTemplate = new RestTemplate();

	private String userUrl = "https://api.github.com/users/typicalitguy";
	private String reposUrl = "https://api.github.com/users/typicalitguy/repos";
	private String mainFolderUrl = "https://api.github.com/repos/typicalitguy/Algorithm-and-data-structure-in-java/contents";
	private String subFolderUrl = "https://api.github.com/repos/typicalitguy/Algorithm-and-data-structure-in-java/contents/src/problems/array";
	private String itemUrl = "https://api.github.com/repos/typicalitguy/Algorithm-and-data-structure-in-java/contents/src/problems/array/BuyAndSellStock.java?ref=master";
	private String rawUrl = "https://raw.githubusercontent.com/typicalitguy/Algorithm-and-data-structure-in-java/master/src/problems/array/BuyAndSellStock.java";

	@GetMapping("/git/main")
	public ResponseEntity<List> mainFolder() {
		return restTemplate.getForEntity(mainFolderUrl, List.class);
	}

	@GetMapping("/git/sub")
	public ResponseEntity<List> subFolder() {
		return restTemplate.getForEntity(subFolderUrl, List.class);
	}

	@GetMapping("/git/item")
	public ResponseEntity<Map> item() {
		ResponseEntity<Map> raw = restTemplate.getForEntity(itemUrl, Map.class);
		String encodedString = (String) raw.getBody().get("content");
		String content = new String(Base64.getDecoder().decode(encodedString), StandardCharsets.UTF_8);
		System.out.println(content);
		return raw;
	}

	@GetMapping("/git/raw")
	public ResponseEntity<String> rawContent() {
		ResponseEntity<String> raw = restTemplate.getForEntity(rawUrl, String.class);
		String content = raw.getBody();
		System.out.println(content);
		return raw;
	}
}
