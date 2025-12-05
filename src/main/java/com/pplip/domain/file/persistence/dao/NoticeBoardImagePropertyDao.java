package com.pplip.domain.file.persistence.dao;

import com.pplip.domain.file.persistence.dao.mapper.NoticeBoardImagePropertyMapper;
import com.pplip.domain.file.persistence.entity.ImageType;
import com.pplip.domain.file.persistence.entity.NoticeBoardImageProperty;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class NoticeBoardImagePropertyDao implements BatchSupportFilePropertyDao<NoticeBoardImageProperty>{
	private final NoticeBoardImagePropertyMapper mapper;
	@Override
	public int insert(NoticeBoardImageProperty property) {
		return mapper.insert(property);
	}

	@Override
	public int delete(Long id) {
		return mapper.delete(id);
	}

	@Override
	public Optional<NoticeBoardImageProperty> findById(Long id) {
		return mapper.findById(id);
	}


	@Override
	public int bulkUpdate(List<Long> imageIds, Long refId) {
		return mapper.bulkUpdateBoardId(imageIds, refId);
	}

	@Override
	public int insertAll(List<NoticeBoardImageProperty> list) {
		return mapper.insertAll(list);
	}

	@Override
	public List<NoticeBoardImageProperty> findAllByIds(List<Long> fileIds) {
		return mapper.findAllByIds(fileIds);
	}

	@Override
	public boolean supports(ImageType imageType) {
		return ImageType.NOTICE.equals(imageType);
	}
}
