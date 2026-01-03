package com.pplip.domain.trip.review.persistence.dao;

import com.pplip.domain.trip.review.api.response.ReviewResponse;
import com.pplip.domain.trip.review.persistence.entity.Review;
import com.pplip.domain.trip.review.persistence.entity.ReviewSort;
import com.pplip.global.page.PageRequest;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

/**
 * 리뷰 데이터에 접근하는 DAO 인터페이스
 */
@Mapper
public interface ReviewDao {
    int insert(Review review);

    List<ReviewResponse.Detail> findAllByAttractionNo(Long attractionNo, PageRequest pageRequest, ReviewSort sort);

    List<ReviewResponse.DetailWithAttractionName> findAllByUserId(Long userId, PageRequest pageRequest, ReviewSort sort);

    Optional<ReviewResponse.Detail> findById(Long id);

    Optional<Review> findByIdToEntity(Long id);

    int countAllByAttractionNo(Long attractionNo);

    int countAllByUserId(Long userId);

    int update(Review review);

    int delete(Long id);

}
