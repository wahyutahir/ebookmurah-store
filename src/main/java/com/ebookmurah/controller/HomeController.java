package com.ebookmurah.controller;

import com.ebookmurah.entity.Ebook;
import com.ebookmurah.repository.EbookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final EbookRepository ebookRepository;

    @GetMapping("/")
    public String home(Model model) {
        try {
            List<Ebook> ebooks = ebookRepository.findAll();
            model.addAttribute("ebooks", ebooks != null ? ebooks : List.of());
        } catch (Exception e) {
            model.addAttribute("ebooks", List.of());
        }
        return "index";
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
