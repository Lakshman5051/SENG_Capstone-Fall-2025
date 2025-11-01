package com.project.library_comparison_tool.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

//My Business logic to get the libraries through restTemplate

@Service
public class LibrariesIoApiService {

    @Value("${libraries.io.api.key}")
    private String apiKey;

    @Value("${libraries.io.api.base-url}")
    private String baseUrl;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public LibrariesIoApiService() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Search for libraries by query
     * @param query Search term (usage - "json", "logging", "web framework")
     * @param platform Platform (usage - "Maven", "NPM", "PyPI", "Go")
     * @param page Page number (starts at 1)
     * @return List of JsonNode objects (raw API response)
     */
    public List<JsonNode> searchLibraries(String query, String platform, int page) {
        String url = UriComponentsBuilder.fromHttpUrl(baseUrl + "/search")
                .queryParam("q", query)
                .queryParam("platforms", platform)
                .queryParam("page", page)
                .queryParam("api_key", apiKey)
                .toUriString();

        try {
            System.out.println("Fetching: " + url);
            String response = restTemplate.getForObject(url, String.class);
            JsonNode rootNode = objectMapper.readTree(response);

            List<JsonNode> results = new ArrayList<>();
            if (rootNode.isArray()) {
                rootNode.forEach(results::add);
            }

            System.out.println("Found " + results.size() + " libraries");
            return results;

        } catch (Exception e) {
            System.err.println("Error fetching libraries: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Get detailed information for a specific library
     * @param platform Platform (Usage - "Maven", "NPM")
     * @param name Library name (Usage - "com.fasterxml.jackson.core:jackson-databind")
     * @return JsonNode with library details
     */
    public JsonNode getLibraryDetails(String platform, String name) {
        String url = UriComponentsBuilder.fromHttpUrl(baseUrl + "/" + platform + "/" + name)
                .queryParam("api_key", apiKey)
                .toUriString();

        try {
            System.out.println("Fetching details: " + url);
            String response = restTemplate.getForObject(url, String.class);
            return objectMapper.readTree(response);

        } catch (Exception e) {
            System.err.println("Error fetching library details: " + e.getMessage());
            return null;
        }
    }

    /**
     * Get dependencies for a specific library version
     * @param platform Platform
     * @param name Library name
     * @param version Version number
     * @return JsonNode with dependencies
     */
    public JsonNode getLibraryDependencies(String platform, String name, String version) {
        String url = UriComponentsBuilder.fromHttpUrl(
                        baseUrl + "/" + platform + "/" + name + "/" + version + "/dependencies")
                .queryParam("api_key", apiKey)
                .toUriString();

        try {
            System.out.println("Fetching dependencies: " + url);
            String response = restTemplate.getForObject(url, String.class);
            return objectMapper.readTree(response);

        } catch (Exception e) {
            System.err.println("Error fetching dependencies: " + e.getMessage());
            return null;
        }
    }
}