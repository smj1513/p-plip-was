package com.pplip.domain.trip.attraction.persistence.dao;

import com.pplip.domain.trip.attraction.persistence.entity.SearchHistory;
import com.pplip.global.page.PageRequest;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

/**
 * 검색 기록 데이터에 접근하는 DAO 인터페이스
 */
@Mapper
public interface SearchHistoryDao {

    int insert(SearchHistory searchHistory);

    List<SearchHistory> findAll(Long userId, PageRequest pageRequest);

    int countAll(Long userId);

    int delete(Long id);

    Optional<SearchHistory> findByIdAndUserId(Long id, Long userId);
}
