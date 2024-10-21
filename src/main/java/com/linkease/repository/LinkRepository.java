package com.linkease.repository;

import com.linkease.domain.Link;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LinkRepository extends JpaRepository<Link, Long> {
    List<Link> findAllByUserId(Long userId);

    Page<Link> findByUrlContaining(String url, Pageable pageable);
}
