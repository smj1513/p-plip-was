package com.pplip.global.docs;

import com.pplip.domain.board.freeboard.api.request.FreeBoardRequest;
import com.pplip.domain.board.freeboard.api.response.FreeBoardResponse;
import com.pplip.global.api.response.CommonResponse;
import com.pplip.global.page.Page;
import com.pplip.global.page.PageRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * 자유게시판 관련 API 명세를 정의하는 인터페이스
 */
@Tag(name = "자유게시판 API", description = "자유게시판 api")
public interface FreeBoardDocsController {


    /**
     * 자유게시판 목록을 페이징하여 조회합니다.
     *
     * @param pageRequest 페이징 요청 정보
     * @return 페이징된 자유게시판 목록
     */
    @Operation(summary = "자유게시판 페이징 조회")
    @ApiResponse(responseCode = "200", description = "성공")
    public CommonResponse<Page<FreeBoardResponse.BoardList>> retrieveFreeBoard(@ModelAttribute PageRequest pageRequest);


    /**
     * 특정 자유게시판 게시글의 상세 정보를 조회합니다.
     *
     * @param id 조회할 게시글의 ID
     * @return 게시글 상세 정보
     */
    @Operation(summary = "자유게시판 게시글 상세 조회")
    @ApiResponse(responseCode = "200", description = "성공")
    CommonResponse<FreeBoardResponse.Detail> detailFreeBoardDetail(Long id);


    /**
     * 새로운 자유게시판 게시글을 작성합니다.
     *
     * @param request   게시글 작성 요청 데이터
     * @param principal 인증된 사용자 정보
     * @return 생성된 게시글의 상세 정보
     */
    @Operation(summary = "자유게시판 게시글 작성")
    @ApiResponse(responseCode = "201", description = "생성")
    CommonResponse<FreeBoardResponse.Detail> postFreeBoardDetail(FreeBoardRequest.BoardPost request, @AuthenticationPrincipal UserDetails principal);

    /**
     * 기존 자유게시판 게시글을 수정합니다.
     *
     * @param update    게시글 수정 요청 데이터
     * @param id        수정할 게시글의 ID
     * @param principal 인증된 사용자 정보
     * @return 수정된 게시글 정보
     */
    @Operation(summary = "자유게시판 게시글 수정")
    @ApiResponse(responseCode = "202", description = "수정")
    CommonResponse<FreeBoardResponse.Update> updateFreeBoardUpdate(FreeBoardRequest.BoardUpdate update, Long id, @AuthenticationPrincipal UserDetails principal);


    /**
     * 특정 자유게시판 게시글을 삭제합니다.
     *
     * @param id        삭제할 게시글의 ID
     * @param principal 인증된 사용자 정보
     * @return 삭제된 게시글 정보
     */
    @Operation(summary = "자유게시판 게시글 삭제")
    @ApiResponse(responseCode = "203", description = "삭제")
    CommonResponse<FreeBoardResponse.Remove> removeFreeBoard(Long id, @AuthenticationPrincipal UserDetails principal);

}
