package com.pplip.domain.file.utils.factory;

import com.pplip.domain.file.persistence.entity.FileProperty;
import com.pplip.domain.file.persistence.entity.FreeBoardImageProperty;
import com.pplip.domain.file.persistence.entity.ImageType;
import com.pplip.domain.user.persistence.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("FreeBoardImagePropertyFactory 테스트")
class FreeBoardImagePropertyFactoryTest {

    private FreeBoardImagePropertyFactory factory;

    @BeforeEach
    void setUp() {
        factory = new FreeBoardImagePropertyFactory();
    }

    @Test
    @DisplayName("성공: FreeBoardImageProperty 생성")
    void create_Success() {
        // Given
        String originFileName = "origin.jpg";
        String savedFileName = "saved.jpg";
        String path = "path/to/file";
        String contentType = "image/jpeg";
        Long size = 1024L;
        Long uploader = 1L;

        // When
        FileProperty property = factory.create(originFileName, savedFileName, path, contentType, size, uploader, ImageType.FREE_BOARD);

        // Then
        assertThat(property).isNotNull();
        assertThat(property).isInstanceOf(FreeBoardImageProperty.class);
        assertThat(property.getOriginFileName()).isEqualTo(originFileName);
        assertThat(property.getSavedFileName()).isEqualTo(savedFileName);
        assertThat(property.getPath()).isEqualTo(path);
        assertThat(property.getContentType()).isEqualTo(contentType);
        assertThat(property.getSize()).isEqualTo(size);
        assertThat(property.getUploaderId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("성공: FREE_BOARD 타입 지원")
    void support_Success_ForFreeBoard() {
        // When
        boolean supports = factory.support(ImageType.FREE_BOARD);

        // Then
        assertThat(supports).isTrue();
    }

    @Test
    @DisplayName("실패: 다른 타입 미지원")
    void support_Fail_ForOtherTypes() {
        // Given
        
        // When
        boolean supportsProfile = factory.support(ImageType.PROFILE);
        boolean supportsNotice = factory.support(ImageType.NOTICE);

        // Then
        assertThat(supportsProfile).isFalse();
        assertThat(supportsNotice).isFalse();
    }
}
