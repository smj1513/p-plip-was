package com.pplip.domain.trip.attraction.persistence.dao;

import com.pplip.domain.trip.attraction.persistence.entity.Tag;
import com.pplip.global.page.PageRequest;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TagDao {
    List<Tag> findAll(PageRequest pageRequest);

    List<Tag> findAllByAttractionNo(Long no);
}
