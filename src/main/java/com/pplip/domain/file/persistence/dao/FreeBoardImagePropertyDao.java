package com.pplip.domain.file.persistence.dao;

import com.pplip.domain.file.persistence.dao.mapper.FreeBoardImagePropertyMapper;
import com.pplip.domain.file.persistence.entity.FreeBoardImageProperty;
import com.pplip.domain.file.persistence.entity.ImageType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class FreeBoardImagePropertyDao implements BatchSupportFilePropertyDao<FreeBoardImageProperty>{
	private final FreeBoardImagePropertyMapper mapper;
	@Override
	public int insert(FreeBoardImageProperty property) {
		return mapper.insert(property);
	}

	@Override
	public int delete(Long id) {
		return mapper.delete(id);
	}

	@Override
	public Optional<FreeBoardImageProperty> findById(Long id) {
		return mapper.findById(id);
	}

	@Override
	public int bulkUpdate(List<Long> imageIds, Long refId) {
		return mapper.bulkUpdateBoardId(imageIds, refId);
	}

	@Override
	public int insertAll(List<FreeBoardImageProperty> list) {
		return mapper.insertAll(list);
	}

	@Override
	public List<FreeBoardImageProperty> findAllByIds(List<Long> fileIds) {
		return mapper.findAllByIds(fileIds);
	}

	@Override
	public void deleteAllById(List<Long> ids) {
		mapper.deleteAllByid(ids);
	}

	@Override
	public boolean supports(ImageType imageType) {
		return ImageType.FREE_BOARD.equals(imageType);
	}

	@Override
	public ImageType supports() {
		return ImageType.FREE_BOARD;
	}

	public List<FreeBoardImageProperty> findByBoardId(Long boardId){
		return mapper.findByBoardId(boardId);
	}

}
