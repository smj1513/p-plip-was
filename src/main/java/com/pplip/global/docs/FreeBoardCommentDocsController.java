package com.pplip.global.docs;

import com.pplip.domain.board.freeboard.api.request.FreeBoardCommentRequest;
import com.pplip.domain.board.freeboard.api.response.FreeBoardCommentResponse;
import com.pplip.global.api.response.CommonResponse;
import com.pplip.global.page.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 자유게시판 댓글 관련 API 명세를 정의하는 인터페이스
 */
@Tag(name = "자유게시판 댓글 API", description = "자유게시판 댓글 api")
public interface FreeBoardCommentDocsController {

    /**
     * 특정 게시글의 댓글 목록을 조회합니다.
     *
     * @param boardId 게시글 ID
     * @return 댓글 목록
     */
    @Operation(summary = "자유게시판 댓글 목록 조회")
    @ApiResponse(responseCode = "200", description = "성공")
    CommonResponse<Page<FreeBoardCommentResponse.Retrieve>> getComments(Long boardId);

    /**
     * 특정 게시글에 새로운 댓글을 작성합니다.
     *
     * @param boardId   게시글 ID
     * @param comment   댓글 생성 요청 정보
     * @param loginUser 현재 로그인한 사용자 정보
     * @return 생성된 댓글 정보
     */
    @Operation(summary = "자유게시판 댓글 작성")
    @ApiResponse(responseCode = "201", description = "생성")
    CommonResponse<FreeBoardCommentResponse.Create> postComment(Long boardId,
                                                                 FreeBoardCommentRequest.Create comment,
                                                                 @AuthenticationPrincipal UserDetails loginUser);

    /**
     * 특정 댓글을 삭제합니다.
     *
     * @param commentId 댓글 ID
     * @param loginUser 현재 로그인한 사용자 정보
     * @return
     */
    @Operation(summary = "자유게시판 댓글 삭제")
    @ApiResponse(responseCode = "203", description = "삭제")
    CommonResponse<Void> deleteComment(Long commentId, @AuthenticationPrincipal UserDetails loginUser);

    /**
     * 특정 댓글을 수정합니다.
     *
     * @param commentId 댓글 ID
     * @param comment   댓글 수정 요청 정보
     * @param loginUser 현재 로그인한 사용자 정보
     * @return 수정된 댓글 정보
     */
    @Operation(summary = "자유게시판 댓글 수정")
    @ApiResponse(responseCode = "202", description = "수정")
    CommonResponse<FreeBoardCommentResponse.Update> updateComment(Long commentId,
                                                                   FreeBoardCommentRequest.Update comment,
                                                                   @AuthenticationPrincipal UserDetails loginUser);
}
