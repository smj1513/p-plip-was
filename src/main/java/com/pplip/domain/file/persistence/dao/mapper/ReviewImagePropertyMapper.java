package com.pplip.domain.file.persistence.dao.mapper;

import com.pplip.domain.file.persistence.entity.NoticeBoardImageProperty;
import com.pplip.domain.file.persistence.entity.ReviewImageProperty;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

/**
 * 리뷰 이미지 속성 관련 데이터베이스 작업을 위한 매퍼 인터페이스
 */
@Mapper
public interface ReviewImagePropertyMapper{
    /**
     * 새로운 리뷰 이미지 속성을 삽입합니다.
     *
     * @param property 삽입할 이미지 속성 객체
     * @return 삽입된 행의 수
     */
    int insert(ReviewImageProperty property);

    /**
     * 지정된 ID를 가진 리뷰 이미지 속성을 삭제합니다.
     *
     * @param id 삭제할 이미지 속성의 ID
     * @return 삭제된 행의 수
     */
    int delete(Long id);

    /**
     * 리뷰 이미지 속성 목록을 일괄 삽입합니다.
     *
     * @param list 삽입할 이미지 속성 객체 리스트
     * @return 삽입된 행의 수
     */
    int insertAll(List<? extends ReviewImageProperty> list);

    /**
     * 지정된 ID를 가진 리뷰 이미지 속성을 찾습니다.
     *
     * @param id 찾을 이미지 속성의 ID
     * @return 이미지 속성(존재하는 경우 Optional로 감싸짐)
     */
    Optional<ReviewImageProperty> findById(Long id);

    /**
     * 여러 리뷰 이미지의 리뷰 ID를 일괄 업데이트합니다.
     *
     * @param imageIds 업데이트할 이미지 ID 목록
     * @param reviewId 설정할 리뷰 ID
     * @return 업데이트된 행의 수
     */
    int bulkUpdateReviewId(List<Long> imageIds, Long reviewId);

    /**
     * 지정된 ID 목록으로 모든 리뷰 이미지 속성을 찾습니다.
     *
     * @param fileIds 찾을 파일 속성의 ID 목록
     * @return 리뷰 이미지 속성 목록
     */
    List<ReviewImageProperty> findAllByIds(List<Long> fileIds);

	void deleteAllById(List<Long> ids);

    List<ReviewImageProperty> findAllByReviewId(Long reviewId);
}
