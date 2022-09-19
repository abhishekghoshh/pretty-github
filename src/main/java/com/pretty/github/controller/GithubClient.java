package com.pretty.github.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class GithubClient {
	@Autowired
	RestCall restCall;
	private static final String USER_URL = "https://api.github.com/users/%s";
	private static final String REPOS_URL = "https://api.github.com/users/%s/repos";
	private static final String CONTENT_URL = "https://api.github.com/repos/%s/%s/contents/%s?ref=%s";

	@SuppressWarnings("rawtypes")
	public ResponseEntity isValidUser(String userName) {
		return restCall.get(String.format(USER_URL, userName), Map.class);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ResponseEntity getRepos(String userName) {
		ResponseEntity<List> entity = restCall.get(String.format(REPOS_URL, userName), List.class);
		if (entity.getStatusCode() != HttpStatus.OK) {
			return entity;
		}
		List<Map> repoList = new ArrayList<>();
		Map repo = null;
		List<Map> rawReposList = entity.getBody();
		for (Map item : rawReposList) {
			repo = new HashMap();
			repo.put("name", item.get("name"));
			repo.put("url", item.get("url"));
			repo.put("branch", item.get("default_branch"));
			repo.put("contentUrl", item.get("url") + "/contents/");
			repoList.add(repo);
		}
		return ResponseEntity.ok(repoList);
	}

	@SuppressWarnings({ "rawtypes" })
	public ResponseEntity getTableContent(Map map) {
		String ownerName = (String) map.get("ownerName");
		String repoName = (String) map.get("repoName");
		String contentPath = (String) map.get("contentPath");
		String branch = (String) map.get("branch");
		String directUrl = (String) map.get("directUrl");
		if (isNotBlank(directUrl)) {
			return restCall.get(directUrl, null, new ParameterizedTypeReference<List<GithubNode>>() {
			});
		}
		String url = String.format(CONTENT_URL, ownerName, repoName, contentPath, branch);
		return restCall.get(url, null, new ParameterizedTypeReference<List<GithubNode>>() {
		});
	}

	private boolean isNotBlank(String... array) {
		boolean isNotBlank = true;
		for (String string : array) {
			isNotBlank = isNotBlank && (null != string && !"".equals(string.trim()));
		}
		return isNotBlank;
	}

	@SuppressWarnings({ "rawtypes" })
	public ResponseEntity rawContent(Map map) {
		ResponseEntity<String> response = restCall.get((String) map.get("rawContentUrl"), String.class);
		Map<String, String> rawContent = new HashMap<>();
		rawContent.put("content", response.getBody());
		return ResponseEntity.ok(rawContent);
	}

}
