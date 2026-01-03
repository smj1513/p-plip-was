package com.pplip.domain.board.freeboard.persistence.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserLikeBoardDao {

    /**
     * 유저 아이디와 보드아이디로 좋아요가 존재하는지 체크합니다.
     * @param boardId 게시글 아이디
     * @param userId 유저 아이디
     * @return 존재 하는지
     */
    boolean findByBoardIdUserId(@Param("boardId") Long boardId, @Param("userId") Long userId);

    /**
     * 테이블에 좋아요를 삽입합니다.
     *
     * @param boardId 게시글 아이디
     * @param userId 유저 아이디
     * @return 삽입된 행의 수
     */
    int insert(@Param("boardId") Long boardId, @Param("userId") Long userId);

    /**
     * 테이블에 좋아요를 삭제(취소)합니다.
     *
     * @param boardId 게시글 아이디
     * @param userId 유저 아이디
     * @return 삽입된 행의 수
     */
    int delete(@Param("boardId") Long boardId, @Param("userId") Long userId);
}
