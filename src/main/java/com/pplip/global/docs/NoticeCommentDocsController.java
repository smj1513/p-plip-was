package com.pplip.global.docs;

import com.pplip.domain.board.notice.api.request.NoticeCommentRequest;
import com.pplip.domain.board.notice.api.response.NoticeCommentResponse;
import com.pplip.global.api.response.CommonResponse;
import com.pplip.global.page.Page;
import com.pplip.global.page.PageRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 공지게시판 댓글 관련 API 명세를 정의하는 인터페이스
 */
@Tag(name = "공지게시판 댓글 API", description = "공지게시판 댓글 API")
public interface NoticeCommentDocsController {

    /**
     * 공지게시판의 댓글 리스트를 조회합니다.
     *
     * @param id 공지게시판의 아이디
     * @param pageRequest 페이지 쿼리파라미터
     * @return 공지게시판의 댓글 리스트
     */
    @Operation(summary = "공지게시판 게시판 댓글 페이징 조회")
    @ApiResponse(responseCode = "200", description = "성공")
    CommonResponse<Page<NoticeCommentResponse.Summary>> listNoticeBoardComment(Long id, PageRequest pageRequest);

    /**
     * 공지게시판에 댓글을 작성합니다.
     *
     * @param request 작성될 댓글 정보
     * @param id 공지게시판의 아이디
     * @param userDetails 댓글 작성자 유저 정보
     * @return 공지게시판의 댓글 리스트
     */
    @Operation(summary = "공지게시판 게시판 댓글 작성")
    @ApiResponse(responseCode = "201", description = "성공")
    CommonResponse<NoticeCommentResponse.Detail> postNoticeBoardComment(NoticeCommentRequest.Post request,
                                                                      Long id,
                                                                      UserDetails userDetails);

    /**
     * 공지게시판의 댓글을 수정합니다.
     *
     * @param update 수정할 댓글 정보
     * @param id 수정될 댓글 아이디
     * @param userDetails 댓글 수정자 유저 정보
     * @return 수정된 댓글 정보
     */
    @Operation(summary = "공지게시판 게시판 댓글 수정")
    @ApiResponse(responseCode = "202", description = "수정")
    CommonResponse<NoticeCommentResponse.Update> updateNoticeBoardComment(NoticeCommentRequest.Update update,
                                                                          Long id,
                                                                          UserDetails userDetails);

    /**
     * 공지게시판의 댓글을 삭제합니다.
     *
     * @param id 삭제될 댓글 아이디
     * @return 203
     */
    @Operation(summary = "공지게시판 게시판 댓글 삭제")
    @ApiResponse(responseCode = "203", description = "삭제")
    CommonResponse<?> deleteNoticeBoardComment(Long id, UserDetails userDetails);
}
