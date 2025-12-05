package com.pplip.global.docs;

import com.pplip.domain.trip.attraction.api.request.AttractionRequest;
import com.pplip.domain.trip.attraction.api.response.AttractionResponse;
import com.pplip.global.api.response.CommonResponse;
import com.pplip.global.page.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 여행지 관련 API 명세를 정의하는 인터페이스
 */
@Tag(name = "여행지 API", description = "여행지 api")
public interface AttractionDocsController {

    /**
     * 행정구역 정보를 조회합니다.
     *
     * @return 행정구역 정보 목록
     */
    @Operation(summary = "행정구역 정보 조회")
    @ApiResponse(responseCode = "200", description = "성공")
    CommonResponse<List<AttractionResponse.Region>> getAdministrativeDistrict();

    /**
     * 키워드를 사용하여 관광지를 검색합니다.
     *
     * @param query 검색어
     * @param numResult 검색 결과 수
     * @return 검색된 관광지 목록
     */
    @Operation(summary = "관광지 검색")
    @ApiResponse(responseCode = "200", description = "성공")
    CommonResponse<Page<AttractionResponse.Summary>> searchAttractions(@RequestParam String query, @RequestParam int numResult);

    /**
     * 사용자의 여행 계획을 기반으로 AI를 사용하여 관광지를 추천합니다.
     *
     * @param suggest 관광지 추천 요청 정보
     * @param userDetails 현재 로그인한 사용자 정보
     * @return 추천된 관광지 목록
     */
    @Operation(summary = "AI 기반 관광지 추천")
    @ApiResponse(responseCode = "200", description = "성공")
    CommonResponse<Page<AttractionResponse.Summary>> suggestAttractions(@RequestBody AttractionRequest.Suggest suggest, UserDetails userDetails);
}
