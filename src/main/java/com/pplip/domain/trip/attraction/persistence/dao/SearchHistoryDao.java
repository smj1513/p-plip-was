package com.pplip.domain.trip.attraction.persistence.dao;

import com.pplip.domain.trip.attraction.persistence.entity.SearchHistory;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SearchHistoryDao {

    int insert(SearchHistory searchHistory);

    List<SearchHistory> findAll(Long userId);
}
