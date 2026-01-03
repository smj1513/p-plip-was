package com.pplip.domain.board.freeboard.usecase.impl;

import com.pplip.domain.auth.persistence.entity.Account;
import com.pplip.domain.auth.utils.SecurityUtils;
import com.pplip.domain.board.freeboard.api.request.FreeBoardCommentRequest;
import com.pplip.domain.board.freeboard.api.response.FreeBoardCommentResponse;
import com.pplip.domain.board.freeboard.persistence.dao.FreeBoardCommentDao;
import com.pplip.domain.board.freeboard.persistence.dao.FreeBoardDao;
import com.pplip.domain.board.freeboard.persistence.entity.FreeComment;
import com.pplip.domain.board.freeboard.usecase.FreeBoardCommentService;
import com.pplip.global.api.code.ErrorCode;
import com.pplip.global.exception.BusinessLogicException;
import com.pplip.global.page.Page;
import com.pplip.global.page.PageRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class FreeBoardCommentServiceImpl implements FreeBoardCommentService {
	private final FreeBoardCommentDao freeBoardCommentDao;

	@Override
	@Transactional(readOnly = true)
	public Page<FreeBoardCommentResponse.Retrieve> getComment(Long boardId, PageRequest pageRequest) {
		List<FreeBoardCommentResponse.Retrieve> comments = freeBoardCommentDao.findAllByBoardId(boardId, pageRequest);
		if (!SecurityUtils.isAnonymous()) {
			Long userId = SecurityUtils.getCurrentUser().getUserId();
			comments.forEach(data -> {
				data.setAuthor(data.getUserId() == userId);
			});
		}
		int allCnt = freeBoardCommentDao.countAllByBoardId(boardId);
		return new Page<>(comments, pageRequest.getPageNum(), pageRequest.getPageSize(), allCnt);
	}

	@Override
	public FreeBoardCommentResponse.Detail postComment(Long boardId, FreeBoardCommentRequest.Create comment, UserDetails loginUser) {
		Long userId = ((Account) loginUser).getUserId();
		FreeComment freeComment = FreeComment.builder()
				.boardId(boardId)
				.authorId(userId)
				.content(comment.getContent())
				.isRemoved(false)
				.createdAt(LocalDateTime.now())
				.build();
		freeBoardCommentDao.insert(freeComment);
		return freeBoardCommentDao.findByIdToDto(freeComment.getId()).get();
	}

	@Override
	public Void deleteComment(Long commentId, UserDetails loginUser) {
		Long loginUserId = ((Account) loginUser).getUserId();
		FreeBoardCommentResponse.Detail detail = freeBoardCommentDao.findByIdToDto(commentId).orElseThrow(()->new BusinessLogicException(ErrorCode.COMMENT_NOT_FOUND, "댓글을 찾을 수 없습니다."));
		Long userId = detail.getUserId();
		if(!userId.equals(loginUserId)){
			throw new BusinessLogicException(ErrorCode.FORBIDDEN,"작성자만 삭제할 수 있습니다.");
		}
		freeBoardCommentDao.delete(detail.getId());
		return null;
	}

	@Override
	public FreeBoardCommentResponse.Detail updateComment(Long commentId, FreeBoardCommentRequest.Update updateInfo, UserDetails loginUser) {
		Long loginUserId = ((Account) loginUser).getUserId();
		FreeBoardCommentResponse.Detail detail = freeBoardCommentDao.findByIdToDto(commentId).orElseThrow(()->new BusinessLogicException(ErrorCode.COMMENT_NOT_FOUND, "댓글을 찾을 수 없습니다."));
		Long userId = detail.getUserId();
		if(!userId.equals(loginUserId)){
			throw new BusinessLogicException(ErrorCode.FORBIDDEN,"작성자만 수정할 수 있습니다.");
		}
		FreeComment comment = freeBoardCommentDao.findById(commentId).orElseThrow(()->new BusinessLogicException(ErrorCode.COMMENT_NOT_FOUND, "댓글을 찾을 수 없습니다."));
		comment.setContent(updateInfo.getContent());
		comment.setUpdatedAt(LocalDateTime.now());
		freeBoardCommentDao.update(comment);
		FreeBoardCommentResponse.Detail resData = freeBoardCommentDao.findByIdToDto(commentId).get();
		resData.setAuthor(resData.getUserId() == loginUserId);

		return resData;
	}
}
