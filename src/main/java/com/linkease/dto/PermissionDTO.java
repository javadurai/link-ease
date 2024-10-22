package com.linkease.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PermissionDTO {
    private Long id;
    private String name;
    private boolean assigned;
}

