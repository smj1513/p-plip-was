package com.pplip.domain.file.persistence.dao;

import com.pplip.domain.file.persistence.entity.FileProperty;

import java.util.List;


/**
 * {@link FilePropertyDao}를 확장하여 배치 처리를 지원하는 인터페이스
 * @param <T> 파일 속성 타입
 */
public interface BatchSupportFilePropertyDao<T extends FileProperty> extends FilePropertyDao<T>  {
	/**
	 * 여러 이미지의 참조 ID를 일괄적으로 업데이트합니다.
	 *
	 * @param imageIds 업데이트할 이미지 ID 목록
	 * @param refId 참조 ID
	 * @return 업데이트된 행의 수
	 */
	int bulkUpdate(List<Long> imageIds, Long refId);

	/**
	 * 여러 파일 속성을 한 번에 저장합니다.
	 *
	 * @param list 저장할 파일 속성 목록
	 * @return 삽입된 행의 수
	 */
	int insertAll(List<T> list);

	/**
	 * 지정된 ID 목록으로 모든 파일 속성을 찾습니다.
	 *
	 * @param fileIds 찾을 파일 속성의 ID 목록
	 * @return 파일 속성 목록
	 */
	List<T> findAllByIds(List<Long> fileIds);

	void deleteAllById(List<Long> ids);
}
