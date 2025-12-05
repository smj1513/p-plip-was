package com.pplip.domain.file.api.response;

import com.pplip.domain.file.persistence.entity.ImageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileResponse {
    private Long id;
    private String name;
    private String contentType;
    private Long size;
    private String path;
    private ImageType imageType;
}
