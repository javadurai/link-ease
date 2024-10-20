package com.linkease.service;

import com.linkease.domain.Link;
import com.linkease.domain.User;
import com.linkease.repository.LinkRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LinkService {

    private final LinkRepository linkRepository;

    public LinkService(LinkRepository linkRepository) {
        this.linkRepository = linkRepository;
    }

    public Link createLink(Link link, User user) {
        link.setUser(user);
        link.setCreatedAt(LocalDateTime.now());
        link.setUpdatedAt(LocalDateTime.now());
        return linkRepository.save(link);
    }

    public List<Link> getAllLinksForUser(Long userId) {
        return linkRepository.findAllByUserId(userId);
    }

    public Link updateLink(Link link) {
        link.setUpdatedAt(LocalDateTime.now());
        return linkRepository.save(link);
    }

    public void deleteLink(Long id) {
        linkRepository.deleteById(id);
    }
}
