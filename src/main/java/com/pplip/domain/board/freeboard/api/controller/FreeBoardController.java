package com.pplip.domain.board.freeboard.api.controller;

import com.pplip.domain.board.freeboard.api.request.FreeBoardRequest;
import com.pplip.domain.board.freeboard.api.response.FreeBoardResponse;
import com.pplip.domain.board.freeboard.persistence.entity.FreeBoardSort;
import com.pplip.domain.board.freeboard.usecase.FreeBoardService;
import com.pplip.domain.board.freeboard.usecase.UserLikeBoardService;
import com.pplip.global.api.code.SuccessCode;
import com.pplip.global.api.response.CommonResponse;
import com.pplip.global.docs.FreeBoardDocsController;
import com.pplip.global.page.Page;
import com.pplip.global.page.PageRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

/**
 * 자유게시판 관련 API 요청을 처리하는 컨트롤러
 */
@RestController
@RequestMapping("/freeboard")
@RequiredArgsConstructor
@Slf4j
public class FreeBoardController implements FreeBoardDocsController {

    private final FreeBoardService freeBoardService;
    private final UserLikeBoardService userLikeBoardService;

    /**
     * 자유게시판 목록을 조회합니다.
     *
     * @param pageRequest 페이지 요청 정보
     * @return 자유게시판 목록과 함께 성공 응답을 반환
     */
    @Override
    @GetMapping
    public CommonResponse<Page<FreeBoardResponse.BoardList>> retrieveFreeBoard(@ModelAttribute PageRequest pageRequest,
                                                                               @RequestParam("sort") FreeBoardSort sort) {
        return CommonResponse.success(SuccessCode.SUCCESS, freeBoardService.retrieve(pageRequest, sort));
    }

    /**
     * 로그인된 사용자가 작성한 자유게시판 목록을 페이징하여 조회합니다.
     *
     * @param pageRequest 페이징 요청 정보
     * @param sort 정렬 정보
     * @return 페이징된 자유게시판 목록
     */
    @Override
    @GetMapping("/my-post")
    public CommonResponse<Page<FreeBoardResponse.BoardList>> retrieveMyFreeBoard(@ModelAttribute PageRequest pageRequest,
                                                                                 @RequestParam("sort") FreeBoardSort sort,
                                                                                 @AuthenticationPrincipal UserDetails userDetails) {
        return CommonResponse.success(SuccessCode.SUCCESS, freeBoardService.retrieveMyPosts(userDetails, pageRequest, sort));
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
        return CommonResponse.success(SuccessCode.SUCCESS, freeBoardService.retrieveDetail(id));
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
        log.info("call request");
        return CommonResponse.success(SuccessCode.SUCCESS, freeBoardService.post(request, principal));
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
    public CommonResponse<FreeBoardResponse.Detail> updateFreeBoardUpdate(
            @RequestBody
            FreeBoardRequest.BoardUpdate update,
            @PathVariable
            Long id,
            @AuthenticationPrincipal
            UserDetails principal) {
        log.info("update={}", update);
        return CommonResponse.success(SuccessCode.SUCCESS, freeBoardService.modifyFreeBoard(update,id, principal));
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
        log.info("id={}", id);
        return CommonResponse.success(SuccessCode.SUCCESS, freeBoardService.remove(id, principal));
    }

    @Override
    @GetMapping("/{id}/like")
    public CommonResponse<FreeBoardResponse.BoardLike> getLikeFreeBoard(@PathVariable Long id,
                                                                       @AuthenticationPrincipal UserDetails userDetails) {

        return CommonResponse.success(SuccessCode.SUCCESS, userLikeBoardService.getLikeFreeBoard(id, userDetails));
    }

    @Override
    @PostMapping("/{id}/like")
    public CommonResponse<FreeBoardResponse.BoardLike> likeFreeBoard(@PathVariable Long id,
                                                                     @AuthenticationPrincipal UserDetails userDetails) {
        return CommonResponse.success(SuccessCode.CREATED, userLikeBoardService.likeFreeBoard(id, userDetails));
    }

    @Override
    @DeleteMapping("/{id}/like")
    public CommonResponse<FreeBoardResponse.BoardLike> unlikeFreeBoard(@PathVariable Long id,
                                                                       @AuthenticationPrincipal UserDetails userDetails) {
        return CommonResponse.success(SuccessCode.REMOVED, userLikeBoardService.unlikeFreeBoard(id, userDetails));
    }
}
