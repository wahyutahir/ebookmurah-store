package com.ebookmurah.repository;

import com.ebookmurah.entity.Ebook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EbookRepository extends JpaRepository<Ebook, Long> {
    Optional<Ebook> findBySlug(String slug);
    List<Ebook> findByActiveTrue();
    boolean existsBySlug(String slug);
}
