package com.project.library_comparison_tool.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "library")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Library {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "library_seq")
    @SequenceGenerator(
            name = "library_seq",
            sequenceName = "library_seq",
            allocationSize = 1
    )
    private Long id;

    // Basic identity / classification
    private String name;                     // "Jackson Databind"
    private String category;                 // "JSON Serialization", "Logging", "UI Components"
    private String description;              // short summary

    // Stack + usage context
    private String framework;                // "spring", "react", "angular", "none"
    private String runtimeEnvironment;       // "jvm", "browser", "nodejs", "cpp/native"

    // Business / risk info
    private String supplier;                 // "Apache Software Foundation", "FasterXML"
    private String licenseType;              // "Apache-2.0", "MIT", etc.
    private String cost;                     // "Free / Open Source", "Commercial", "Dual License"

    // Version / maturity
    private String latestVersion;            // "2.17.2"
    private String lastUpdated;              // can store as String or later switch to Instant/LocalDate

    // Platform support ("linux", "windows", "macos", "browser")
    @ElementCollection
    @CollectionTable(
            name = "library_supported_os",
            joinColumns = @JoinColumn(name = "library_id")
    )
    @Column(name = "os_name")
    private List<String> supportedOs;

    // Developer experience
    @Column(length = 4000)
    private String exampleCodeSnippet;

    // Relationships
    @OneToMany(
            mappedBy = "library",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<LibraryDependency> dependencies;
}
