package com.pplip.domain.file.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("FilePathGenerator 테스트")
class FilePathGeneratorTest {

    private FilePathGenerator filePathGenerator;

    @BeforeEach
    void setUp() {
        filePathGenerator = new FilePathGenerator();
    }

    @Test
    @DisplayName("성공: 파일 경로 생성")
    void generatePath_Success() {
        // Given
        String saveName = "test-save-name.jpg";
        LocalDateTime now = LocalDateTime.now();
        String expectedPathPrefix = now.getYear() + "/" + now.getMonth().toString() + "/" + now.getDayOfMonth();

        // When
        String generatedPath = filePathGenerator.generatePath(saveName);

        // Then
        assertThat(generatedPath).startsWith(expectedPathPrefix);
        assertThat(generatedPath).endsWith(saveName);
    }

    @Test
    @DisplayName("성공: 저장 파일 이름 생성")
    void generateSaveFileName_Success() {
        // Given
        String originFileName = "original.png";

        // When
        String saveFileName = filePathGenerator.generateSaveFileName(originFileName);

        // Then
        assertThat(saveFileName).isNotNull();
        assertThat(saveFileName).isNotEqualTo(originFileName);
        assertThat(saveFileName).endsWith(".png");
        assertThat(saveFileName).doesNotContain("-");
    }
}
