package com.linkease.controller;

import com.linkease.domain.Link;
import com.linkease.domain.User;
import com.linkease.service.LinkService;
import com.linkease.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/links")
public class LinkController {

    private final LinkService linkService;
    private final UserService userService;

    public LinkController(LinkService linkService, UserService userService) {
        this.linkService = linkService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<Link> createLink(@RequestBody Link link, @RequestParam Long userId) {
        User user = userService.findById(userId);
        return ResponseEntity.ok(linkService.createLink(link, user));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Link>> getAllLinksForUser(@PathVariable Long userId) {
        return ResponseEntity.ok(linkService.getAllLinksForUser(userId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Link> updateLink(@PathVariable Long id, @RequestBody Link link) {
        link.setId(id);
        return ResponseEntity.ok(linkService.updateLink(link));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLink(@PathVariable Long id) {
        linkService.deleteLink(id);
        return ResponseEntity.ok().build();
    }
}
