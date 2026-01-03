package com.pplip.domain.file.utils;


import com.pplip.domain.file.persistence.entity.FileProperty;
import com.pplip.domain.file.persistence.entity.ImageType;
import com.pplip.domain.file.utils.factory.FilePropertyFactory;
import com.pplip.domain.user.persistence.entity.User;
import com.pplip.global.api.code.ErrorCode;
import com.pplip.global.exception.BusinessLogicException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FilePropertyProvider {
    private final List<FilePropertyFactory> fileProperties;
    private final FilePathGenerator filePathGenerator;

    public FileProperty create(String fileName, String contentType, Long size, long uploaderId, ImageType imageType) {
        String savedFileName = filePathGenerator.generateSaveFileName(fileName);
        String path = filePathGenerator.generatePath(savedFileName);
        for (FilePropertyFactory factory : fileProperties) {
            if (factory.support(imageType)) {
                return factory.create(fileName, savedFileName, path, contentType, size, uploaderId, imageType);
            }
        }

        throw new BusinessLogicException(ErrorCode.FILE_TYPE_NOT_SUPPORT, "지원하지 않는 파일 타입입니다.");
    }
}
