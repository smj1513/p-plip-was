package com.pplip.domain.board.notice.utils;

import com.pplip.domain.board.notice.api.response.NoticeResponse;
import org.springframework.stereotype.Component;

@Component
public class NoticeBoardParser {
    public NoticeResponse.Update detailResToUpdateRes(NoticeResponse.Detail detail) {
        return NoticeResponse.Update.builder()
                .id(detail.getId())
                .authorName(detail.getAuthorName())
                .noticeImages(detail.getNoticeImages())
                .title(detail.getTitle())
                .viewCnt(detail.getViewCnt())
                .createdAt(detail.getCreatedAt())
                .updatedAt(detail.getUpdatedAt())
                .content(detail.getContent())
                .isAuthor(detail.isAuthor())
                .build();
    }
}
