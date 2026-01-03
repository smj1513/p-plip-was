package com.pplip.domain.board.freeboard.usecase.impl;

import com.pplip.domain.auth.utils.SecurityUtils;
import com.pplip.domain.board.freeboard.api.response.FreeBoardResponse;
import com.pplip.domain.board.freeboard.persistence.dao.FreeBoardDao;
import com.pplip.domain.board.freeboard.persistence.dao.UserLikeBoardDao;
import com.pplip.domain.board.freeboard.usecase.UserLikeBoardService;
import com.pplip.global.api.code.ErrorCode;
import com.pplip.global.exception.BusinessLogicException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserLikeBoardServiceImpl implements UserLikeBoardService {

	private final UserLikeBoardDao userLikeBoardDao;
	private final FreeBoardDao freeBoardDao;

	@Override
	public FreeBoardResponse.BoardLike getLikeFreeBoard(Long id, UserDetails userDetails) {
		Long userId = SecurityUtils.resolveUserId(userDetails);

		validBoard(id);

		boolean isLiked = userLikeBoardDao.findByBoardIdUserId(id, userId);
		return FreeBoardResponse.BoardLike.builder().isLike(isLiked).build();
	}


	@Override
	public FreeBoardResponse.BoardLike likeFreeBoard(Long id, UserDetails userDetails) {
		Long userId = SecurityUtils.resolveUserId(userDetails);

		validBoard(id);

		if (userLikeBoardDao.findByBoardIdUserId(id, userId)) {
			throw new BusinessLogicException(ErrorCode.EXISTS_BOARD_LIKE, "이미 좋아요한 게시판입니다.");
		}

		if (userLikeBoardDao.insert(id, userId) == 0) {
			throw new BusinessLogicException(ErrorCode.FAIL_TO_CREATE_LIKE, "게시글 좋아요에 실패했습니다.");
		}
		return FreeBoardResponse.BoardLike.builder().isLike(true).build();
	}

	@Override
	public FreeBoardResponse.BoardLike unlikeFreeBoard(Long id, UserDetails userDetails) {
		Long userId = SecurityUtils.resolveUserId(userDetails);
		validBoard(id);

		if (userLikeBoardDao.delete(id, userId) == 0) {
			throw new BusinessLogicException(ErrorCode.FAIL_TO_DELETE_LIKE, "좋아요 삭제에 실패했습니다.");
		}
		return FreeBoardResponse.BoardLike.builder().isLike(false).build();
	}

	private void validBoard(Long id) {
		if (!freeBoardDao.findById(id).isPresent()) {
			throw new BusinessLogicException(ErrorCode.BOARD_NOT_FOUND_ERROR, "게시판을 찾을 수 없습니다.(좋아요)");
		}
	}
}
