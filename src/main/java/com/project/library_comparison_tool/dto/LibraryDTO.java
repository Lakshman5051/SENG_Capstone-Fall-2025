package com.project.library_comparison_tool.dto;

import com.project.library_comparison_tool.entity.Library;
import com.project.library_comparison_tool.entity.LibraryDependency;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Data Transfer Object for Library
 * Used to control what data is exposed in the API
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LibraryDTO {

    private Long id;
    private String name;
    private String category;
    private String description;
    private String framework;
    private String runtimeEnvironment;
    private String supplier;
    private String licenseType;
    private String cost;
    private String latestVersion;
    private String lastUpdated;
    private List<String> supportedOs;
    private String exampleCodeSnippet;

    // Dependencies - just the names
    private List<String> dependencyNames;
    private Integer dependencyCount;

    /**
     * Convert Library entity to DTO
     */
    public static LibraryDTO fromEntity(Library library) {
        List<String> depNames = null;
        Integer depCount = 0;

        // Safely extract dependency names
        if (library.getDependencies() != null && !library.getDependencies().isEmpty()) {
            depNames = library.getDependencies().stream()
                    .map(LibraryDependency::getDependencyName)
                    .collect(Collectors.toList());
            depCount = depNames.size();
        }

        return LibraryDTO.builder()
                .id(library.getId())
                .name(library.getName())
                .category(library.getCategory())
                .description(library.getDescription())
                .framework(library.getFramework())
                .runtimeEnvironment(library.getRuntimeEnvironment())
                .supplier(library.getSupplier())
                .licenseType(library.getLicenseType())
                .cost(library.getCost())
                .latestVersion(library.getLatestVersion())
                .lastUpdated(library.getLastUpdated())
                .supportedOs(library.getSupportedOs())
                .exampleCodeSnippet(library.getExampleCodeSnippet())
                .dependencyNames(depNames)
                .dependencyCount(depCount)
                .build();
    }

    /**
     * Convert list of Library entities to list of DTOs
     */
    public static List<LibraryDTO> fromEntities(List<Library> libraries) {
        return libraries.stream()
                .map(LibraryDTO::fromEntity)
                .collect(Collectors.toList());
    }
}