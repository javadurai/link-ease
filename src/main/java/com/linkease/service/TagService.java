package com.linkease.service;

import com.linkease.domain.Tag;
import com.linkease.repository.TagRepository;
import org.springframework.stereotype.Service;

@Service
public class TagService {

    private final TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public Tag createTag(Tag tag) {
        return tagRepository.save(tag);
    }

    public Tag findByName(String name) {
        return tagRepository.findByName(name);
    }
}
