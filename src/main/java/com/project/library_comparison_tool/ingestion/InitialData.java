package com.project.library_comparison_tool.ingestion;

import com.project.library_comparison_tool.entity.Library;
import com.project.library_comparison_tool.entity.LibraryDependency;
import com.project.library_comparison_tool.repository.LibraryRepository;
import com.project.library_comparison_tool.service.LibraryService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class InitialData implements CommandLineRunner {

    private final LibraryRepository libraryRepository;
    private final LibraryService libraryService;

    public InitialData(LibraryRepository libraryRepository,
                                      LibraryService libraryService) {
        this.libraryRepository = libraryRepository;
        this.libraryService = libraryService;
    }

    @Override
    public void run(String... args) throws Exception {

        // Only ingest once: if DB already has data, skip
        if (libraryRepository.count() > 0) {
            System.out.println("[ingestion] Libraries already present, skipping bootstrap.");
            return;
        }

        System.out.println("[ingestion] No libraries found. Seeding initial data...");

        // Example: Jackson
        Library jackson = Library.builder()
                .name("Jackson Databind")
                .category("JSON Serialization")
                .description("High-performance JSON processor for Java that maps JSON <-> POJOs.")
                .framework("spring")
                .runtimeEnvironment("jvm")
                .supplier("FasterXML")
                .licenseType("Apache-2.0")
                .cost("Free / Open Source")
                .latestVersion("2.17.2")
                .lastUpdated("2025-10-26")
                .supportedOs(List.of("linux", "windows", "macos"))
                .exampleCodeSnippet(
                        "ObjectMapper mapper = new ObjectMapper();\n" +
                                "User user = mapper.readValue(jsonString, User.class);\n" +
                                "String out = mapper.writeValueAsString(user);"
                )
                .build();

        jackson.setDependencies(List.of(
                LibraryDependency.builder()
                        .dependencyName("jackson-core")
                        .library(jackson)
                        .build()
        ));

        libraryService.addLibrary(jackson);


        // Example: Gson
        Library gson = Library.builder()
                .name("Gson")
                .category("JSON Serialization")
                .description("Google's JSON library to convert Java Objects to JSON and back.")
                .framework("none")
                .runtimeEnvironment("jvm")
                .supplier("Google")
                .licenseType("Apache-2.0")
                .cost("Free / Open Source")
                .latestVersion("2.11.0")
                .lastUpdated("2025-10-26")
                .supportedOs(List.of("linux", "windows", "macos"))
                .exampleCodeSnippet(
                        "Gson gson = new Gson();\n" +
                                "User user = gson.fromJson(jsonString, User.class);\n" +
                                "String out = gson.toJson(user);"
                )
                .build();

        gson.setDependencies(List.of(
                LibraryDependency.builder()
                        .dependencyName("gson-runtime")
                        .library(gson)
                        .build()
        ));

        libraryService.addLibrary(gson);


        // Example: Log4j 2
        Library log4j2 = Library.builder()
                .name("Apache Log4j 2")
                .category("Logging")
                .description("Logging framework for Java with async logging and rich appenders.")
                .framework("spring")
                .runtimeEnvironment("jvm")
                .supplier("Apache Software Foundation")
                .licenseType("Apache-2.0")
                .cost("Free / Open Source")
                .latestVersion("2.23.1")
                .lastUpdated("2025-10-26")
                .supportedOs(List.of("linux", "windows", "macos"))
                .exampleCodeSnippet(
                        "private static final Logger log = LogManager.getLogger(MyClass.class);\n" +
                                "log.info(\"Service started\");"
                )
                .build();

        log4j2.setDependencies(List.of(
                LibraryDependency.builder()
                        .dependencyName("log4j-api")
                        .library(log4j2)
                        .build(),
                LibraryDependency.builder()
                        .dependencyName("log4j-core")
                        .library(log4j2)
                        .build()
        ));

        libraryService.addLibrary(log4j2);


        // Example: Logback
        Library logback = Library.builder()
                .name("Logback")
                .category("Logging")
                .description("Logging framework, default in Spring Boot via SLF4J binding.")
                .framework("spring")
                .runtimeEnvironment("jvm")
                .supplier("QOS.ch")
                .licenseType("EPL/LGPL")
                .cost("Free / Open Source")
                .latestVersion("1.5.6")
                .lastUpdated("2025-10-26")
                .supportedOs(List.of("linux", "windows", "macos"))
                .exampleCodeSnippet(
                        "private static final Logger log = LoggerFactory.getLogger(MyClass.class);\n" +
                                "log.info(\"Starting up\");"
                )
                .build();

        logback.setDependencies(List.of(
                LibraryDependency.builder()
                        .dependencyName("slf4j-api")
                        .library(logback)
                        .build()
        ));

        libraryService.addLibrary(logback);


        System.out.println("Sample Data from ingestion load complete.");
    }
}
