package com.pplip.domain.trip.review.persistence.dao;

import com.pplip.domain.trip.review.api.response.ReviewResponse;
import com.pplip.domain.trip.review.persistence.entity.Review;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ReviewDao {
    int insert(Review review);

    List<ReviewResponse.Detail> findAllByAttractionNo(Long attractionNo);

    int update(Review review);

    int delete(Long id);
}
