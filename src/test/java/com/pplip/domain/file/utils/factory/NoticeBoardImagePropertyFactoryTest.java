package com.pplip.domain.file.utils.factory;

import com.pplip.domain.file.persistence.entity.FileProperty;
import com.pplip.domain.file.persistence.entity.ImageType;
import com.pplip.domain.file.persistence.entity.NoticeBoardImageProperty;
import com.pplip.domain.user.persistence.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("NoticeBoardImagePropertyFactory 테스트")
class NoticeBoardImagePropertyFactoryTest {

    private NoticeBoardImagePropertyFactory factory;

    @BeforeEach
    void setUp() {
        factory = new NoticeBoardImagePropertyFactory();
    }

    @Test
    @DisplayName("성공: NoticeBoardImageProperty 생성")
    void create_Success() {
        // Given
        String originFileName = "origin.jpg";
        String savedFileName = "saved.jpg";
        String path = "path/to/file";
        String contentType = "image/jpeg";
        Long size = 1024L;

        // When
        FileProperty property = factory.create(originFileName, savedFileName, path, contentType, size, 1L, ImageType.NOTICE);

        // Then
        assertThat(property).isNotNull();
        assertThat(property).isInstanceOf(NoticeBoardImageProperty.class);
        assertThat(property.getOriginFileName()).isEqualTo(originFileName);
        assertThat(property.getSavedFileName()).isEqualTo(savedFileName);
        assertThat(property.getPath()).isEqualTo(path);
        assertThat(property.getContentType()).isEqualTo(contentType);
        assertThat(property.getSize()).isEqualTo(size);
        assertThat(property.getUploaderId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("성공: NOTICE 타입 지원")
    void support_Success_ForNotice() {
        // When
        boolean supports = factory.support(ImageType.NOTICE);

        // Then
        assertThat(supports).isTrue();
    }

    @Test
    @DisplayName("실패: 다른 타입 미지원")
    void support_Fail_ForOtherTypes() {
        // Given

        // When
        boolean supportsProfile = factory.support(ImageType.PROFILE);
        boolean supportsFreeBoard = factory.support(ImageType.FREE_BOARD);

        // Then
        assertThat(supportsProfile).isFalse();
        assertThat(supportsFreeBoard).isFalse();
    }
}
