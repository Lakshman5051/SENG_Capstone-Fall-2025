package com.project.library_comparison_tool.Controller;

import com.project.library_comparison_tool.entity.Library;
import com.project.library_comparison_tool.service.LibraryService;
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
    public ResponseEntity<List<Library>> getAllLibraries() {
        return ResponseEntity.ok(libraryService.getAllLibraries());
    }

    // 3. get library by ID
    @GetMapping("/{id}")
    public ResponseEntity<Library> getLibraryById(@PathVariable String id) {
        return libraryService.getLibraryById(UUID.fromString(id))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 4. search by name partial (fuzzy-lite)
    @GetMapping("/search")
    public ResponseEntity<List<Library>> searchByName(@RequestParam("name") String name) {
        return ResponseEntity.ok(libraryService.searchLibrariesByName(name));
    }

    // 5. filter by category
    @GetMapping("/category/{category}")
    public ResponseEntity<List<Library>> getByCategory(@PathVariable String category) {
        return ResponseEntity.ok(libraryService.getLibrariesByCategory(category));
    }
}
