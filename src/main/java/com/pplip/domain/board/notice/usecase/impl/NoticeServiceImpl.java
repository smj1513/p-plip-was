package com.pplip.domain.board.notice.usecase.impl;

import com.pplip.domain.auth.persistence.entity.Account;
import com.pplip.domain.auth.utils.SecurityUtils;
import com.pplip.domain.board.aop.annotation.CountView;
import com.pplip.domain.board.aop.enums.BoardType;
import com.pplip.domain.board.notice.api.request.NoticeRequest;
import com.pplip.domain.board.notice.api.response.NoticeResponse;
import com.pplip.domain.board.notice.persistence.dao.NoticeDao;
import com.pplip.domain.board.notice.persistence.entity.NoticeBoard;
import com.pplip.domain.board.notice.usecase.NoticeService;
import com.pplip.domain.board.notice.usecase.model.NoticeBoardModel;
import com.pplip.domain.board.notice.utils.NoticeBoardParser;
import com.pplip.domain.file.api.request.FileRequest;
import com.pplip.domain.file.persistence.dao.NoticeBoardImagePropertyDao;
import com.pplip.domain.file.persistence.entity.FileProperty;
import com.pplip.domain.file.persistence.entity.ModifyStatus;
import com.pplip.domain.file.persistence.entity.ImageType;
import com.pplip.domain.file.persistence.entity.NoticeBoardImageProperty;
import com.pplip.domain.file.usecase.FileService;
import com.pplip.global.api.code.ErrorCode;
import com.pplip.global.exception.BoardLogicException;
import com.pplip.global.page.Page;
import com.pplip.global.page.PageRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class NoticeServiceImpl implements NoticeService {

    private final NoticeDao dao;
    private final NoticeBoardImagePropertyDao imagePropertyDao;
    private final NoticeBoardParser noticeBoardParser;
    private final FileService fileService;

    @Override
    public Page<NoticeResponse.Summary> findAll(PageRequest pageRequest) {
        List<NoticeResponse.Summary> datas = dao.findAll(pageRequest);
        int count = dao.noticeBoardAllCount();

        if (!SecurityUtils.isAnonymous()) {
            Long authorId = SecurityUtils.getCurrentUser().getUserId();
            datas = datas.stream().map(data -> {
                data.setAuthor(data.getAuthorId() == authorId);
                return data;
            }).toList();
        }
        return new Page<>(datas, pageRequest.getPageNum(), pageRequest.getPageSize(), count);
    }

    @Override
    public Page<NoticeResponse.Summary> findAllByUserId(UserDetails userDetails, PageRequest pageRequest) {
        Long userId = ((Account) userDetails).getUserId();
        List<NoticeResponse.Summary> resData = dao.findAllByUserId(userId, pageRequest);
        int count = dao.noticeBoardAllCountByUserId(userId);

        resData.forEach(data -> data.setAuthor(data.getAuthorId() == userId));

        return new Page<>(resData, pageRequest.getPageNum(), pageRequest.getPageSize(), count);
    }

    @Override
    public NoticeResponse.Detail post(NoticeRequest.Post post, UserDetails userDetails) {
        Long userId = SecurityUtils.resolveUserId(userDetails);

        NoticeBoardModel boardModel = new NoticeBoardModel(post, userId);
        NoticeBoard entity = boardModel.toEntity();

        if (dao.insert(entity) == 0) {
            throw new BoardLogicException(ErrorCode.FAIL_TO_CREATE_BOARD);
        }

        // 이미지들의 board_id 값을 업데이트.

        if (!post.getImageIds().isEmpty()) {
            if (imagePropertyDao.bulkUpdate(post.getImageIds(), entity.getId()) != post.getImageIds().size()) {
                throw new BoardLogicException(ErrorCode.FAIL_TO_CREATE_BOARD);
            }
        }

        NoticeResponse.Detail resData = dao.findById(entity.getId())
                .orElseThrow(() -> new BoardLogicException(ErrorCode.FAIL_TO_CREATE_BOARD));

        resData.setAuthor(resData.getAuthorId() == userId);
        return resData;
    }

    @Override
    @CountView(BoardType.NOTICE)
//    @Transactional(readOnly = true)
    public NoticeResponse.Detail findById(Long id) {
        NoticeResponse.Detail resData = dao.findById(id)
                .orElseThrow(() -> new BoardLogicException(ErrorCode.BOARD_NOT_FOUND_ERROR));

        if (!SecurityUtils.isAnonymous()) {
            Long userId = SecurityUtils.getCurrentUser().getUserId();
            resData.setAuthor(resData.getAuthorId() == userId);
        }

        return resData;
    }

    @Override
    public NoticeResponse.Update update(NoticeRequest.Update update, Long id, UserDetails userDetails) {
        Long userId = SecurityUtils.resolveUserId(userDetails);

        NoticeBoard entity = dao.findByIdToEntity(id)
                .orElseThrow(() -> new BoardLogicException(ErrorCode.BOARD_NOT_FOUND_ERROR));

        if (!entity.getAuthorId().equals(userId)) {
            throw new BoardLogicException(ErrorCode.FORBIDDEN, "작성자만 수정할 수 있습니다.");
        }

        entity.update(update);
        dao.update(entity);

        List<Long> removeImgIds = update.getImages().stream().filter(img -> img.getStatus().equals(ModifyStatus.REMOVE)).map(FileRequest::getId).toList();

        if (!removeImgIds.isEmpty()) {
            List<NoticeBoardImageProperty> removeImgs = imagePropertyDao.findAllByIds(removeImgIds);

            fileService.deleteSavedFiles(removeImgIds, ImageType.NOTICE);
            fileService.deleteOriginFiles(removeImgs.stream().map(FileProperty::getPath).toList());
        }


        List<Long> updateImgIds = update.getImages().stream().filter(img -> img.getStatus().equals(ModifyStatus.NEW)).map(FileRequest::getId).toList();
        if (!updateImgIds.isEmpty()) {
            imagePropertyDao.bulkUpdate(updateImgIds, id);
        }

        NoticeResponse.Detail detail = dao.findById(id)
                .orElseThrow(() -> new BoardLogicException(ErrorCode.BOARD_NOT_FOUND_ERROR));

        detail.setAuthor(detail.getAuthorId() == userId);

        return noticeBoardParser.detailResToUpdateRes(detail);
    }

    @Override
    public void remove(Long id, UserDetails userDetails) {
        Long userId = SecurityUtils.resolveUserId(userDetails);
        NoticeBoard entity = dao.findByIdToEntity(id)
                .orElseThrow(() -> new BoardLogicException(ErrorCode.BOARD_NOT_FOUND_ERROR));

        if (!entity.getAuthorId().equals(userId)) {
            throw new BoardLogicException(ErrorCode.FORBIDDEN, "작성자만 삭제할 수 있습니다.");
        }

        if (dao.delete(id) != 1) {
            throw new BoardLogicException(ErrorCode.BOARD_FAIL_DELETE, "게시판 삭제에 실패했습니다.");
        }
    }
}
