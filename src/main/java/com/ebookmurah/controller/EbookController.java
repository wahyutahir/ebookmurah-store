package com.ebookmurah.controller;

import com.ebookmurah.entity.Ebook;
import com.ebookmurah.repository.EbookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Controller
public class EbookController {

    private final EbookRepository ebookRepository;

    @Autowired
    public EbookController(EbookRepository ebookRepository) {
        this.ebookRepository = ebookRepository;
    }

    @GetMapping("/ebook/{id}")
    public String ebookDetail(@PathVariable Long id, Model model) {
        Optional<Ebook> ebook = ebookRepository.findById(id);
        if (ebook.isPresent()) {
            model.addAttribute("ebook", ebook.get());
            return "detail";
        }
        return "redirect:/";
    }
}
