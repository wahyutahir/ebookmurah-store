package com.ebookmurah.service;

import com.ebookmurah.entity.Ebook;
import com.ebookmurah.repository.EbookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EbookService {

    private final EbookRepository ebookRepository;

    public List<Ebook> getAllActiveEbooks() {
        return ebookRepository.findByActiveTrue();
    }

    public Optional<Ebook> getBySlug(String slug) {
        return ebookRepository.findBySlug(slug);
    }

    public Optional<Ebook> getById(Long id) {
        return ebookRepository.findById(id);
    }

    @Transactional
    public Ebook createEbook(Ebook ebook) {
        if (ebookRepository.existsBySlug(ebook.getSlug())) {
            throw new RuntimeException("Slug already exists");
        }
        return ebookRepository.save(ebook);
    }

    @Transactional
    public Ebook updateEbook(Long id, Ebook ebookDetails) {
        Ebook ebook = ebookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ebook not found"));

        ebook.setTitle(ebookDetails.getTitle());
        ebook.setDescription(ebookDetails.getDescription());
        ebook.setPrice(ebookDetails.getPrice());
        ebook.setCoverImageUrl(ebookDetails.getCoverImageUrl());
        ebook.setPdfFilePath(ebookDetails.getPdfFilePath());
        ebook.setPdfDownloadLink(ebookDetails.getPdfDownloadLink());
        ebook.setPhase1Narrative(ebookDetails.getPhase1Narrative());
        ebook.setPhase2Narrative(ebookDetails.getPhase2Narrative());
        ebook.setPhase3Narrative(ebookDetails.getPhase3Narrative());
        ebook.setPhase4Narrative(ebookDetails.getPhase4Narrative());
        ebook.setPhase5Narrative(ebookDetails.getPhase5Narrative());

        return ebookRepository.save(ebook);
    }

    @Transactional
    public void deleteEbook(Long id) {
        Ebook ebook = ebookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ebook not found"));
        ebook.setActive(false);
        ebookRepository.save(ebook);
    }
}
