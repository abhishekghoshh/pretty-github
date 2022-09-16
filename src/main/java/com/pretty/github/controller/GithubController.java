package com.pretty.github.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GithubController {
	@Autowired
	GithubClient githubClient;

	@SuppressWarnings("rawtypes")
	@GetMapping("/git/validuser/{username}")
	public ResponseEntity isValidUser(@PathVariable String username) {
		return githubClient.isValidUser(username);
	}

	@SuppressWarnings("rawtypes")
	@GetMapping("/git/{username}/repos")
	public ResponseEntity repos(@PathVariable String username) {
		return githubClient.getRepos(username);
	}
}
