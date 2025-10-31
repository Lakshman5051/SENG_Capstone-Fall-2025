package com.project.library_comparison_tool.repository;

import com.project.library_comparison_tool.entity.Library;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LibraryRepository extends JpaRepository<Library, Long> {

    // simple partial / case-insensitive match
    List<Library> findByNameContainingIgnoreCase(String name);

    // search by category filter
    List<Library> findByCategoryIgnoreCase(String category);

    //duplicate prevention
    Optional<Library> findByNameIgnoreCase(String name);

    //sort by popularity metrics
    List<Library> findAllByOrderByGithubStarsDesc();
}
