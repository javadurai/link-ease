package com.linkease.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Data
@Entity
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;  // e.g., "page.links.view", "page.links.add", "page.links.delete", "job.refresh.configs"

    // Example: pages, buttons, jobs, etc.
    @Column(nullable = false)
    private String type;

    @ManyToMany(mappedBy = "permissions", fetch = FetchType.LAZY)
    private Set<Role> roles;
}
