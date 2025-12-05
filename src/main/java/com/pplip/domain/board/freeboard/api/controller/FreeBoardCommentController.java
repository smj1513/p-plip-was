package com.pplip.domain.board.freeboard.api.controller;

import com.pplip.domain.board.freeboard.api.request.FreeBoardCommentRequest;
import com.pplip.domain.board.freeboard.api.response.FreeBoardCommentResponse;
import com.pplip.global.api.code.SuccessCode;
import com.pplip.global.api.response.CommonResponse;
import com.pplip.global.docs.FreeBoardCommentDocsController;
import com.pplip.global.page.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/freeboard")
@RequiredArgsConstructor
public class FreeBoardCommentController implements FreeBoardCommentDocsController {
    /**
     * 특정 게시글의 댓글 목록을 조회합니다.
     *
     * @param boardId 게시글 ID
     * @return 댓글 목록
     */
    @GetMapping("/{id}/comment")
    @Override
    public CommonResponse<Page<FreeBoardCommentResponse.Retrieve>> getComments(@PathVariable(name = "id") Long boardId) {
        return CommonResponse.success(SuccessCode.SUCCESS, null);
    }

    /**
     * 특정 게시글에 새로운 댓글을 작성합니다.
     *
     * @param boardId   게시글 ID
     * @param comment   댓글 생성 요청 정보
     * @param loginUser 현재 로그인한 사용자 정보
     * @return 생성된 댓글 정보
     */
    @PostMapping("/{id}/comment")
    @Override
    public CommonResponse<FreeBoardCommentResponse.Create> postComment(@PathVariable(name = "id") Long boardId,
                                                                       @RequestBody FreeBoardCommentRequest.Create comment,
                                                                       @AuthenticationPrincipal UserDetails loginUser) {
        return CommonResponse.success(SuccessCode.CREATED, null);
    }

    /**
     * 특정 댓글을 삭제합니다.
     *
     * @param commentId 댓글 ID
     * @param loginUser 현재 로그인한 사용자 정보
     * @return
     */
    @DeleteMapping("/comment/{id}")
    @Override
    public CommonResponse<Void> deleteComment(@PathVariable(name = "id") Long commentId,
                                              @AuthenticationPrincipal UserDetails loginUser) {
        return CommonResponse.success(SuccessCode.SUCCESS, null);
    }

    /**
     * 특정 댓글을 수정합니다.
     *
     * @param commentId 댓글 ID
     * @param comment   댓글 수정 요청 정보
     * @param loginUser 현재 로그인한 사용자 정보
     * @return 수정된 댓글 정보
     */
    @PutMapping("/comment/{id}")
    @Override
    public CommonResponse<FreeBoardCommentResponse.Update> updateComment(@PathVariable(name = "id") Long commentId,
                                                                         @RequestBody FreeBoardCommentRequest.Update comment,
                                                                         @AuthenticationPrincipal UserDetails loginUser) {
        return CommonResponse.success(SuccessCode.SUCCESS, null);
    }

}
