package com.ebookmurah.controller;

import com.ebookmurah.entity.Ebook;
import com.ebookmurah.service.EbookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.UUID;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final EbookService ebookService;

    @GetMapping("/upload")
    public String uploadForm(Model model) {
        return "admin/upload";
    }

    @PostMapping("/upload")
    public String uploadEbook(@RequestParam String title,
                               @RequestParam String description,
                               @RequestParam BigDecimal price,
                               @RequestParam String coverImageUrl,
                               @RequestParam String downloadUrl,
                               RedirectAttributes redirectAttributes) {
        try {
            // Generate slug from title
            String slug = title.toLowerCase()
                    .replaceAll("[^a-z0-9\\s-]", "")
                    .replaceAll("\\s+", "-")
                    .substring(0, Math.min(title.length(), 50));
            
            // Ensure unique slug
            slug = slug + "-" + UUID.randomUUID().toString().substring(0, 8);

            Ebook ebook = Ebook.builder()
                    .slug(slug)
                    .title(title)
                    .description(description)
                    .price(price)
                    .coverImageUrl(coverImageUrl)
                    .pdfFilePath(downloadUrl)
                    .active(true)
                    .stock(999) // Digital product, unlimited stock
                    .build();

            ebookService.createEbook(ebook);
            
            redirectAttributes.addFlashAttribute("success", "Ebook '" + title + "' berhasil diupload!");
            return "redirect:/admin/upload";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Gagal upload ebook: " + e.getMessage());
            return "redirect:/admin/upload";
        }
    }
}
