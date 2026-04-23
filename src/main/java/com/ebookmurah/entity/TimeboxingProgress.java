package com.ebookmurah.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "timeboxing_progress")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TimeboxingProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private Integer currentDay;

    @Column(nullable = false)
    @Builder.Default
    private Integer phase = 1; // 1: Pembersihan Trauma (Day 1-30), 2: Instalasi Potensi (Day 31-60), 3: Market Launch (Day 61-90)

    @Column(columnDefinition = "TEXT")
    private String dailyNote;

    @Column(columnDefinition = "TEXT")
    private String motivationText;

    @Column(nullable = false)
    @Builder.Default
    private Boolean completed = false;

    @Column(name = "completed_date")
    private LocalDate completedDate;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (startDate == null) {
            startDate = LocalDate.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public String getPhaseName() {
        if (currentDay >= 1 && currentDay <= 30) {
            return "Pembersihan Trauma";
        } else if (currentDay >= 31 && currentDay <= 60) {
            return "Instalasi Potensi";
        } else if (currentDay >= 61 && currentDay <= 90) {
            return "Market Launch & Value";
        }
        return "Selesai";
    }

    public String getMotivationText() {
        if (currentDay >= 1 && currentDay <= 30) {
            return "Mental Nyam-Nyam Berkurang";
        } else if (currentDay >= 31 && currentDay <= 60) {
            return "Meraut Bambu Karier";
        } else if (currentDay >= 61 && currentDay <= 90) {
            return "Menjadi Orang yang Dicari Dunia";
        }
        return "Transformasi Selesai!";
    }
}
