package com.bio.sequencing.models;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "results")
@Getter
@Setter
@NoArgsConstructor
public class Result {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "job_id", nullable = false)
    private AnalysisJob job;

    @Column(name = "result_type", nullable = false)
    private String resultType;

    @Column(name = "storage_key", nullable = false, unique = true)
    private String storageKey;

    @Column(columnDefinition = "JSON")
    private String metadata;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}

//For a bioinformatics platform, result_type could represent values such as:
//
//ALIGNMENT
//        VARIANT_CALLS
//ANNOTATION
//        PHYLOGENETIC_TREE
//QUALITY_REPORT
//        ASSEMBLY