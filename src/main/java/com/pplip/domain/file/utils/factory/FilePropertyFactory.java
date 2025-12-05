package com.pplip.domain.file.utils.factory;

import com.pplip.domain.file.persistence.entity.FileProperty;
import com.pplip.domain.file.persistence.entity.ImageType;
import com.pplip.domain.user.persistence.entity.User;

/**
 * FileProperty 객체 생성을 위한 팩토리 인터페이스
 */
public interface FilePropertyFactory {
    /**
     * FileProperty 객체를 생성합니다.
     *
     * @param originFileName 원본 파일 이름
     * @param saveFileName   저장될 파일 이름
     * @param path           파일 저장 경로
     * @param contentType    파일의 Content Type
     * @param size           파일 크기
     * @param uploaderId       업로더 정보
     * @param imageType      이미지 타입
     * @return 생성된 FileProperty 객체
     */
    FileProperty create(String originFileName, String saveFileName, String path, String contentType, Long size, long uploaderId, ImageType imageType);

    /**
     * 해당 이미지 타입을 지원하는지 여부를 확인합니다.
     *
     * @param imageType 이미지 타입
     * @return 지원 여부
     */
    boolean support(ImageType imageType);
}
