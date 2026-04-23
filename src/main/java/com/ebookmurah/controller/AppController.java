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

    @GetMapping("/timeboxing")
    public String timeboxing(@AuthenticationPrincipal User user, Model model) {
        List<TimeboxingProgress> progressList = timeboxingService.getUserProgress(user.getId());
        double progressPercentage = timeboxingService.getProgressPercentage(user.getId());
        int completedDays = timeboxingService.getCompletedDays(user.getId());

        model.addAttribute("user", user);
        model.addAttribute("progressList", progressList);
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
