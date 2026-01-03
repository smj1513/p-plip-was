package com.pplip.domain.board.notice.usecase;

import com.pplip.domain.board.notice.api.request.NoticeRequest;
import com.pplip.domain.board.notice.api.response.NoticeResponse;
import com.pplip.global.page.Page;
import com.pplip.global.page.PageRequest;
import org.springframework.security.core.userdetails.UserDetails;

public interface NoticeService {
    Page<NoticeResponse.Summary> findAll(PageRequest pageRequest);

    Page<NoticeResponse.Summary> findAllByUserId(UserDetails userDetails, PageRequest pageRequest);

    NoticeResponse.Detail post(NoticeRequest.Post post, UserDetails userDetails);

    NoticeResponse.Detail findById(Long id);

    NoticeResponse.Update update(NoticeRequest.Update update, Long id, UserDetails userDetails);

    void remove(Long id, UserDetails userDetails);
}
