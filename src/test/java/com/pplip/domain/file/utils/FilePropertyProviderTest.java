package com.pplip.domain.file.utils;

import com.pplip.domain.file.persistence.entity.FileProperty;
import com.pplip.domain.file.persistence.entity.ImageType;
import com.pplip.domain.file.utils.factory.FilePropertyFactory;
import com.pplip.domain.user.persistence.entity.User;
import com.pplip.global.exception.BusinessLogicException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("FilePropertyProvider 테스트")
class FilePropertyProviderTest {

    @Mock
    private FilePathGenerator filePathGenerator;

    @Mock
    private FilePropertyFactory filePropertyFactory;

    @InjectMocks
    private FilePropertyProvider filePropertyProvider;

    @Test
    @DisplayName("성공: 파일 속성 생성")
    void create_Success() {
        // Given
        String fileName = "test.jpg";
        String contentType = "image/jpeg";
        Long size = 1024L;
        Long uploader = 1L;
        ImageType imageType = ImageType.FREE_BOARD;

        // filePropertyProvider의 fileProperties 필드를 mock FilePropertyFactory를 포함하는 리스트로 설정
        filePropertyProvider = new FilePropertyProvider(List.of(filePropertyFactory), filePathGenerator);

        when(filePathGenerator.generatePath(anyString())).thenReturn("some/path/test.jpg");
        when(filePathGenerator.generateSaveFileName(anyString())).thenReturn("saved-name.jpg");
        when(filePropertyFactory.support(imageType)).thenReturn(true);
        when(filePropertyFactory.create(anyString(), anyString(), anyString(), anyString(), anyLong(),anyLong(), any(ImageType.class)))
                .thenReturn(mock(FileProperty.class));

        // When
        FileProperty result = filePropertyProvider.create(fileName, contentType, size, uploader, imageType);

        // Then
        assertThat(result).isNotNull();
        verify(filePropertyFactory, times(1)).create(anyString(), anyString(), anyString(), anyString(), anyLong(), anyLong(), any(ImageType.class));
    }

    @Test
    @DisplayName("실패: 지원하는 팩토리가 없음")
    void create_Fail_WhenNoSupportingFactory() {
        // Given
        String fileName = "test.jpg";
        String contentType = "image/jpeg";
        Long size = 1024L;
        Long uploader = 1L;
        ImageType imageType = ImageType.FREE_BOARD;

        filePropertyProvider = new FilePropertyProvider(Collections.emptyList(), filePathGenerator);
        
        when(filePathGenerator.generatePath(anyString())).thenReturn("some/path/test.jpg");
        when(filePathGenerator.generateSaveFileName(anyString())).thenReturn("saved-name.jpg");

        // When & Then
        assertThatThrownBy(() -> filePropertyProvider.create(fileName, contentType, size, uploader, imageType))
                .isInstanceOf(BusinessLogicException.class);
    }
}
