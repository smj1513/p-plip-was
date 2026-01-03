package com.pplip.domain.board.freeboard.usecase;

import com.pplip.domain.board.freeboard.api.request.FreeBoardCommentRequest;
import com.pplip.domain.board.freeboard.api.response.FreeBoardCommentResponse;
import com.pplip.global.page.Page;
import com.pplip.global.page.PageRequest;
import org.springframework.security.core.userdetails.UserDetails;

public interface FreeBoardCommentService {
	/**
	 * 특정 게시글의 댓글 목록을 페이징하여 가져옵니다.
	 *
	 * @param boardId 게시글 ID
	 * @param pageRequest 페이징 정보
	 * @return 페이징된 댓글 목록
	 */
	Page<FreeBoardCommentResponse.Retrieve> getComment(Long boardId, PageRequest pageRequest);

	/**
	 * 새로운 댓글을 작성합니다.
	 *
	 * @param boardId 게시글 ID
	 * @param comment 작성할 댓글 정보
	 * @param loginUser 로그인한 사용자 정보
	 * @return 작성된 댓글 상세 정보
	 */
	FreeBoardCommentResponse.Detail postComment(Long boardId, FreeBoardCommentRequest.Create comment, UserDetails loginUser);

	/**
	 * 특정 댓글을 삭제합니다.
	 *
	 * @param commentId 삭제할 댓글 ID
	 * @param loginUser 로그인한 사용자 정보
	 * @return Void
	 */
	Void deleteComment(Long commentId, UserDetails loginUser);

	/**
	 * 특정 댓글을 수정합니다.
	 *
	 * @param commentId 수정할 댓글 ID
	 * @param comment 수정할 댓글 정보
	 * @param loginUser 로그인한 사용자 정보
	 * @return 수정된 댓글 상세 정보
	 */
	FreeBoardCommentResponse.Detail updateComment(Long commentId, FreeBoardCommentRequest.Update comment, UserDetails loginUser);
}
