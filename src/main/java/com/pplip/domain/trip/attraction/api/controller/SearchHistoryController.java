package com.pplip.domain.trip.attraction.api.controller;


import com.pplip.domain.trip.attraction.api.request.SearchHistoryRequest;
import com.pplip.domain.trip.attraction.api.response.SearchHistoryResponse;
import com.pplip.domain.trip.attraction.usecase.SearchHistoryService;
import com.pplip.global.api.code.SuccessCode;
import com.pplip.global.api.response.CommonResponse;
import com.pplip.global.docs.SearchHistoryDocsController;
import com.pplip.global.page.Page;
import com.pplip.global.page.PageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 검색기록 관련 API 요청을 처리하는 컨트롤러
 */
@RequestMapping("/trip/attraction/search-history")
@RestController
@RequiredArgsConstructor
public class SearchHistoryController implements SearchHistoryDocsController {

    private final SearchHistoryService historyService;

    @GetMapping
    @Override
    public CommonResponse<Page<SearchHistoryResponse.History>> getSearchHistory(@ModelAttribute PageRequest pageRequest,
                                                                                @AuthenticationPrincipal UserDetails userDetails) {
        return CommonResponse.success(SuccessCode.SUCCESS, historyService.findAll(pageRequest, userDetails));
    }

    @DeleteMapping("/{id}")
    @Override
    public CommonResponse<Long> removeSearchHistory(@PathVariable Long id,
                                                    @AuthenticationPrincipal UserDetails userDetails) {
        return CommonResponse.success(SuccessCode.REMOVED, historyService.delete(id, userDetails));
    }
}
