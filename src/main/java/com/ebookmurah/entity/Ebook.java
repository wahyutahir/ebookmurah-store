package com.ebookmurah.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "ebooks")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Ebook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String slug;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String phase1Narrative; // Menggali Luka
    @Column(columnDefinition = "TEXT")
    private String phase2Narrative; // Instal Mindset
    @Column(columnDefinition = "TEXT")
    private String phase3Narrative; // Strategi Potensi
    @Column(columnDefinition = "TEXT")
    private String phase4Narrative; // Titik Balik
    @Column(columnDefinition = "TEXT")
    private String phase5Narrative; // Transformasi

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    @Builder.Default
    private Integer stock = 999;

    @Column(nullable = false)
    @Builder.Default
    private Boolean active = true;

    private String coverImageUrl;
    private String pdfFilePath;
    private String pdfDownloadLink;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
