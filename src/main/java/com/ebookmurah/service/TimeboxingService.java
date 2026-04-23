package com.ebookmurah.service;

import com.ebookmurah.entity.TimeboxingProgress;
import com.ebookmurah.entity.User;
import com.ebookmurah.repository.TimeboxingProgressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TimeboxingService {

    private final TimeboxingProgressRepository timeboxingProgressRepository;

    @Transactional
    public TimeboxingProgress initializeProgress(User user) {
        // Check if user already has progress
        List<TimeboxingProgress> existingProgress = timeboxingProgressRepository.findByUserIdOrderByCurrentDayAsc(user.getId());
        if (!existingProgress.isEmpty()) {
            return existingProgress.get(0);
        }

        // Create initial progress for day 1
        TimeboxingProgress progress = TimeboxingProgress.builder()
                .user(user)
                .currentDay(1)
                .phase(1)
                .completed(false)
                .motivationText("Mental Nyam-Nyam Berkurang")
                .startDate(LocalDate.now())
                .build();

        return timeboxingProgressRepository.save(progress);
    }

    @Transactional
    public TimeboxingProgress updateProgress(Long userId, Integer currentDay, String dailyNote) {
        TimeboxingProgress progress = timeboxingProgressRepository.findByUserIdAndCurrentDay(userId, currentDay)
                .orElseThrow(() -> new RuntimeException("Progress not found for day " + currentDay));

        progress.setDailyNote(dailyNote);
        progress.setCompleted(true);
        progress.setCompletedDate(LocalDate.now());

        // Update phase based on current day
        if (currentDay >= 1 && currentDay <= 30) {
            progress.setPhase(1);
            progress.setMotivationText("Mental Nyam-Nyam Berkurang");
        } else if (currentDay >= 31 && currentDay <= 60) {
            progress.setPhase(2);
            progress.setMotivationText("Meraut Bambu Karier");
        } else if (currentDay >= 61 && currentDay <= 90) {
            progress.setPhase(3);
            progress.setMotivationText("Menjadi Orang yang Dicari Dunia");
        }

        return timeboxingProgressRepository.save(progress);
    }

    @Transactional
    public TimeboxingProgress advanceToNextDay(Long userId) {
        List<TimeboxingProgress> progressList = timeboxingProgressRepository.findByUserIdOrderByCurrentDayAsc(userId);
        
        if (progressList.isEmpty()) {
            throw new RuntimeException("No progress found for user");
        }

        TimeboxingProgress lastProgress = progressList.get(progressList.size() - 1);
        Integer nextDay = lastProgress.getCurrentDay() + 1;

        if (nextDay > 90) {
            throw new RuntimeException("90-day program completed");
        }

        TimeboxingProgress newProgress = TimeboxingProgress.builder()
                .user(lastProgress.getUser())
                .currentDay(nextDay)
                .completed(false)
                .startDate(lastProgress.getStartDate())
                .build();

        // Set phase and motivation
        if (nextDay >= 1 && nextDay <= 30) {
            newProgress.setPhase(1);
            newProgress.setMotivationText("Mental Nyam-Nyam Berkurang");
        } else if (nextDay >= 31 && nextDay <= 60) {
            newProgress.setPhase(2);
            newProgress.setMotivationText("Meraut Bambu Karier");
        } else if (nextDay >= 61 && nextDay <= 90) {
            newProgress.setPhase(3);
            newProgress.setMotivationText("Menjadi Orang yang Dicari Dunia");
        }

        return timeboxingProgressRepository.save(newProgress);
    }

    public List<TimeboxingProgress> getUserProgress(Long userId) {
        return timeboxingProgressRepository.findByUserIdOrderByCurrentDayAsc(userId);
    }

    public Optional<TimeboxingProgress> getUserProgressForDay(Long userId, Integer day) {
        return timeboxingProgressRepository.findByUserIdAndCurrentDay(userId, day);
    }

    public int getCompletedDays(Long userId) {
        return timeboxingProgressRepository.findByUserIdAndCompletedTrue(userId).size();
    }

    public double getProgressPercentage(Long userId) {
        int completed = getCompletedDays(userId);
        return (completed / 90.0) * 100;
    }
}
