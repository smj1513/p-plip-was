package com.pplip.domain.trip.attraction.api.controller;

import com.pplip.domain.trip.attraction.api.request.AttractionRequest;
import com.pplip.domain.trip.attraction.api.response.AttractionResponse;
import com.pplip.global.api.code.SuccessCode;
import com.pplip.global.api.response.CommonResponse;
import com.pplip.global.docs.AttractionDocsController;
import com.pplip.global.page.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 여행지 관련 API 요청을 처리하는 컨트롤러
 */
@RestController
@RequestMapping("/trip/attraction")
public class AttractionController implements AttractionDocsController {

    /**
     * 행정구역 정보를 조회합니다.
     *
     * @return 행정구역 정보 목록
     */
    @GetMapping("/region")
    @Override
    public CommonResponse<List<AttractionResponse.Region>> getAdministrativeDistrict() {
        return CommonResponse.success(SuccessCode.SUCCESS, null);
    }

    /**
     * 키워드를 사용하여 관광지를 검색합니다.
     *
     * @param query 검색어
     * @param numResult 검색 결과 수
     * @return 검색된 관광지 목록
     */
    @GetMapping("/search")
    public CommonResponse<Page<AttractionResponse.Summary>> searchAttractions(@RequestParam String query, @RequestParam int numResult) {
        return CommonResponse.success(SuccessCode.SUCCESS, null);
    }

    /**
     * 사용자의 여행 계획을 기반으로 AI를 사용하여 관광지를 추천합니다.
     *
     * @param suggest 관광지 추천 요청 정보
     * @param userDetails 현재 로그인한 사용자 정보
     * @return 추천된 관광지 목록
     */
    @GetMapping("/suggest")
    public CommonResponse<Page<AttractionResponse.Summary>> suggestAttractions(@RequestBody AttractionRequest.Suggest suggest, @AuthenticationPrincipal UserDetails userDetails){
        return CommonResponse.success(SuccessCode.SUCCESS,null);

    }




}
