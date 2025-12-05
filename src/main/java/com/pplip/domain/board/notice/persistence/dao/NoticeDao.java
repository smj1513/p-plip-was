package com.pplip.domain.board.notice.persistence.dao;

import com.pplip.domain.board.notice.api.response.NoticeResponse;
import com.pplip.domain.board.notice.persistence.entity.NoticeBoard;
import com.pplip.global.page.PageRequest;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface NoticeDao {
    List<NoticeResponse.Summary> findAll(PageRequest pageRequest);

    int noticeBoardAllCount();

    Optional<NoticeResponse.Detail> findById(Long id);

    int insert(NoticeBoard noticeBoard);

    int delete(Long id);

    int update(NoticeBoard noticeBoard);
}
