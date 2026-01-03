package com.pplip.domain.board.notice.usecase;

import com.pplip.domain.board.notice.api.request.NoticeCommentRequest;
import com.pplip.domain.board.notice.api.response.NoticeCommentResponse;
import com.pplip.global.page.Page;
import com.pplip.global.page.PageRequest;
import org.springframework.security.core.userdetails.UserDetails;

public interface NoticeCommentService {
    Page<NoticeCommentResponse.Summary> findAllByBoardId(Long BoardId, PageRequest pageRequest);

    NoticeCommentResponse.Detail post(NoticeCommentRequest.Post post, Long boardId, UserDetails userDetails);

    NoticeCommentResponse.Update update(NoticeCommentRequest.Update update, Long id, UserDetails userDetails);

    void delete(Long id, UserDetails userDetails);
}
