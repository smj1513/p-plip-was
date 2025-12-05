package com.pplip.domain.board.notice.persistence.dao;

import com.pplip.domain.board.notice.api.response.NoticeCommentResponse;
import com.pplip.domain.board.notice.api.response.NoticeResponse;
import com.pplip.domain.board.notice.persistence.entity.NoticeBoard;
import com.pplip.domain.board.notice.persistence.entity.NoticeComment;
import com.pplip.global.page.PageRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface NoticeCommentDao {
    List<NoticeCommentResponse.Summary> findAll(Long noticeId);

    int noticeBoardCommentAllCount(Long id);

    int insert(NoticeComment comment);

    int delete(Long id);

    int update(NoticeComment comment);


}
