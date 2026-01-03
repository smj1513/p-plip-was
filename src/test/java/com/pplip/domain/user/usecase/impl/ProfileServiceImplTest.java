package com.pplip.domain.user.usecase.impl;

import com.pplip.domain.auth.utils.SecurityUtils;
import com.pplip.domain.file.persistence.dao.ProfileImagePropertyDao;
import com.pplip.domain.file.persistence.entity.ImageType;
import com.pplip.domain.file.persistence.entity.ProfileImageProperty;
import com.pplip.domain.file.usecase.FileService;
import com.pplip.domain.user.api.request.ProfileRequest;
import com.pplip.domain.user.persistence.dao.ProfileDao;
import com.pplip.domain.user.persistence.entity.Profile;
import com.pplip.global.api.code.ErrorCode;
import com.pplip.global.exception.BusinessLogicException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProfileServiceImpl 테스트")
class ProfileServiceImplTest {

    @InjectMocks
    private ProfileServiceImpl profileService;

    @Mock
    private ProfileDao profileDao;

    @Mock
    private ProfileImagePropertyDao profileImagePropertyDao;

    @Mock
    private FileService fileService;

    @Mock
    private UserDetails userDetails;

    private MockedStatic<SecurityUtils> securityUtilsMock;

    @BeforeEach
    void setUp() {
        securityUtilsMock = mockStatic(SecurityUtils.class);
    }
    
    @AfterEach
    void tearDown() {
        securityUtilsMock.close();
    }


    private Profile createProfile(Long userId, Long profileId) {
        return Profile.builder()
                .id(profileId)
                .userId(userId)
                .nickname("기존 닉네임")
                .description("기존 한 줄 소개")
                .build();
    }

    private ProfileImageProperty createProfileImageProperty(Long imageId, Long profileId) {
        return ProfileImageProperty.builder()
                .id(imageId)
                .profileId(profileId)
                .originFileName("originalName.jpg")
                .savedFileName("storedName.jpg")
                .path("http://example.com/imageUrl")
                .size(100L)
                .contentType("image/jpeg")
                .build();
    }

    @Nested
    @DisplayName("modifyInfo 메소드는")
    class Describe_modifyToDoUpdatedInfo {

        private final Long userId = 1L;
        private final Long profileId = 10L;

        @Test
        @DisplayName("텍스트 정보만 성공적으로 수정한다")
        void modifyInfo_Success_TextOnly() {
            // given
            ProfileRequest.UpdateInfo request = new ProfileRequest.UpdateInfo("새 닉네임", "새 한 줄 소개", null);
            Profile profile = spy(createProfile(userId, profileId));

            given(SecurityUtils.resolveUserId(any(UserDetails.class))).willReturn(userId);
            given(profileDao.findByUserId(userId)).willReturn(Optional.of(profile));

            // when
            profileService.modifyInfo(request, userDetails);

            // then
            verify(profile).setNickname(request.getNickname());
            verify(profile).setDescription(request.getDesc());
            verify(fileService, never()).remove(anyLong(), any(ImageType.class), any(UserDetails.class));
            verify(profileImagePropertyDao, never()).delete(anyLong());
        }
        @Test
        @DisplayName("새 이미지로 성공적으로 수정한다 (기존 이미지 존재)")
        void modifyInfo_Success_WithNewImage() {
            // given
            Long oldImageId = 100L;
            Long newImageId = 101L;
            ProfileRequest.UpdateInfo request = new ProfileRequest.UpdateInfo("새 닉네임", "새 한 줄 소개", newImageId);

            // spy는 실제 객체를 감싸므로 주의 필요. Mock 객체라면 아래 given 사용이 더 자연스러움.
            Profile profile = spy(createProfile(userId, profileId));
            ProfileImageProperty oldImage = createProfileImageProperty(oldImageId, profileId);
            ProfileImageProperty newImage = spy(createProfileImageProperty(newImageId, null));

            given(SecurityUtils.resolveUserId(any(UserDetails.class))).willReturn(userId);
            given(profileDao.findByUserId(userId)).willReturn(Optional.of(profile));
            given(profileImagePropertyDao.findByProfileId(profileId)).willReturn(Optional.of(oldImage));
            given(profileImagePropertyDao.findById(newImageId)).willReturn(Optional.of(newImage));

            // [수정 포인트 1] fileService.remove가 void라면 doNothing 유지,
            // 만약 boolean 등을 반환하면 given(...).willReturn(...)으로 변경해야 함.
// int를 반환하므로 1(삭제 성공)을 리턴하도록 설정
            given(profileImagePropertyDao.delete(oldImageId)).willReturn(1);
            // [수정 포인트 2] DAO delete가 int(영향받은 행 수)를 반환한다면 doNothing 대신 willReturn 사용
            // 예: 삭제 성공 시 1 반환
            given(profileImagePropertyDao.delete(oldImageId)).willReturn(1);

            // when
            profileService.modifyInfo(request, userDetails);

            // then
            verify(profile).setNickname(request.getNickname());
            verify(profile).setDescription(request.getDesc());
            verify(fileService).remove(oldImageId, ImageType.PROFILE, userDetails);

            // verify는 리턴 타입 상관없이 호출 여부만 확인하므로 그대로 둠
            verify(profileImagePropertyDao).delete(oldImageId);
            verify(newImage).setProfileId(profileId);
        }

        @Test
        @DisplayName("기존 이미지 없이 새 이미지를 추가한다")
        void modifyInfo_Success_AddNewImage() {
            // given
            Long newImageId = 101L;
            ProfileRequest.UpdateInfo request = new ProfileRequest.UpdateInfo("새 닉네임", "새 한 줄 소개", newImageId);
            Profile profile = createProfile(userId, profileId);
            ProfileImageProperty newImage = spy(createProfileImageProperty(newImageId, null));

            given(SecurityUtils.resolveUserId(any(UserDetails.class))).willReturn(userId);
            given(profileDao.findByUserId(userId)).willReturn(Optional.of(profile));
            given(profileImagePropertyDao.findByProfileId(profileId)).willReturn(Optional.empty());
            given(profileImagePropertyDao.findById(newImageId)).willReturn(Optional.of(newImage));

            // when
            profileService.modifyInfo(request, userDetails);

            // then
            verify(fileService, never()).remove(anyLong(), any(ImageType.class), any(UserDetails.class));
            verify(profileImagePropertyDao, never()).delete(anyLong());
            verify(newImage).setProfileId(profileId);
        }

        @Test
        @DisplayName("사용자 프로필을 찾지 못하면 BusinessLogicException을 던진다")
        void modifyInfo_Fail_UserNotFound() {
            // given
            ProfileRequest.UpdateInfo request = new ProfileRequest.UpdateInfo("닉네임", "소개", null);
            given(SecurityUtils.resolveUserId(any(UserDetails.class))).willReturn(userId);
            given(profileDao.findByUserId(userId)).willReturn(Optional.empty());

            // when & then
            BusinessLogicException exception = assertThrows(BusinessLogicException.class,
                    () -> profileService.modifyInfo(request, userDetails));
            assertEquals(ErrorCode.USER_NOT_FOUND_ERROR, exception.getErrorCode());
        }

        @Test
        @DisplayName("새 이미지 ID가 유효하지 않으면 BusinessLogicException을 던진다")
        void modifyInfo_Fail_NewImageNotFound() {
            // given
            Long invalidImageId = 999L;
            ProfileRequest.UpdateInfo request = new ProfileRequest.UpdateInfo("닉네임", "소개", invalidImageId);
            Profile profile = createProfile(userId, profileId);

            given(SecurityUtils.resolveUserId(any(UserDetails.class))).willReturn(userId);
            given(profileDao.findByUserId(userId)).willReturn(Optional.of(profile));
            given(profileImagePropertyDao.findById(invalidImageId)).willReturn(Optional.empty());

            // when & then
            BusinessLogicException exception = assertThrows(BusinessLogicException.class,
                    () -> profileService.modifyInfo(request, userDetails));
            assertEquals(ErrorCode.FILE_NOT_FOUND, exception.getErrorCode());
        }
    }
}