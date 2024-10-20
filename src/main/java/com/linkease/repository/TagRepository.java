package com.linkease.repository;

import com.linkease.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Tag findByName(String name);
}
