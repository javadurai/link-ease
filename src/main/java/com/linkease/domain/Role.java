package com.linkease.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Set;

@Data
@Entity
@EqualsAndHashCode(of = {"id", "name"})
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @ManyToMany(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinTable(
            name = "role_permissions",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private Set<Permission> permissions;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<User> users;
}
