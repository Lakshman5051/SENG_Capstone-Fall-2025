package com.project.library_comparison_tool.repository;

import com.project.library_comparison_tool.entity.Library;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface LibraryRepository extends JpaRepository<Library, Long> {

    // simple partial / case-insensitive match
    List<Library> findByNameContainingIgnoreCase(String name);

    // optional: search by category filter
    List<Library> findByCategoryIgnoreCase(String category);
}
