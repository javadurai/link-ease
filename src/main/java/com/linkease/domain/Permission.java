package com.linkease.domain;

import com.linkease.dto.PermissionType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

@Data
@Entity
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;  // e.g., "page.links.view", "page.links.add", "page.links.delete", "job.refresh.configs"

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @EqualsAndHashCode.Exclude
    private PermissionType type;  // Enum to specify PAGE, ACTION, JOB, etc

    @ManyToMany(mappedBy = "permissions", fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    private Set<Role> roles;
}
