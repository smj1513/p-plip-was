package com.pplip.domain.board.freeboard.persistence.dao;

import com.pplip.domain.board.freeboard.api.response.FreeBoardCommentResponse;
import com.pplip.domain.board.freeboard.persistence.entity.FreeComment;
import com.pplip.global.page.PageRequest;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FreeBoardCommentDao {
    List<FreeBoardCommentResponse.Retrieve> findAll(Long boardId);

    int insert(FreeComment comment);

    int update(FreeComment comment);

    int delete(Long id);
}