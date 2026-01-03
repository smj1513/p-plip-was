package com.pplip.domain.board.freeboard.api.controller;

import com.pplip.domain.auth.persistence.entity.Account;
import com.pplip.domain.board.freeboard.api.request.FreeBoardCommentRequest;
import com.pplip.domain.board.freeboard.api.response.FreeBoardCommentResponse;
import com.pplip.domain.board.freeboard.usecase.FreeBoardCommentService;
import com.pplip.global.api.code.SuccessCode;
import com.pplip.global.api.response.CommonResponse;
import com.pplip.global.docs.FreeBoardCommentDocsController;
import com.pplip.global.page.Page;
import com.pplip.global.page.PageRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/freeboard")
@RequiredArgsConstructor
@Slf4j
public class FreeBoardCommentController implements FreeBoardCommentDocsController {

    private final FreeBoardCommentService commentService;

    /**
     * 특정 게시글의 댓글 목록을 조회합니다.
     *
     * @param boardId 게시글 ID
     * @return 댓글 목록
     */
    @GetMapping("/{id}/comment")
    @Override
    public CommonResponse<Page<FreeBoardCommentResponse.Retrieve>> getComments(@PathVariable(name = "id") Long boardId, @ModelAttribute PageRequest page) {
        return CommonResponse.success(SuccessCode.SUCCESS, commentService.getComment(boardId, page));
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
    public CommonResponse<FreeBoardCommentResponse.Detail> postComment(@PathVariable(name = "id") Long boardId,
                                                                       @RequestBody FreeBoardCommentRequest.Create comment,
                                                                       @AuthenticationPrincipal UserDetails loginUser) {
        log.info("board id ={}, comment={}, loginUser={}", boardId, comment, ((Account) loginUser).getUserId());
        return CommonResponse.success(SuccessCode.CREATED, commentService.postComment(boardId,comment,loginUser));
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
        return CommonResponse.success(SuccessCode.SUCCESS, commentService.deleteComment(commentId, loginUser));
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
    public CommonResponse<FreeBoardCommentResponse.Detail> updateComment(@PathVariable(name = "id") Long commentId,
                                                                         @RequestBody FreeBoardCommentRequest.Update comment,
                                                                         @AuthenticationPrincipal UserDetails loginUser) {
        return CommonResponse.success(SuccessCode.SUCCESS, commentService.updateComment(commentId, comment, loginUser));
    }

}
