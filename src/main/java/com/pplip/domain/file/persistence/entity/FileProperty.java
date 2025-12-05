package com.pplip.domain.file.persistence.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class FileProperty {
    private Long id;
    private String originFileName;
    private String path;
    private String contentType;
    private String savedFileName;
    private Long uploaderId;
    private long size;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
