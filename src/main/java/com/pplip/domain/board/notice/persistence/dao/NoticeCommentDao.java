package com.pplip.domain.board.notice.persistence.dao;

import com.pplip.domain.board.notice.api.response.NoticeCommentResponse;
import com.pplip.domain.board.notice.api.response.NoticeResponse;
import com.pplip.domain.board.notice.persistence.entity.NoticeBoard;
import com.pplip.domain.board.notice.persistence.entity.NoticeComment;
import com.pplip.global.page.PageRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

/**
 * 공지사항 댓글 데이터에 접근하는 DAO 인터페이스
 */
@Mapper
public interface NoticeCommentDao {
    List<NoticeCommentResponse.Summary> findAll(Long noticeId, PageRequest pageRequest);

    Optional<NoticeCommentResponse.Detail> findById(Long id);

    Optional<NoticeComment> findByIdToEntity(Long id);

    int noticeBoardCommentAllCount(Long id);

    int insert(NoticeComment comment);

    int delete(Long id);

    int update(NoticeComment comment);


}
