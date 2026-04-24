package com.ebookmurah.controller;

import com.ebookmurah.entity.Ebook;
import com.ebookmurah.service.EbookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class PublicController {

    private final EbookService ebookService;

    @GetMapping("/")
    public String home(Model model) {
        try {
            List<Ebook> ebooks = ebookService.getAllEbooks();
            model.addAttribute("ebooks", ebooks != null ? ebooks : List.of());
        } catch (Exception e) {
            model.addAttribute("ebooks", List.of());
        }
        return "index";
    }

    @GetMapping("/ebook/{id}")
    public String ebookDetail(@PathVariable Long id, Model model) {
        Ebook ebook = ebookService.getById(id)
                .orElseThrow(() -> new RuntimeException("Ebook not found"));
        model.addAttribute("ebook", ebook);
        return "detail";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }
}
