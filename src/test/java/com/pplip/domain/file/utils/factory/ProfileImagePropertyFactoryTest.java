package com.pplip.domain.file.utils.factory;

import com.pplip.domain.file.persistence.entity.FileProperty;
import com.pplip.domain.file.persistence.entity.ImageType;
import com.pplip.domain.file.persistence.entity.ProfileImageProperty;
import com.pplip.domain.user.persistence.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ProfileImagePropertyFactory 테스트")
class ProfileImagePropertyFactoryTest {

    private ProfileImagePropertyFactory factory;

    @BeforeEach
    void setUp() {
        factory = new ProfileImagePropertyFactory();
    }

    @Test
    @DisplayName("성공: ProfileImageProperty 생성")
    void create_Success() {
        // Given
        String originFileName = "origin.jpg";
        String savedFileName = "saved.jpg";
        String path = "path/to/file";
        String contentType = "image/jpeg";
        Long size = 1024L;
        Long uploader = 1L;

        // When
        FileProperty property = factory.create(originFileName, savedFileName, path, contentType, size, uploader, ImageType.PROFILE);

        // Then
        assertThat(property).isNotNull();
        assertThat(property).isInstanceOf(ProfileImageProperty.class);
        assertThat(property.getOriginFileName()).isEqualTo(originFileName);
        assertThat(property.getSavedFileName()).isEqualTo(savedFileName);
        assertThat(property.getPath()).isEqualTo(path);
        assertThat(property.getContentType()).isEqualTo(contentType);
        assertThat(property.getSize()).isEqualTo(size);
        assertThat(property.getUploaderId()).isEqualTo(uploader);
    }

    @Test
    @DisplayName("성공: PROFILE 타입 지원")
    void support_Success_ForProfile() {
        // When
        boolean supports = factory.support(ImageType.PROFILE);

        // Then
        assertThat(supports).isTrue();
    }

    @Test
    @DisplayName("실패: 다른 타입 미지원")
    void support_Fail_ForOtherTypes() {
        // Given

        // When
        boolean supportsNotice = factory.support(ImageType.NOTICE);
        boolean supportsFreeBoard = factory.support(ImageType.FREE_BOARD);

        // Then
        assertThat(supportsNotice).isFalse();
        assertThat(supportsFreeBoard).isFalse();
    }
}
