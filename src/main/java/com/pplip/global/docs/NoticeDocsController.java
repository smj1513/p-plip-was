package com.pplip.global.docs;

import com.pplip.domain.board.notice.api.request.NoticeRequest;
import com.pplip.domain.board.notice.api.response.NoticeResponse;
import com.pplip.global.api.response.CommonResponse;
import com.pplip.global.page.Page;
import com.pplip.global.page.PageRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 공지게시판 관련 API 명세를 정의하는 인터페이스
 */
@Tag(name = "공지게시판 API", description = "공지게시판 API")
public interface NoticeDocsController {

    /**
     * 공지게시판의 게시글들을 불러옵니다.
     *
     * @param pageRequest 공지 게시글 페이징 정보.
     * @return 페이징된 공지게시글 요약 정보.
     */
    @Operation(summary = "공지게시판 페이징 조회")
    @ApiResponse(responseCode = "200", description = "성공")
    CommonResponse<Page<NoticeResponse.Summary>> listNoticeBoard(PageRequest pageRequest);

    /**
     * 특정 사용자가 작성한 공지게시판의 게시글들을 불러옵니다.
     *
     * @param userDetails 인증된 사용자
     * @param pageRequest 공지 게시글 페이징 정보.
     * @return 페이징된 공지게시글 요약 정보.
     */
    @Operation(summary = "특정 사용자가 작성한 공지게시판 페이징 조회")
    @ApiResponse(responseCode = "200", description = "성공")
    CommonResponse<Page<NoticeResponse.Summary>> listMyNoticeBoard(UserDetails userDetails, PageRequest pageRequest);


    /**
     * 공지게시판의 게시글을 작성합니다.
     *
     * @param request 작성될 공지게시글 정보.
     * @param userDetails 공지 게시글 작성자 정보
     * @return 작성된 공지게시글 상세 정보.
     */
    @Operation(summary = "공지게시판 게시글 작성")
    @ApiResponse(responseCode = "201", description = "생성")
    CommonResponse<NoticeResponse.Detail> postNoticeBoard(NoticeRequest.Post request, UserDetails userDetails);

    /**
     * 공지게시판의 게시글을 조회합니다.
     *
     * @param id 공지게시판의 게시글 아이디
     * @return 공지게시글 상세 정보.
     */
    @Operation(summary = "공지게시판 게시글 상세 조회")
    @ApiResponse(responseCode = "200", description = "성공")
    CommonResponse<NoticeResponse.Detail> findNoticeBoard(Long id);

    /**
     * 공지게시판의 게시글을 수정합니다.
     *
     * @param update 수정할 공지게시판의 게시글 정보
     * @param id 공지게시판의 게시글 아이디
     * @return 수정된 공지게시글 상세 정보.
     */
    @Operation(summary = "공지게시판 게시판 수정")
    @ApiResponse(responseCode = "202", description = "수정")
    CommonResponse<NoticeResponse.Update> updateNoticeBoard(NoticeRequest.Update update, Long id, UserDetails userDetails);

    /**
     * 공지게시판의 게시글을 삭제합니다..
     *
     * @param id 공지게시판의 게시글 아이디
     * @return 203 ( 삭제 ).
     */
    @Operation(summary = "공지게시판 게시판 삭제")
    @ApiResponse(responseCode = "203", description = "삭제")
    CommonResponse<Void> removeNoticeBoard(Long id, UserDetails userDetails);

}
