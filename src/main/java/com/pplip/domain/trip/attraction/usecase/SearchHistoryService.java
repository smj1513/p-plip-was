package com.pplip.domain.trip.attraction.usecase;

import com.pplip.domain.trip.attraction.api.request.AttractionRequest;
import com.pplip.domain.trip.attraction.api.response.SearchHistoryResponse;
import com.pplip.global.page.Page;
import com.pplip.global.page.PageRequest;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface SearchHistoryService {
    Page<SearchHistoryResponse.History> findAll(PageRequest pageRequest, UserDetails userDetails);

    void post(AttractionRequest.Search search , UserDetails userDetails);

    long delete(Long id, UserDetails userDetails);
}
