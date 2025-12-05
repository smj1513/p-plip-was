package com.pplip.domain.file.utils.factory;

import com.pplip.domain.file.persistence.entity.FileProperty;
import com.pplip.domain.file.persistence.entity.ImageType;
import com.pplip.domain.file.persistence.entity.ProfileImageProperty;
import com.pplip.domain.user.persistence.entity.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ProfileImagePropertyFactory implements FilePropertyFactory{
	@Override
	public FileProperty create(String originFileName, String saveFileName, String path, String contentType, Long size,long uploader, ImageType imageType) {
		return ProfileImageProperty.builder()
				.originFileName(originFileName)
				.savedFileName(saveFileName)
				.path(path)
				.contentType(contentType)
				.size(size)
				.uploaderId(uploader)
				.createdAt(LocalDateTime.now())
				.build();
	}

	@Override
	public boolean support(ImageType imageType) {
		return ImageType.PROFILE.equals(imageType);
	}
}
