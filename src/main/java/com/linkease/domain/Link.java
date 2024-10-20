package com.linkease.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Entity
public class Link {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String url;

    private String description;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private User user;

    @ManyToMany
    @JoinTable(
            name = "link_tags",
            joinColumns = @JoinColumn(name = "link_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Tag> tags;
}
