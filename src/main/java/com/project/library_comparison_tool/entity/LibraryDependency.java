package com.project.library_comparison_tool.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "library_dependency")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LibraryDependency {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "libdep_seq")
    @SequenceGenerator(
            name = "libdep_seq",
            sequenceName = "libdep_seq",
            allocationSize = 1
    )
    private Long id;

    private String dependencyName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "library_id")
    private Library library;
}
