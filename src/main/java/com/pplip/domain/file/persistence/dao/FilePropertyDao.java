package com.pplip.domain.file.persistence.dao;

import com.pplip.domain.file.persistence.entity.FileProperty;
import com.pplip.domain.file.persistence.entity.FreeBoardImageProperty;
import com.pplip.domain.file.persistence.entity.ImageType;

import java.util.List;
import java.util.Optional;

/**
 * 파일 속성 데이터 액세스 작업을 위한 계약을 정의하는 인터페이스
 * @param <T> 파일 속성 타입
 */
public interface FilePropertyDao <T extends FileProperty>{
	/**
	 * 파일 속성을 저장합니다.
	 *
	 * @param property 저장할 파일 속성
	 * @return 삽입된 행의 수
	 */
	int insert(T property);

	/**
	 * 지정된 ID의 파일 속성을 삭제합니다.
	 *
	 * @param id 삭제할 파일 속성의 ID
	 * @return 삭제된 행의 수
	 */
	int delete(Long id);

	/**
	 * 지정된 ID의 파일 속성을 찾습니다.
	 *
	 * @param id 찾을 파일 속성의 ID
	 * @return 파일 속성(옵셔널)
	 */
	Optional<T> findById(Long id);

	/**
	 * 지정된 이미지 유형을 지원하는지 확인합니다.
	 *
	 * @param imageType 확인할 이미지 유형
	 * @return 지원하면 true, 그렇지 않으면 false
	 */
	boolean supports(ImageType imageType);
}
