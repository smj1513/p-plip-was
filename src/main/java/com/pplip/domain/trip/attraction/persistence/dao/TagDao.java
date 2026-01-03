package com.pplip.domain.trip.attraction.persistence.dao;

import com.pplip.domain.trip.attraction.persistence.entity.Tag;
import com.pplip.global.page.PageRequest;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 태그 데이터에 접근하는 DAO 인터페이스
 */
@Mapper
public interface TagDao {
    List<Tag> findAll(PageRequest pageRequest);

    List<Tag> findAllByAttractionNo(Long no);
}
