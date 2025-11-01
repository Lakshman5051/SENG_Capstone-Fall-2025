package com.project.library_comparison_tool.dto;

import com.fasterxml.jackson.databind.JsonNode;
import com.project.library_comparison_tool.entity.Library;
import com.project.library_comparison_tool.entity.LibraryDependency;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class LibraryIOMapperDTO {

    //Convert libraries.io API response to Library entity
    public Library mapToLibrary(JsonNode apiResponse, String category) {
        if (apiResponse == null) {
            return null;
        }

        Library library = new Library();


        library.setName(getTextOrNull(apiResponse, "name"));
        library.setDescription(getTextOrNull(apiResponse, "description"));
        library.setCategory(category); // Set based on search term

        String platform = getTextOrNull(apiResponse, "platform");
        library.setPackageManager(platform); // "Maven", "NPM", etc.
        library.setLanguage(getTextOrNull(apiResponse, "language"));

        library.setLatestVersion(getTextOrNull(apiResponse, "latest_stable_release_number"));

        // Parse last updated date
        String publishedAt = getTextOrNull(apiResponse, "latest_stable_release_published_at");
        if (publishedAt != null) {
            library.setLastUpdated(publishedAt.substring(0, 10));
        }

        // URLs
        library.setHomepageUrl(getTextOrNull(apiResponse, "homepage"));
        library.setRepositoryUrl(getTextOrNull(apiResponse, "repository_url"));
        library.setPackageUrl(getTextOrNull(apiResponse, "package_manager_url"));

        // Popularity metrics
        library.setGithubStars(getIntOrNull(apiResponse, "stars"));
        library.setGithubForks(getIntOrNull(apiResponse, "forks"));
        library.setDependentProjectsCount(getIntOrNull(apiResponse, "dependent_repos_count"));

        // License
        JsonNode licenses = apiResponse.get("normalized_licenses");
        if (licenses != null && licenses.isArray() && licenses.size() > 0) {
            library.setLicenseType(licenses.get(0).asText());
        }

        // Cost model (based on license)
        String license = library.getLicenseType();
        if (license != null && isOpenSourceLicense(license)) {
            library.setCost("Free / Open Source");
        } else if (license == null) {
            library.setCost("Unknown");
        } else {
            library.setCost("Check License");
        }

        // Status flags
        library.setIsDeprecated(false);
        library.setHasSecurityVulnerabilities(false);

        // Determine framework based on language and name
        library.setFramework(inferFramework(library.getName(), library.getLanguage()));

        // Runtime environment based on language
        library.setRuntimeEnvironment(inferRuntimeEnvironment(library.getLanguage()));

        return library;
    }

    //Map dependencies from API response
    public List<LibraryDependency> mapDependencies(JsonNode dependenciesResponse, Library library) {
        List<LibraryDependency> dependencies = new ArrayList<>();

        if (dependenciesResponse == null || !dependenciesResponse.has("dependencies")) {
            return dependencies;
        }

        JsonNode depsArray = dependenciesResponse.get("dependencies");
        if (depsArray.isArray()) {
            depsArray.forEach(dep -> {
                LibraryDependency dependency = new LibraryDependency();
                dependency.setDependencyName(dep.get("name").asText());
                dependency.setLibrary(library);
                dependencies.add(dependency);
            });
        }

        return dependencies;
    }

    private String getTextOrNull(JsonNode node, String fieldName) {
        JsonNode field = node.get(fieldName);
        if (field != null && !field.isNull()) {
            return field.asText();
        }
        return null;
    }

    private Integer getIntOrNull(JsonNode node, String fieldName) {
        JsonNode field = node.get(fieldName);
        if (field != null && !field.isNull() && field.isNumber()) {
            return field.asInt();
        }
        return null;
    }

    private boolean isOpenSourceLicense(String license) {
        String lower = license.toLowerCase();
        return lower.contains("apache") ||
                lower.contains("mit") ||
                lower.contains("bsd") ||
                lower.contains("gpl") ||
                lower.contains("lgpl") ||
                lower.contains("mpl");
    }

    private String inferFramework(String name, String language) {
        if (name == null) return "none";

        String lower = name.toLowerCase();

        // Java frameworks
        if (lower.contains("spring")) return "spring";

        // JavaScript frameworks
        if (lower.contains("react")) return "react";
        if (lower.contains("vue")) return "vue";
        if (lower.contains("angular")) return "angular";
        if (lower.contains("express")) return "express";

        // Python frameworks
        if (lower.contains("django")) return "django";
        if (lower.contains("flask")) return "flask";

        return "none";
    }

    private String inferRuntimeEnvironment(String language) {
        if (language == null) return "unknown";

        String lower = language.toLowerCase();

        if (lower.contains("java")) return "jvm";
        if (lower.contains("javascript") || lower.contains("typescript")) return "browser";
        if (lower.contains("python")) return "python";
        if (lower.contains("c++") || lower.contains("c") || lower.contains("rust")) return "native";
        if (lower.contains("go")) return "native";
        if (lower.contains("c#") || lower.contains("f#")) return "dotnet";

        return "unknown";
    }
}
