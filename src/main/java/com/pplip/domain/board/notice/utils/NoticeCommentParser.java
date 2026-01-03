package com.pplip.domain.board.notice.utils;

import com.pplip.domain.board.notice.api.response.NoticeCommentResponse;
import org.springframework.stereotype.Component;


@Component
public class NoticeCommentParser {

    public NoticeCommentResponse.Update detailResToUpdateRes(NoticeCommentResponse.Detail detail) {
        return NoticeCommentResponse.Update.builder()
                .authorNickName(detail.getAuthorNickName())
                .profileImage(detail.getProfileImage())
                .createdAt(detail.getCreatedAt())
                .updatedAt(detail.getUpdatedAt())
                .content(detail.getContent())
                .isAuthor(detail.isAuthor())
                .build();
    }
}
