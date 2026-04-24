package com.ebookmurah.controller;

import com.ebookmurah.entity.Ebook;
import com.ebookmurah.service.EbookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final EbookService ebookService;

    @Autowired
    public AdminController(EbookService ebookService) {
        this.ebookService = ebookService;
    }

    @GetMapping("/upload")
    public String uploadForm(Model model) {
        return "admin/upload";
    }

    @PostMapping("/upload")
    public String uploadEbook(@RequestParam String title,
                               @RequestParam String description,
                               @RequestParam Long price,
                               @RequestParam String coverUrl,
                               @RequestParam String productLink,
                               RedirectAttributes redirectAttributes) {
        try {
            Ebook ebook = new Ebook();
            ebook.setTitle(title);
            ebook.setDescription(description);
            ebook.setPrice(price);
            ebook.setCoverUrl(coverUrl);
            ebook.setProductLink(productLink);

            ebookService.createEbook(ebook);
            
            redirectAttributes.addFlashAttribute("success", "Ebook '" + title + "' berhasil diupload!");
            return "redirect:/admin/upload";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Gagal upload ebook: " + e.getMessage());
            return "redirect:/admin/upload";
        }
    }
}
