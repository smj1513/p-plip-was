package com.pplip.domain.board.notice.api.controller;

import com.pplip.domain.board.notice.api.request.NoticeCommentRequest;
import com.pplip.domain.board.notice.api.response.NoticeCommentResponse;
import com.pplip.domain.board.notice.usecase.NoticeCommentService;
import com.pplip.global.api.code.SuccessCode;
import com.pplip.global.api.response.CommonResponse;
import com.pplip.global.docs.NoticeCommentDocsController;
import com.pplip.global.page.Page;
import com.pplip.global.page.PageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notice")
public class NoticeCommentController implements NoticeCommentDocsController {

    private final NoticeCommentService service;

    /**
     * 공지게시판의 댓글 리스트를 조회합니다.
     *
     * @param id 공지게시판의 아이디
     * @return 공지게시판의 댓글 리스트
     */
    @Override
    @GetMapping("/{id}/comment")
    public CommonResponse<Page<NoticeCommentResponse.Summary>> listNoticeBoardComment(@PathVariable Long id,
                                                                                      @ModelAttribute PageRequest pageRequest) {
        return CommonResponse.success(SuccessCode.SUCCESS, service.findAllByBoardId(id, pageRequest));
    }

    /**
     * 공지게시판에 댓글을 작성합니다.
     *
     * @param request 작성될 댓글 정보
     * @param id 공지게시판의 아이디
     * @param userDetails 댓글 작성자 유저 정보
     * @return 공지게시판의 댓글 리스트
     */
    @Override
    @PostMapping("/{id}/comment")
    public CommonResponse<NoticeCommentResponse.Detail> postNoticeBoardComment(
            @RequestBody NoticeCommentRequest.Post request,
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {

        return CommonResponse.success(SuccessCode.CREATED, service.post(request, id, userDetails));
    }


    /**
     * 공지게시판의 댓글을 수정합니다.
     *
     * @param update 수정할 댓글 정보
     * @param id 수정될 댓글 아이디
     * @return 수정된 댓글 정보
     */
    @Override
    @PutMapping("/comment/{id}")
    public CommonResponse<NoticeCommentResponse.Update> updateNoticeBoardComment(@RequestBody NoticeCommentRequest.Update update,
                                                                                 @PathVariable Long id,
                                                                                 @AuthenticationPrincipal UserDetails userDetails) {
        return CommonResponse.success(SuccessCode.UPDATED, service.update(update, id, userDetails));
    }

    /**
     * 공지게시판의 댓글을 삭제합니다.
     *
     * @param id 삭제될 댓글 아이디
     * @return 203 (No Content)
     */
    @Override
    @DeleteMapping("/comment/{id}")
    public CommonResponse<?> deleteNoticeBoardComment(@PathVariable Long id,
                                                      @AuthenticationPrincipal UserDetails userDetails) {
        service.delete(id, userDetails);

        return CommonResponse.success(SuccessCode.REMOVED, null);
    }

}
