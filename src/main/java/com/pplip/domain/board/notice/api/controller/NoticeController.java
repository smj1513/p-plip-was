package com.pplip.domain.board.notice.api.controller;

import com.pplip.domain.board.notice.api.request.NoticeRequest;
import com.pplip.domain.board.notice.api.response.NoticeResponse;
import com.pplip.global.api.code.SuccessCode;
import com.pplip.global.api.response.CommonResponse;
import com.pplip.global.docs.NoticeDocsController;
import com.pplip.global.page.Page;
import com.pplip.global.page.PageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notice")
public class NoticeController implements NoticeDocsController {

    /**
     * 공지게시판의 게시글들을 불러옵니다.
     *
     * @param pageRequest 공지 게시글 페이징 정보.
     * @return 페이징된 공지게시글 요약 정보.
     */
    @GetMapping
    public CommonResponse<Page<NoticeResponse.Summary>> listNoticeBoard(@ModelAttribute PageRequest pageRequest) {
        return CommonResponse.success(SuccessCode.SUCCESS, null);
    }

    /**
     * 공지게시판의 게시글을 작성합니다.
     *
     * @param request 작성될 공지게시글 정보.
     * @param userDetails 공지 게시글 작성자 정보
     * @return 작성된 공지게시글 상세 정보.
     */
    @PostMapping
    public CommonResponse<NoticeResponse.Detail> postNoticeBoard(NoticeRequest.Post request,
                                                                 @AuthenticationPrincipal UserDetails userDetails) {
        return CommonResponse.success(SuccessCode.CREATED, null);
    }

    /**
     * 공지게시판의 게시글을 조회합니다.
     *
     * @param id 공지게시판의 게시글 아이디
     * @return 공지게시글 상세 정보.
     */
    @GetMapping("/{id}")
    public CommonResponse<NoticeResponse.Detail> findNoticeBoard(@PathVariable Long id) {
        return CommonResponse.success(SuccessCode.SUCCESS, null);
    }

    /**
     * 공지게시판의 게시글을 수정합니다.
     *
     * @param update 수정할 공지게시판의 게시글 정보
     * @param id 공지게시판의 게시글 아이디
     * @return 수정된 공지게시글 상세 정보.
     */
    @PutMapping("/{id}")
    public CommonResponse<NoticeResponse.Update> updateNoticeBoard(NoticeRequest.Update update, @PathVariable Long id) {
        return CommonResponse.success(SuccessCode.UPDATED, null);
    }

    /**
     * 공지게시판의 게시글을 삭제합니다..
     *
     * @param id 공지게시판의 게시글 아이디
     * @return 203 ( 삭제 ).
     */
    @DeleteMapping("/{id}")
    public CommonResponse<NoticeResponse.Update> removeNoticeBoard(@PathVariable Long id) {
        return CommonResponse.success(SuccessCode.REMOVED, null);
    }
}
