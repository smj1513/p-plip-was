package com.pplip.domain.file.persistence.dao;

import com.pplip.domain.file.persistence.dao.mapper.ReviewImagePropertyMapper;
import com.pplip.domain.file.persistence.entity.ImageType;
import com.pplip.domain.file.persistence.entity.ReviewImageProperty;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ReviewImagePropertyDao implements BatchSupportFilePropertyDao<ReviewImageProperty> {

	private final ReviewImagePropertyMapper mapper;

	@Override
	public int bulkUpdate(List<Long> imageIds, Long refId) {
		return mapper.bulkUpdateReviewId(imageIds, refId);
	}

	@Override
	public int insertAll(List<ReviewImageProperty> list) {
		return mapper.insertAll(list);
	}

	@Override
	public List<ReviewImageProperty> findAllByIds(List<Long> fileIds) {
		return mapper.findAllByIds(fileIds);
	}

	@Override
	public void deleteAllById(List<Long> ids) {
		mapper.deleteAllById(ids);
	}

	@Override
	public int insert(ReviewImageProperty property) {
		return mapper.insert(property);
	}

	@Override
	public int delete(Long id) {
		return mapper.delete(id);
	}

	@Override
	public Optional<ReviewImageProperty> findById(Long id) {
		return mapper.findById(id);
	}

	@Override
	public boolean supports(ImageType imageType) {
		return ImageType.REVIEW.equals(imageType);
	}

	@Override
	public ImageType supports() {
		return ImageType.REVIEW;
	}

	public List<ReviewImageProperty> findAllByReviewId(Long reviewId) {
		return mapper.findAllByReviewId(reviewId);
	}
}
