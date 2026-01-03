package com.pplip.global.docs;

import com.pplip.domain.trip.attraction.api.request.SearchHistoryRequest;
import com.pplip.domain.trip.attraction.api.response.SearchHistoryResponse;
import com.pplip.global.api.response.CommonResponse;
import com.pplip.global.page.Page;
import com.pplip.global.page.PageRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 검색기록 관련 API 명세를 정의하는 인터페이스
 */
@Tag(name = "검색기록 API", description = "검색기록 api")
public interface SearchHistoryDocsController {

    /**
     * 검색 기록을 조회합니다.
     *
     * @param pageRequest 페이징 정보
     * @param userDetails 현재 로그인한 사용자 정보
     * @return 검색 기록 목록
     */
    @Operation(summary = "검색 기록 조회")
    @ApiResponse(responseCode = "200", description = "성공")
    @GetMapping
    CommonResponse<Page<SearchHistoryResponse.History>> getSearchHistory(@ModelAttribute PageRequest pageRequest,
                                                                         @AuthenticationPrincipal UserDetails userDetails);
    /**
     * 검색 기록을 삭제합니다.
     *
     * @param id 삭제할 검색 기록 ID
     * @param userDetails 현재 로그인한 사용자 정보
     * @return 삭제된 검색 기록 ID
     */
    @Operation(summary = "검색 기록 삭제")
    @ApiResponse(responseCode = "200", description = "성공")
    @DeleteMapping("/{id}")
    CommonResponse<Long> removeSearchHistory(@PathVariable Long id,
                                             @AuthenticationPrincipal UserDetails userDetails);
}
