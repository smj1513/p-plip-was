package com.pplip.domain.board.notice.persistence.dao;

import com.pplip.domain.board.notice.api.response.NoticeResponse;
import com.pplip.domain.board.notice.persistence.entity.NoticeBoard;
import com.pplip.global.page.PageRequest;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

/**
 * 공지사항 데이터에 접근하는 DAO 인터페이스
 */
@Mapper
public interface NoticeDao {
    List<NoticeResponse.Summary> findAll(PageRequest pageRequest);

    List<NoticeResponse.Summary> findAllByUserId(Long userId, PageRequest pageRequest);

    int noticeBoardAllCount();

    int noticeBoardAllCountByUserId(Long userId);

    Optional<NoticeResponse.Detail> findById(Long id);

    Optional<NoticeBoard> findByIdToEntity(Long id);

    int insert(NoticeBoard noticeBoard);

    int delete(Long id);

    int update(NoticeBoard noticeBoard);

	void updateViewCount(Long id);
}
