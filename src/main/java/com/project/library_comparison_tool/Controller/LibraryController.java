package com.project.library_comparison_tool.Controller;

import com.project.library_comparison_tool.entity.Library;
import com.project.library_comparison_tool.service.LibraryService;
import com.project.library_comparison_tool.dto.LibraryDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/libraries")
@CrossOrigin(origins = "*") // later you can lock this to your React origin
public class LibraryController {

    private final LibraryService libraryService;

    public LibraryController(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    // 1. add new library
    @PostMapping
    public ResponseEntity<Library> addLibrary(@RequestBody Library library) {
        Library saved = libraryService.addLibrary(library);
        return ResponseEntity.ok(saved);
    }

    // 2. list all libraries
    @GetMapping
    public ResponseEntity<List<LibraryDTO>> getAllLibraries() {
        List<Library> libraries = libraryService.getAllLibraries();
        return ResponseEntity.ok(LibraryDTO.fromEntities(libraries));
    }

    // 3. get library by ID
    @GetMapping("/{id}")
    public ResponseEntity<LibraryDTO> getLibraryById(@PathVariable Long id) {
        return libraryService.getLibraryById(id)
                .map(LibraryDTO::fromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 4. search by name partial (fuzzy-lite)
    @GetMapping("/search")
    public ResponseEntity<List<LibraryDTO>> searchByName(@RequestParam("name") String name) {
        List<Library> libraries = libraryService.searchLibrariesByName(name);
        return ResponseEntity.ok(LibraryDTO.fromEntities(libraries));
    }

    // 5. filter by category
    @GetMapping("/category/{category}")
    public ResponseEntity<List<LibraryDTO>> getByCategory(@PathVariable String category) {
        List<Library> libraries = libraryService.getLibrariesByCategory(category);
        return ResponseEntity.ok(LibraryDTO.fromEntities(libraries));
    }
}
