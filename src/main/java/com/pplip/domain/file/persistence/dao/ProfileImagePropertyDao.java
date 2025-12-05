package com.pplip.domain.file.persistence.dao;

import com.pplip.domain.file.persistence.dao.mapper.ProfileImagePropertyMapper;
import com.pplip.domain.file.persistence.entity.ImageType;
import com.pplip.domain.file.persistence.entity.ProfileImageProperty;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@RequiredArgsConstructor
@Repository
public class ProfileImagePropertyDao implements FilePropertyDao<ProfileImageProperty> {
	private final ProfileImagePropertyMapper profileImagePropertyMapper;
	@Override
	public int insert(ProfileImageProperty property) {
		return profileImagePropertyMapper.insert(property);
	}

	@Override
	public int delete(Long id) {
		return profileImagePropertyMapper.delete(id);
	}

	@Override
	public Optional<ProfileImageProperty> findById(Long id) {
		return profileImagePropertyMapper.findById(id);
	}


	@Override
	public boolean supports(ImageType imageType) {
		return ImageType.PROFILE.equals(imageType);
	}
}
