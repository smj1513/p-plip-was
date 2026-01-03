package com.pplip.domain.board.freeboard.persistence.dao;

import com.pplip.domain.board.freeboard.api.response.FreeBoardResponse;
import com.pplip.domain.board.freeboard.persistence.entity.FreeBoard;
import com.pplip.domain.board.freeboard.persistence.entity.FreeBoardSort;
import com.pplip.global.page.PageRequest;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

/**
 * 자유게시판 데이터에 접근하는 DAO 인터페이스
 */
@Mapper
public interface FreeBoardDao {
    /**
     * 모든 자유게시판 게시글을 페이징하여 조회합니다.
     *
     * @param pageRequest 페이징 정보
     * @param sort 정렬 정보
     * @return 페이징된 게시글 목록
     */
    List<FreeBoardResponse.BoardList> findAll(PageRequest pageRequest, FreeBoardSort sort);

    /**
     * 사용자의 자유게시판 게시글을 페이징하여 조회합니다.
     *
     * @param pageRequest 페이징 정보
     * @param userId 유저 아이디
     * @param sort 정렬 정보
     * @return 페이징된 게시글 목록
     */
    List<FreeBoardResponse.BoardList> findAllByUserId(PageRequest pageRequest, Long userId, FreeBoardSort sort);

    /**
     * 모든 자유게시판 게시글의 총 개수를 조회합니다.
     *
     * @return 게시글 총 개수
     */
    int countAll();

    /**
     * 사용자의 자유게시판 게시글의 총 개수를 조회합니다.
     *
     * @param userId 유저 아이디
     * @return 게시글 총 개수
     */
    int countAllByUserId(Long userId);


    /**
     * ID를 이용하여 게시글 상세 정보를 조회합니다.
     *
     * @param id 조회할 게시글의 ID
     * @return 게시글 상세 정보 DTO
     */
    Optional<FreeBoardResponse.Detail> findByIdToDto(Long id);

    /**
     * ID를 이용하여 게시글 엔티티를 조회합니다.
     *
     * @param id 조회할 게시글의 ID
     * @return 게시글 엔티티
     */
    Optional<FreeBoard> findById(Long id);

    /**
     * 새로운 게시글을 삽입합니다.
     *
     * @param freeBoard 삽입할 게시글 엔티티
     * @return 삽입된 행의 수
     */
    int insert(FreeBoard freeBoard);

    /**
     * 기존 게시글을 수정합니다.
     *
     * @param freeBoard 수정할 게시글 엔티티
     * @return 수정된 행의 수
     */
    int update(FreeBoard freeBoard);

    /**
     * 특정 게시글을 삭제합니다.
     *
     * @param id 삭제할 게시글의 ID
     * @return 삭제된 행의 수
     */
    int delete(Long id);

    /**
     * 게시글의 조회수를 1 증가시킵니다.
     *
     * @param id 조회수를 증가시킬 게시글의 ID
     */
    void updateViewCount(Long id);
}
