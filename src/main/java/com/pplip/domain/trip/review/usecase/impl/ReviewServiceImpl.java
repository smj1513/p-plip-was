package com.pplip.domain.trip.review.usecase.impl;

import com.pplip.domain.auth.persistence.entity.Account;
import com.pplip.domain.auth.utils.SecurityUtils;
import com.pplip.domain.file.persistence.dao.ReviewImagePropertyDao;
import com.pplip.domain.file.persistence.entity.ModifyStatus;
import com.pplip.domain.file.persistence.entity.ImageType;
import com.pplip.domain.file.persistence.entity.ReviewImageProperty;
import com.pplip.domain.file.usecase.FileService;
import com.pplip.domain.trip.review.api.request.ReviewRequest;
import com.pplip.domain.trip.review.api.response.ReviewResponse;
import com.pplip.domain.trip.review.persistence.dao.ReviewDao;
import com.pplip.domain.trip.review.persistence.entity.Review;
import com.pplip.domain.trip.review.persistence.entity.ReviewSort;
import com.pplip.domain.trip.review.usecase.ReviewService;
import com.pplip.domain.trip.review.usecase.model.ReviewModel;
import com.pplip.domain.trip.review.utils.ReviewParser;
import com.pplip.global.api.code.ErrorCode;
import com.pplip.global.exception.BoardLogicException;
import com.pplip.global.exception.BusinessLogicException;
import com.pplip.global.page.Page;
import com.pplip.global.page.PageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewDao reviewDao;
    private final ReviewImagePropertyDao imagePropertyDao;
    private final FileService fileService;
    private final ReviewParser parser;

    @Override
    public Page<ReviewResponse.Detail> findAll(Long attractionId, PageRequest pageRequest, ReviewSort sort) {
        List<ReviewResponse.Detail> resData = reviewDao.findAllByAttractionNo(attractionId, pageRequest, sort);
        int count = reviewDao.countAllByAttractionNo(attractionId);

        resData.forEach(data -> {
            if (!SecurityUtils.isAnonymous()) {
                Long userId = SecurityUtils.getCurrentUser().getUserId();
                data.setAuthor(data.getAuthorId().equals(userId));
            }
        });

        if (!SecurityUtils.isAnonymous()) {
            Long userId = SecurityUtils.getCurrentUser().getUserId();
            resData.forEach(data -> data.setAuthor(data.getAuthorId().equals(userId)));
        }

        return new Page<>(resData, pageRequest.getPageNum(), pageRequest.getPageSize(), count);
    }

    @Override
    public Page<ReviewResponse.DetailWithAttractionName> findAllByUserId(UserDetails userDetails, PageRequest pageRequest, ReviewSort sort) {
        Long userId = ((Account) userDetails).getUserId();
        List<ReviewResponse.DetailWithAttractionName> resData = reviewDao.findAllByUserId(userId, pageRequest, sort);

        resData.forEach(data -> data.setAuthor(data.getAuthorId().equals(userId)));
        int count = reviewDao.countAllByUserId(userId);

        return new Page<>(resData, pageRequest.getPageNum(), pageRequest.getPageSize(), count);
    }

    @Override
    public ReviewResponse.Detail post(ReviewRequest.Post post, Long attractionId, UserDetails userDetails) {
        Long userId = SecurityUtils.resolveUserId(userDetails);

        ReviewModel model = new ReviewModel(post, userId, attractionId);
        Review entity = model.toEntity();

        if (reviewDao.insert(entity) == 0) {
            throw new BusinessLogicException(ErrorCode.REVIEW_CREATE_FAILURE, "리뷰 작성에 실패했습니다");
        }

        if(!post.getFileIds().isEmpty()) {
            imagePropertyDao.bulkUpdate(post.getFileIds(), entity.getId());
        }

        ReviewResponse.Detail resData = reviewDao.findById(entity.getId())
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.REVIEW_NOT_FOUND, "리뷰 조회에 실패했습니다."));
        resData.setAuthor(resData.getAuthorId().equals(userId));

        return resData;
    }

    @Override
    public ReviewResponse.Update update(ReviewRequest.Update update, Long id, UserDetails userDetails) {
        Review entity = reviewDao.findByIdToEntity(id)
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.REVIEW_NOT_FOUND, "리뷰 조회에 실패했습니다."));

        Long userId = SecurityUtils.resolveUserId(userDetails);

        if (!entity.getAuthorId().equals(userId)) {
            throw new BusinessLogicException(ErrorCode.FORBIDDEN, "작성자만 수정할 수 있습니다.");
        }

        entity.setUpdatedAt(LocalDateTime.now());
        entity.setContent(update.getContent());

        if (!update.getFiles().isEmpty()) {
            List<Long> removeIds = update.getFiles().stream().filter(file -> file.getStatus().equals(ModifyStatus.REMOVE)).map(file -> file.getId()).toList();
            List<Long> newIds = update.getFiles().stream().filter(file -> file.getStatus().equals(ModifyStatus.NEW)).map(file -> file.getId()).toList();

            List<ReviewImageProperty> imageProperties = imagePropertyDao.findAllByReviewId(id);

            if (!removeIds.isEmpty()) {
                List<ReviewImageProperty> removeFiles = imageProperties.stream().filter(file -> removeIds.contains(file.getId())).toList();
                fileService.deleteSavedFiles(removeIds, ImageType.REVIEW);
                fileService.deleteOriginFiles(removeFiles.stream().map(file -> file.getPath()).toList());
            }
            if (!newIds.isEmpty()) {
                imagePropertyDao.bulkUpdate(newIds, id);
            }
        }
        reviewDao.update(entity);
        ReviewResponse.Detail resData = reviewDao.findById(entity.getId()).orElseThrow(() -> new BoardLogicException(ErrorCode.REVIEW_NOT_FOUND, "수정 후, 리뷰 조회에 실패했습니다."));

        resData.setAuthor(resData.getAuthorId().equals(userId));

        return parser.detailResToUpdateRes(resData);
    }

    @Override
    public long delete(Long id, UserDetails userDetails) {
        Review entity = reviewDao.findByIdToEntity(id).orElseThrow(() -> new BoardLogicException(ErrorCode.REVIEW_NOT_FOUND, "리뷰 조회에 실패했습니다."));

        Long userId = SecurityUtils.resolveUserId(userDetails);

        if (!entity.getAuthorId().equals(userId)) {
            throw new BusinessLogicException(ErrorCode.FORBIDDEN, "작성자만 삭제할 수 있습니다.");
        }
        if (reviewDao.delete(id) == 0) {
            throw new BusinessLogicException(ErrorCode.REVIEW_FAIL_DELETE, "리뷰 삭제에 실패했습니다.");
        }
        return entity.getId();
    }

}
