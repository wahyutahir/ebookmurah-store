package com.ebookmurah.repository;

import com.ebookmurah.entity.TimeboxingProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TimeboxingProgressRepository extends JpaRepository<TimeboxingProgress, Long> {
    List<TimeboxingProgress> findByUserIdOrderByCurrentDayAsc(Long userId);
    Optional<TimeboxingProgress> findByUserIdAndCurrentDay(Long userId, Integer currentDay);
    List<TimeboxingProgress> findByUserIdAndCompletedTrue(Long userId);
}
