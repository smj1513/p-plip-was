package com.pplip.domain.board.freeboard.api.controller;

import com.pplip.domain.board.freeboard.api.request.FreeBoardRequest;
import com.pplip.domain.board.freeboard.api.response.FreeBoardResponse;
import com.pplip.global.api.code.SuccessCode;
import com.pplip.global.api.response.CommonResponse;
import com.pplip.global.docs.FreeBoardDocsController;
import com.pplip.global.page.Page;
import com.pplip.global.page.PageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

/**
 * 자유게시판 관련 API 요청을 처리하는 컨트롤러
 */
@RestController
@RequestMapping("/freeboard")
@RequiredArgsConstructor
public class FreeBoardController implements FreeBoardDocsController {

    /**
     * 자유게시판 목록을 조회합니다.
     *
     * @param pageRequest 페이지 요청 정보
     * @return 자유게시판 목록과 함께 성공 응답을 반환
     */
    @Override
    @GetMapping
    public CommonResponse<Page<FreeBoardResponse.BoardList>> retrieveFreeBoard(@ModelAttribute PageRequest pageRequest) {
        return CommonResponse.success(SuccessCode.SUCCESS, null);
    }

    /**
     * 자유게시판 게시글 상세 정보를 조회합니다.
     *
     * @param id 게시글 ID
     * @return 게시글 상세 정보와 함께 성공 응답을 반환
     */
    @Override
    @GetMapping("/{id}")
    public CommonResponse<FreeBoardResponse.Detail> detailFreeBoardDetail(@PathVariable Long id) {
        return CommonResponse.success(SuccessCode.SUCCESS, null);
    }

    /**
     * 자유게시판에 새 게시글을 등록합니다.
     *
     * @param request   게시글 등록 요청 정보
     * @param principal 현재 인증된 사용자 정보
     * @return 등록된 게시글 상세 정보와 함께 성공 응답을 반환
     */
    @Override
    @PostMapping
    public CommonResponse<FreeBoardResponse.Detail> postFreeBoardDetail(
            @RequestBody
            FreeBoardRequest.BoardPost request,
            @AuthenticationPrincipal
            UserDetails principal) {
        return CommonResponse.success(SuccessCode.SUCCESS, null);
    }

    /**
     * 자유게시판 게시글을 수정합니다.
     *
     * @param update    게시글 수정 요청 정보
     * @param id        게시글 ID
     * @param principal 현재 인증된 사용자 정보
     * @return 수정된 게시글 정보와 함께 성공 응답을 반환
     */
    @Override
    @PutMapping("/{id}")
    public CommonResponse<FreeBoardResponse.Update> updateFreeBoardUpdate(
            @RequestBody
            FreeBoardRequest.BoardUpdate update,
            @PathVariable
            Long id,
            @AuthenticationPrincipal
            UserDetails principal) {
        return CommonResponse.success(SuccessCode.SUCCESS, null);
    }

    /**
     * 자유게시판 게시글을 삭제합니다.
     *
     * @param id        게시글 ID
     * @param principal 현재 인증된 사용자 정보
     * @return 삭제된 게시글 정보와 함께 성공 응답을 반환
     */
    @Override
    @DeleteMapping("/{id}")
    public CommonResponse<FreeBoardResponse.Remove> removeFreeBoard(
            @PathVariable
            Long id,
            @AuthenticationPrincipal
            UserDetails principal) {
        return CommonResponse.success(SuccessCode.SUCCESS, null);
    }
}
