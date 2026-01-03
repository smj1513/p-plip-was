package com.pplip.domain.board.freeboard.usecase;

import com.pplip.domain.board.freeboard.api.request.FreeBoardRequest;
import com.pplip.domain.board.freeboard.api.response.FreeBoardResponse;
import com.pplip.domain.board.freeboard.persistence.entity.FreeBoardSort;
import com.pplip.global.page.Page;
import com.pplip.global.page.PageRequest;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 자유게시판 관련 비즈니스 로직을 처리하는 서비스 인터페이스
 */
public interface FreeBoardService {

	/**
	 * 특정 자유게시판 게시글을 삭제합니다.
	 *
	 * @param id 삭제할 게시글의 ID
	 * @param principal 인증된 사용자 정보
	 * @return 삭제된 게시글 정보
	 */
	FreeBoardResponse.Remove remove(Long id, UserDetails principal);

	/**
	 * 기존 자유게시판 게시글을 수정합니다.
	 *
	 * @param update    게시글 수정 요청 데이터
	 * @param id        수정할 게시글의 ID
	 * @param principal 인증된 사용자 정보
	 * @return 수정된 게시글 정보
	 */
	FreeBoardResponse.Detail modifyFreeBoard(FreeBoardRequest.BoardUpdate update, Long id, UserDetails principal);


	/**
	 * 새로운 자유게시판 게시글을 작성합니다.
	 *
	 * @param request   게시글 작성 요청 데이터
	 * @param principal 인증된 사용자 정보
	 * @return 생성된 게시글의 상세 정보
	 */
	FreeBoardResponse.Detail post(FreeBoardRequest.BoardPost request, UserDetails principal);


	/**
	 * 특정 자유게시판 게시글의 상세 정보를 조회합니다.
	 *
	 * @param id 조회할 게시글의 ID
	 * @return 게시글 상세 정보
	 */
	FreeBoardResponse.Detail retrieveDetail(Long id);


	/**
	 * 자유게시판 목록을 페이징하여 조회합니다.
	 *
	 * @param pageRequest 페이징 요청 정보
	 * @return 페이징된 자유게시판 목록
	 */
	Page<FreeBoardResponse.BoardList> retrieve(PageRequest pageRequest, FreeBoardSort sort);

	/**
	 * 로그된 사용자가 작성한 자유게시판 목록을 페이징하여 조회합니다.
	 *
	 * @param pageRequest 페이징 요청 정보
	 * @param userDetails 인증된 사용자 정보
	 * @return 페이징된 자유게시판 목록
	 */
	Page<FreeBoardResponse.BoardList> retrieveMyPosts(UserDetails userDetails, PageRequest pageRequest, FreeBoardSort sort);

}
