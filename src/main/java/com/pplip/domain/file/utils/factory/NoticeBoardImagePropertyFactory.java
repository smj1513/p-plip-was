package com.pplip.domain.file.utils.factory;

import com.pplip.domain.file.persistence.entity.FileProperty;
import com.pplip.domain.file.persistence.entity.ImageType;
import com.pplip.domain.file.persistence.entity.NoticeBoardImageProperty;
import com.pplip.domain.user.persistence.entity.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class NoticeBoardImagePropertyFactory implements FilePropertyFactory{
	@Override
	public FileProperty create(String originFileName, String saveFileName, String path, String contentType, Long size, long uploader, ImageType imageType) {
		return NoticeBoardImageProperty.builder()
				.originFileName(originFileName)
				.size(size)
				.path(path)
				.savedFileName(saveFileName)
				.contentType(contentType)
				.uploaderId(uploader)
				.createdAt(LocalDateTime.now())
				.build();
	}

	@Override
	public boolean support(ImageType imageType) {
		return ImageType.NOTICE.equals(imageType);
	}
}
