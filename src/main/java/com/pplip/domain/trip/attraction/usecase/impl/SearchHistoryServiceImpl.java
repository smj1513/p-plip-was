package com.pplip.domain.trip.attraction.usecase.impl;

import com.pplip.domain.auth.persistence.entity.Account;
import com.pplip.domain.auth.utils.SecurityUtils;
import com.pplip.domain.trip.attraction.api.request.AttractionRequest;
import com.pplip.domain.trip.attraction.api.request.SearchHistoryRequest;
import com.pplip.domain.trip.attraction.api.response.SearchHistoryResponse;
import com.pplip.domain.trip.attraction.persistence.dao.SearchHistoryDao;
import com.pplip.domain.trip.attraction.persistence.entity.SearchHistory;
import com.pplip.domain.trip.attraction.usecase.SearchHistoryService;
import com.pplip.domain.trip.attraction.usecase.model.SearchHistoryModel;
import com.pplip.global.api.code.ErrorCode;
import com.pplip.global.exception.BoardLogicException;
import com.pplip.global.exception.BusinessLogicException;
import com.pplip.global.page.Page;
import com.pplip.global.page.PageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class SearchHistoryServiceImpl implements SearchHistoryService {

    private final SearchHistoryDao dao;

    @Override
    public Page<SearchHistoryResponse.History> findAll(PageRequest pageRequest, UserDetails userDetails) {
        Long userId = SecurityUtils.resolveUserId(userDetails);

        List<SearchHistoryResponse.History> resData = dao.findAll(userId, pageRequest).stream()
                .map(data -> SearchHistoryResponse.History.builder()
                        .keyword(data.getKeyword())
                        .id(data.getId())
                        .searchedAt(data.getSearchedAt())
                        .build())
                .toList();

        return new Page<>(resData, pageRequest.getPageNum(), pageRequest.getPageSize(), dao.countAll(userId));
    }

    @Override
    public void post(AttractionRequest.Search post, UserDetails userDetails) {
        Long userId = SecurityUtils.resolveUserId(userDetails);
        SearchHistory entity = new SearchHistoryModel(post, userId).toEntity();

        if (dao.insert(entity) == 0) {
            throw new BusinessLogicException(ErrorCode.FAIL_TO_CREATE_SEARCH_HISTORY, "검색 기록 작성에 실패했습니다.");
        }
    }

    @Override
    public long delete(Long id, UserDetails userDetails) {
        Long userId = SecurityUtils.resolveUserId(userDetails);
        SearchHistory entity = dao.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new BoardLogicException(ErrorCode.ATTRACTION_NOT_FOUND, "검색 기록이 존재하지 않습니다"));

        if (!entity.getUserId().equals(userId)) {
            throw new BusinessLogicException(ErrorCode.FORBIDDEN, "기록자만 삭제할 수 있습니다.");
        }

        if (dao.delete(entity.getId()) == 0) {
            throw new BusinessLogicException(ErrorCode.FAIL_TO_DELETE_SEARCH_HISTORY, "검색 기록 삭제에 실패했습니다.");
        }
        return id;
    }
}
