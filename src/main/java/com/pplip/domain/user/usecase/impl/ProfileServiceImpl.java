package com.pplip.domain.user.usecase.impl;

import com.pplip.domain.auth.utils.SecurityUtils;
import com.pplip.domain.file.api.response.FileResponse;
import com.pplip.domain.file.persistence.dao.ProfileImagePropertyDao;
import com.pplip.domain.file.persistence.entity.ImageType;
import com.pplip.domain.file.persistence.entity.ProfileImageProperty;
import com.pplip.domain.file.usecase.FileService;
import com.pplip.domain.user.api.request.ProfileRequest;
import com.pplip.domain.user.api.response.ProfileResponse;
import com.pplip.domain.user.persistence.dao.ProfileDao;
import com.pplip.domain.user.persistence.entity.Profile;
import com.pplip.domain.user.usecase.ProfileService;
import com.pplip.global.api.code.ErrorCode;
import com.pplip.global.exception.BusinessLogicException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ProfileServiceImpl implements ProfileService {
	private final ProfileDao profileDao;
	private final ProfileImagePropertyDao profileImagePropertyDao;
	private final FileService fileService;

	@Override
	public ProfileResponse.UpdatedInfo modifyInfo(ProfileRequest.UpdateInfo request, UserDetails userDetails) {
		Long userId = SecurityUtils.resolveUserId(userDetails);

		// 1. 프로필 본문 수정 (텍스트 정보)
		Profile profile = profileDao.findByUserId(userId)
				.orElseThrow(() -> new BusinessLogicException(ErrorCode.USER_NOT_FOUND_ERROR, "등록되지 않은 사용자입니다."));

		profile.setDescription(request.getDesc());
		profile.setNickname(request.getNickname());

		// 2. 이미지 처리 로직
		ProfileImageProperty currentImage = resolveProfileImage(profile.getId(), request.getImageId(), userDetails);
		// 3. 응답 생성 (이미지가 있으면 변환, 없으면 null)
		FileResponse fileResponse = null;
		if (currentImage != null) {
			fileResponse = toFileResponse(currentImage);
			profileImagePropertyDao.insert(currentImage);
		}

		profileDao.update(profile);
		return ProfileResponse.UpdatedInfo.builder()
				.desc(profile.getDescription())
				.nickname(profile.getNickname())
				.profileImage(fileResponse)
				.build();
	}

	@Override
	public ProfileResponse.Info getInfo(UserDetails userDetails) {
		Long userId = SecurityUtils.resolveUserId(userDetails);
		ProfileResponse.Info profile = profileDao.findByIdToDto(userId);
		if (profile.getProfileImage() != null) {
			profile.getProfileImage().setImageType(ImageType.PROFILE);
		}
		return profile;
	}

	/**
	 * 이미지 변경 요청 여부에 따라 적절한 이미지 엔티티를 반환하는 메서드
	 */
	private ProfileImageProperty resolveProfileImage(Long profileId, Long newImageId, UserDetails userDetails) {
		if (newImageId == null) {
			return profileImagePropertyDao.findByProfileId(profileId).orElse(null);
		}

		return processProfileImageChange(profileId, newImageId, userDetails);
	}

	/**
	 * 실제 이미지 교체(삭제 및 등록) 트랜잭션 로직
	 */
	private ProfileImageProperty processProfileImageChange(Long profileId, Long newImageId, UserDetails userDetails) {
		ProfileImageProperty newImage = profileImagePropertyDao.findById(newImageId)
				.orElseThrow(() -> new BusinessLogicException(ErrorCode.FILE_NOT_FOUND, "존재하지 않는 파일입니다."));

		Optional<ProfileImageProperty> oldImageOpt = profileImagePropertyDao.findByProfileId(profileId);

		oldImageOpt.ifPresent(oldImage -> {
			if (!oldImage.getId().equals(newImageId)) {
				// 1. S3 실제 파일 삭제 (FileService 위임)
				fileService.remove(oldImage.getId(), ImageType.PROFILE, userDetails);
				profileImagePropertyDao.delete(oldImage.getId());
			}
		});
		newImage.setProfileId(profileId);
		return newImage;
	}

	/**
	 * 엔티티 -> 응답 DTO 변환 (Mapper 메서드)
	 * 코드 중복을 줄이기 위해 분리
	 */
	private FileResponse toFileResponse(ProfileImageProperty image) {
		return FileResponse.builder()
				.id(image.getId())
				.size(image.getSize())
				.name(image.getSavedFileName())
				.path(image.getPath()) // CloudFront URL 등
				.imageType(ImageType.PROFILE)
				.contentType(image.getContentType())
				.build();
	}
}
