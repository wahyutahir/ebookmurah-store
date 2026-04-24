package com.ebookmurah.service;

import com.ebookmurah.entity.Ebook;
import com.ebookmurah.repository.EbookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class EbookService {

    private final EbookRepository ebookRepository;

    @Autowired
    public EbookService(EbookRepository ebookRepository) {
        this.ebookRepository = ebookRepository;
    }

    public List<Ebook> getAllEbooks() {
        return ebookRepository.findAll();
    }

    public Optional<Ebook> getById(Long id) {
        return ebookRepository.findById(id);
    }

    @Transactional
    public Ebook createEbook(Ebook ebook) {
        return ebookRepository.save(ebook);
    }

    @Transactional
    public Ebook updateEbook(Long id, Ebook ebookDetails) {
        Ebook ebook = ebookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ebook not found"));

        ebook.setTitle(ebookDetails.getTitle());
        ebook.setDescription(ebookDetails.getDescription());
        ebook.setPrice(ebookDetails.getPrice());
        ebook.setCoverUrl(ebookDetails.getCoverUrl());
        ebook.setProductLink(ebookDetails.getProductLink());

        return ebookRepository.save(ebook);
    }

    @Transactional
    public void deleteEbook(Long id) {
        ebookRepository.deleteById(id);
    }
}
