package com.pplip.domain.board.freeboard.persistence.dao;

import com.pplip.domain.board.freeboard.api.response.FreeBoardResponse;
import com.pplip.domain.board.freeboard.persistence.entity.FreeBoard;
import com.pplip.global.page.PageRequest;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface FreeBoardDao {
    List<FreeBoardResponse.BoardList> findAll(PageRequest pageRequest);

    int freeBoardAllCount();

    Optional<FreeBoardResponse.Detail> findById(Long id);

    int insert(FreeBoard freeBoard);

    int update(FreeBoard freeBoard);

    int delete(Long id);
}
