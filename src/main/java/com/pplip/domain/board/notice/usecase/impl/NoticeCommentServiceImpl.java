package com.pplip.domain.board.notice.usecase.impl;

import com.pplip.domain.auth.persistence.entity.Account;
import com.pplip.domain.auth.utils.SecurityUtils;
import com.pplip.domain.board.notice.api.request.NoticeCommentRequest;
import com.pplip.domain.board.notice.api.response.NoticeCommentResponse;
import com.pplip.domain.board.notice.persistence.dao.NoticeCommentDao;
import com.pplip.domain.board.notice.persistence.entity.NoticeComment;
import com.pplip.domain.board.notice.usecase.NoticeCommentService;
import com.pplip.domain.board.notice.usecase.model.NoticeCommentModel;
import com.pplip.domain.board.notice.utils.NoticeCommentParser;
import com.pplip.global.api.code.ErrorCode;
import com.pplip.global.exception.BoardLogicException;
import com.pplip.global.page.Page;
import com.pplip.global.page.PageRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.core.Local;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class NoticeCommentServiceImpl implements NoticeCommentService {

    private final NoticeCommentDao dao;
    private final NoticeCommentParser parser;
    @Override
    public Page<NoticeCommentResponse.Summary> findAllByBoardId(Long boardId, PageRequest pageRequest) {
        List<NoticeCommentResponse.Summary> datas = dao.findAll(boardId, pageRequest);

        if (!SecurityUtils.isAnonymous()) {
            Long userId = SecurityUtils.getCurrentUser().getUserId();
            datas.forEach(data -> data.setAuthor(data.getAuthorId() == userId));
        }

        return new Page<>(dao.findAll(boardId, pageRequest),
                pageRequest.getPageNum(),
                pageRequest.getPageSize(),
                dao.noticeBoardCommentAllCount(boardId));
    }

    @Override
    public NoticeCommentResponse.Detail post(NoticeCommentRequest.Post post, Long boardId, UserDetails userDetails) {
        Long userId = SecurityUtils.resolveUserId(userDetails);
        NoticeCommentModel model = new NoticeCommentModel(post, boardId, userId);
        NoticeComment entity = model.toEntity();

        if (dao.insert(entity) != 1) {
            throw new BoardLogicException(ErrorCode.COMMENT_CREATE_ERROR);
        }
        NoticeCommentResponse.Detail resData = dao.findById(entity.getId())
                .orElseThrow(() -> new BoardLogicException(ErrorCode.COMMENT_NOT_FOUND));

        resData.setAuthor(resData.getAuthorId() == userId);
        return resData;
    }

    @Override
    public NoticeCommentResponse.Update update(NoticeCommentRequest.Update update,
                                               Long id,
                                               UserDetails userDetails) {
        Long userId = SecurityUtils.resolveUserId(userDetails);

        NoticeComment entity = dao.findByIdToEntity(id)
                .orElseThrow(() -> new BoardLogicException(ErrorCode.COMMENT_NOT_FOUND, "댓글을 찾을 수 없습니다."));

        if (!entity.getAuthorId().equals(userId)) {
            throw new BoardLogicException(ErrorCode.FORBIDDEN, "작성자만 수정 가능합니다.");
        }

        entity.setUpdatedAt(LocalDateTime.now());
        entity.updateContent(update.getContent());
        dao.update(entity);

        NoticeCommentResponse.Detail detail = dao.findById(id)
                .orElseThrow(() -> new BoardLogicException(ErrorCode.COMMENT_NOT_FOUND));

        detail.setAuthor(detail.getAuthorId() == userId);

        return parser.detailResToUpdateRes(detail);
    }

    @Override
    public void delete(Long id, UserDetails userDetails) {
        Long userId = SecurityUtils.resolveUserId(userDetails);
        NoticeComment entity = dao.findByIdToEntity(id)
                .orElseThrow(() -> new BoardLogicException(ErrorCode.COMMENT_NOT_FOUND, "댓글을 찾지 못하였습니다."));

        if (!entity.getAuthorId().equals(userId)) {
            throw new BoardLogicException(ErrorCode.FORBIDDEN, "작성자만 삭제할 수 있습니다.");
        }

        if (dao.delete(id) != 1) {
            throw new BoardLogicException(ErrorCode.COMMENT_DELETE_FAIL_ERROR, "댓글 삭제에 실패했습니다.");
        }
    }
}
