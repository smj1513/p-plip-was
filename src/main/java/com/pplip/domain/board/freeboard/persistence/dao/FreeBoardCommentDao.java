package com.pplip.domain.board.freeboard.persistence.dao;

import com.pplip.domain.board.freeboard.api.response.FreeBoardCommentResponse;
import com.pplip.domain.board.freeboard.persistence.entity.FreeComment;
import com.pplip.global.page.PageRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

/**
 * 자유게시판 댓글 데이터에 접근하는 DAO 인터페이스
 */
@Mapper
public interface FreeBoardCommentDao {

	/**
	 * 특정 게시글에 달린 댓글의 총 개수를 조회합니다.
	 *
	 * @param boardId 게시글 ID
	 * @return 댓글 총 개수
	 */
	int countAllByBoardId(@Param("boardId")Long boardId);

	/**
	 * 특정 게시글에 달린 댓글 목록을 페이징하여 조회합니다.
	 *
	 * @param boardId 게시글 ID
	 * @param pageRequest 페이징 정보
	 * @return 댓글 목록
	 */
    List<FreeBoardCommentResponse.Retrieve> findAllByBoardId(@Param("boardId") Long boardId, @Param("page") PageRequest pageRequest);

	/**
	 * 새로운 댓글을 삽입합니다.
	 *
	 * @param comment 삽입할 댓글 엔티티
	 * @return 삽입된 행의 수
	 */
    int insert(FreeComment comment);

	/**
	 * 기존 댓글을 수정합니다.
	 *
	 * @param comment 수정할 댓글 엔티티
	 * @return 수정된 행의 수
	 */
    int update(FreeComment comment);

	/**
	 * 특정 댓글을 삭제합니다.
	 *
	 * @param id 삭제할 댓글의 ID
	 * @return 삭제된 행의 수
	 */
    int delete(Long id);

	/**
	 * ID를 이용하여 댓글 상세 정보를 조회합니다.
	 *
	 * @param id 조회할 댓글의 ID
	 * @return 댓글 상세 정보 DTO
	 */
	Optional<FreeBoardCommentResponse.Detail> findByIdToDto(Long id);

	/**
	 * ID를 이용하여 댓글 엔티티를 조회합니다.
	 *
	 * @param id 조회할 댓글의 ID
	 * @return 댓글 엔티티
	 */
	Optional<FreeComment> findById(Long id);
}