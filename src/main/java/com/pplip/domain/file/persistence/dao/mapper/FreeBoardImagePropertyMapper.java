package com.pplip.domain.file.persistence.dao.mapper;

import com.pplip.domain.file.persistence.entity.FreeBoardImageProperty;
import com.pplip.domain.file.persistence.entity.ReviewImageProperty;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

/**
 * 자유 게시판 이미지 속성 관련 데이터베이스 작업을 위한 매퍼 인터페이스
 */
@Mapper
public interface FreeBoardImagePropertyMapper{
    /**
     * 새로운 자유 게시판 이미지 속성을 삽입합니다.
     *
     * @param property 삽입할 이미지 속성 객체
     * @return 삽입된 행의 수
     */
    int insert(FreeBoardImageProperty property);

    /**
     * 지정된 ID를 가진 자유 게시판 이미지 속성을 삭제합니다.
     *
     * @param id 삭제할 이미지 속성의 ID
     * @return 삭제된 행의 수
     */
    int delete(Long id);

    /**
     * 지정된 ID를 가진 자유 게시판 이미지 속성을 찾습니다.
     *
     * @param id 찾을 이미지 속성의 ID
     * @return 이미지 속성(존재하는 경우 Optional로 감싸짐)
     */
    Optional<FreeBoardImageProperty> findById(Long id);

    /**
     * 자유 게시판 이미지 속성 목록을 일괄 삽입합니다.
     *
     * @param list 삽입할 이미지 속성 객체 리스트
     * @return 삽입된 행의 수
     */
    int insertAll(List<? extends FreeBoardImageProperty> list);

    /**
     * 여러 자유 게시판 이미지의 게시판 ID를 일괄 업데이트합니다.
     *
     * @param imageIds 업데이트할 이미지 ID 목록
     * @param boardId 설정할 게시판 ID
     * @return 업데이트된 행의 수
     */
    int bulkUpdateBoardId(List<Long> imageIds, Long boardId);

    /**
     * 지정된 ID 목록으로 모든 자유 게시판 이미지 속성을 찾습니다.
     *
     * @param fileIds 찾을 파일 속성의 ID 목록
     * @return 자유 게시판 이미지 속성 목록
     */
    List<FreeBoardImageProperty> findAllByIds(List<Long> fileIds);

	List<FreeBoardImageProperty> findByBoardId(Long boardId);

    void deleteAllByid(List<Long> ids);
}
