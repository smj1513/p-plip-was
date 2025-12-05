package com.pplip.domain.file.usecase;

import com.amazonaws.services.s3.AmazonS3;
import com.pplip.global.exception.BusinessLogicException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AwsS3FileStorage 테스트")
class AwsS3FileStorageTest {

    @Mock
    private AmazonS3 s3Client;

    @InjectMocks
    private AwsS3FileStorage awsS3FileStorage;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(awsS3FileStorage, "bucketName", "test-bucket");
    }

    @Test
    @DisplayName("성공: 파일 저장")
    void store_Success() throws IOException {
        // Given
        MockMultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", "test data".getBytes());
        String path = "some/path/test.jpg";

        // When
        awsS3FileStorage.store(file, path);

        // Then
        verify(s3Client, times(1)).putObject(eq("test-bucket"), eq(path), any(), any());
    }

    @Test
    @DisplayName("실패: 파일 저장 중 IOException 발생")
    void store_Fail_WhenIOException() throws IOException {
        // Given
        MockMultipartFile file = mock(MockMultipartFile.class);
        String path = "some/path/test.jpg";
        when(file.getInputStream()).thenThrow(new IOException());

        // When & Then
        assertThrows(BusinessLogicException.class, () -> awsS3FileStorage.store(file, path));
    }

    @Test
    @DisplayName("성공: 파일 삭제")
    void delete_Success() {
        // Given
        String path = "some/path/test.jpg";

        // When
        awsS3FileStorage.delete(path);

        // Then
        verify(s3Client, times(1)).deleteObject("test-bucket", path);
    }
}
