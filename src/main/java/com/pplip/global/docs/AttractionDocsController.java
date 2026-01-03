package com.pplip.global.docs;

import com.pplip.domain.trip.ai.dto.response.AiResponse;
import com.pplip.domain.trip.attraction.api.request.AttractionRequest;
import com.pplip.domain.trip.attraction.api.response.AttractionResponse;
import com.pplip.global.api.response.CommonResponse;
import com.pplip.global.page.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.ModelAttribute;
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
     * @param search 검색정보
     * @return 검색된 관광지 목록
     */
    @Operation(summary = "관광지 검색")
    @ApiResponse(responseCode = "200", description = "성공")
    CommonResponse<Page<AttractionResponse.Summary>> searchAttractions(AttractionRequest.Search search);

    /**
     * 사용자의 여행 계획을 기반으로 AI를 사용하여 관광지를 추천합니다.
     *
     * @param suggest 관광지 추천 요청 정보
     * @param userDetails 현재 로그인한 사용자 정보
     * @return 추천된 관광지 목록
     */
    @Operation(summary = "AI 기반 관광지 추천")
    @ApiResponse(responseCode = "200", description = "성공")
    CommonResponse<List<AiResponse.SuggestAttraction>> suggestAttractions(@RequestBody AttractionRequest.Suggest suggest, UserDetails userDetails);

    /**
     * 시도 구군 코드를 기반으로 AI를 사용하여 관광지를 추천합니다.
     *
     * @param suggest 시도 구군 코드
     * @param userDetails 현재 로그인한 사용자 정보
     * @return 추천된 관광지 목록
     */
    @Operation(summary = "시도, 구군 코드로 AI 기반 관광지 추천")
    @ApiResponse(responseCode = "200", description = "성공")
    CommonResponse<List<AiResponse.SuggestAttraction>> suggestAttractionsBySidoGuguns(@ModelAttribute AttractionRequest.SuggestBySidoGuguns suggest, @AuthenticationPrincipal UserDetails userDetails);

    /**
     * 사용자의 여행 계획을 기반으로 AI를 사용하여 관광지를 추천합니다.
     *
     * @param no 관광지 번호
     * @return 추천된 관광지 목록
     */
    @Operation(summary = "관광지 상세검색")
    @ApiResponse(responseCode = "200", description = "성공")
    CommonResponse<AttractionResponse.Details> getAttractionDetail(Long no);
}
