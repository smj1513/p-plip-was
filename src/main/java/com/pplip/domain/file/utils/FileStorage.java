package com.pplip.domain.file.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 파일 저장소 작업을 위한 계약을 정의하는 인터페이스
 */
public interface FileStorage {
    /**
     * 지정된 경로에 파일을 저장합니다.
     *
     * @param file 저장할 파일
     * @param path 파일이 저장될 경로
     */
    void store(MultipartFile file, String path);

    /**
     * 지정된 경로의 파일을 삭제합니다.
     *
     * @param path 삭제할 파일의 경로
     */
    void delete(String path);
}
