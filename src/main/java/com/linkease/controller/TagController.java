package com.linkease.controller;

import com.linkease.domain.Tag;
import com.linkease.service.TagService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tags")
public class TagController {

    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @PostMapping
    public ResponseEntity<Tag> createTag(@RequestBody Tag tag) {
        return ResponseEntity.ok(tagService.createTag(tag));
    }

    @GetMapping("/{name}")
    public ResponseEntity<Tag> getTagByName(@PathVariable String name) {
        return ResponseEntity.ok(tagService.findByName(name));
    }
}
