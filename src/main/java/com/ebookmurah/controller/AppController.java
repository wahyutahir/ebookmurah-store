package com.ebookmurah.controller;

import com.ebookmurah.entity.TimeboxingProgress;
import com.ebookmurah.entity.Transaction;
import com.ebookmurah.entity.User;
import com.ebookmurah.service.TimeboxingService;
import com.ebookmurah.service.TransactionService;
import com.ebookmurah.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/app")
@RequiredArgsConstructor
public class AppController {

    private final UserService userService;
    private final TimeboxingService timeboxingService;
    private final TransactionService transactionService;

    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", user);
        return "app/dashboard";
    }

    @GetMapping("/generator")
    public String generator(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", user);
        return "app/generator";
    }

    @PostMapping("/generator/analyze")
    public String analyzeSkill(@AuthenticationPrincipal User user,
                               @RequestParam String skillTarget,
                               @RequestParam String currentLevel,
                               @RequestParam String timeframe,
                               RedirectAttributes redirectAttributes) {
        // Simple skill analysis - store in flash attribute for display
        String analysis = String.format("Analisis untuk '%s' (Level: %s, Target: %s) sedang diproses. ", 
            skillTarget, currentLevel, timeframe);
        analysis += "Fitur lengkap akan segera hadir dengan AI-powered learning path!";
        
        redirectAttributes.addFlashAttribute("analysis", analysis);
        redirectAttributes.addFlashAttribute("success", "Analisis skill berhasil disimpan!");
        return "redirect:/app/generator";
    }

    @GetMapping("/timeboxing")
    public String timeboxing(@AuthenticationPrincipal User user, Model model) {
        // Initialize progress if user has no progress yet
        List<TimeboxingProgress> progressList = timeboxingService.getUserProgress(user.getId());
        if (progressList == null || progressList.isEmpty()) {
            timeboxingService.initializeProgress(user);
            progressList = timeboxingService.getUserProgress(user.getId());
        }
        
        double progressPercentage = timeboxingService.getProgressPercentage(user.getId());
        int completedDays = timeboxingService.getCompletedDays(user.getId());

        model.addAttribute("user", user);
        model.addAttribute("progressList", progressList != null ? progressList : List.of());
        model.addAttribute("progressPercentage", progressPercentage);
        model.addAttribute("completedDays", completedDays);
        model.addAttribute("totalDays", 90);

        return "app/timeboxing";
    }

    @PostMapping("/timeboxing/advance")
    public String advanceDay(@AuthenticationPrincipal User user, RedirectAttributes redirectAttributes) {
        try {
            timeboxingService.advanceToNextDay(user.getId());
            redirectAttributes.addFlashAttribute("success", "Berhasil lanjut ke hari berikutnya!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/app/timeboxing";
    }

    @PostMapping("/timeboxing/update")
    public String updateProgress(@AuthenticationPrincipal User user,
                                 @RequestParam Integer currentDay,
                                 @RequestParam String dailyNote,
                                 RedirectAttributes redirectAttributes) {
        try {
            timeboxingService.updateProgress(user.getId(), currentDay, dailyNote);
            redirectAttributes.addFlashAttribute("success", "Progress hari ke-" + currentDay + " berhasil disimpan!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/app/timeboxing";
    }

    @GetMapping("/transactions")
    public String transactions(@AuthenticationPrincipal User user, Model model) {
        List<Transaction> transactions = transactionService.getUserTransactions(user.getId());
        model.addAttribute("user", user);
        model.addAttribute("transactions", transactions);
        return "app/transactions";
    }
}
