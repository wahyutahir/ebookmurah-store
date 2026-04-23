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
        List<Ebook> ebooks = ebookService.getAllActiveEbooks();
        model.addAttribute("ebooks", ebooks);
        return "index";
    }

    @GetMapping("/ebook/{slug}")
    public String ebookDetail(@PathVariable String slug, Model model) {
        Ebook ebook = ebookService.getBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Ebook not found"));
        model.addAttribute("ebook", ebook);
        return "ebook-detail";
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
