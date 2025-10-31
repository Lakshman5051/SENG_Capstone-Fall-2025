package com.project.library_comparison_tool.service;

import com.project.library_comparison_tool.entity.Library;
import com.project.library_comparison_tool.repository.LibraryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class LibraryService {

    private final LibraryRepository libraryRepository;

    public LibraryService(LibraryRepository libraryRepository) {
        this.libraryRepository = libraryRepository;
    }

    //If we already have a given library (same name), update it. Otherwise insert it
    public Library addOrUpdateLibrary(Library library) {
        Optional<Library> existingOpt =
                libraryRepository.findByNameIgnoreCase(library.getName());

        if (existingOpt.isPresent()) {
            Library existing = existingOpt.get();

            // Update mutable fields
            existing.setCategory(library.getCategory());
            existing.setDescription(library.getDescription());
            existing.setFramework(library.getFramework());
            existing.setRuntimeEnvironment(library.getRuntimeEnvironment());
            existing.setSupplier(library.getSupplier());
            existing.setLicenseType(library.getLicenseType());
            existing.setCost(library.getCost());
            existing.setLatestVersion(library.getLatestVersion());
            existing.setLastUpdated(library.getLastUpdated());
            existing.setSupportedOs(library.getSupportedOs());
            existing.setExampleCodeSnippet(library.getExampleCodeSnippet());
            // NOTE: you could also merge dependencies here if you want

            return libraryRepository.save(existing);
        } else {
            // brand new library
            return libraryRepository.save(library);
        }
    }


    // Create / Add new library
    public Library addLibrary(Library library) {
        // later you can validate fields, normalize casing, etc.
        return libraryRepository.save(library);
    }

    // Get all libraries
    public List<Library> getAllLibraries() {
        return libraryRepository.findAll();
    }

    // Get one by ID
    public Optional<Library> getLibraryById(Long id) {
        return libraryRepository.findById(id);
    }

    // Search by name (partial match)
    public List<Library> searchLibrariesByName(String namePart) {
        return libraryRepository.findByNameContainingIgnoreCase(namePart);
    }

    // Filter by category (e.g. "logging", "json", "ui-components")
    public List<Library> getLibrariesByCategory(String category) {
        return libraryRepository.findByCategoryIgnoreCase(category);
    }

    //most popular libraries
    public List<Library> getMostPopular() {
        return libraryRepository.findAllByOrderByGithubStarsDesc();
    }
}