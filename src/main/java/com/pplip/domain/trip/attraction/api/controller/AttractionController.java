package com.pplip.domain.trip.attraction.api.controller;

import com.pplip.domain.trip.ai.dto.response.AiResponse;
import com.pplip.domain.trip.attraction.api.request.AttractionRequest;
import com.pplip.domain.trip.attraction.api.response.AttractionResponse;
import com.pplip.domain.trip.attraction.usecase.AttractionService;
import com.pplip.domain.trip.attraction.usecase.SidoGugunsService;
import com.pplip.global.api.code.SuccessCode;
import com.pplip.global.api.response.CommonResponse;
import com.pplip.global.docs.AttractionDocsController;
import com.pplip.global.page.Page;
import com.pplip.global.page.PageRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 여행지 관련 API 요청을 처리하는 컨트롤러
 */
@RestController
@RequestMapping("/trip/attraction")
@RequiredArgsConstructor
@Slf4j
public class AttractionController implements AttractionDocsController {

	private final AttractionService attractionService;
	private final SidoGugunsService sidoGugunsService;

	/**
	 * 행정구역 정보를 조회합니다.
	 *
	 * @return 행정구역 정보 목록
	 */
	@GetMapping("/region")
	@Override
	public CommonResponse<List<AttractionResponse.Region>> getAdministrativeDistrict() {
		return CommonResponse.success(SuccessCode.SUCCESS, sidoGugunsService.findAllRegion());
	}

	/**
	 * 키워드를 사용하여 관광지를 검색합니다.
	 *
	 * @param search 검색정보
	 * @return 검색된 관광지 목록
	 */
	@GetMapping("/search")
	public CommonResponse<Page<AttractionResponse.Summary>> searchAttractions(@ModelAttribute AttractionRequest.Search search) {
		PageRequest pageRequest = PageRequest.builder()
				.pageNum(search.getPageNum())
				.pageSize(search.getPageSize())
				.build();

		return CommonResponse.success(SuccessCode.SUCCESS, attractionService.findAllBySearch(search, pageRequest));
	}

	@GetMapping
	public CommonResponse<List<AttractionResponse.NearByAttraction>> getMainAttractions(@RequestParam Double latitude,
	                                                                                    @RequestParam Double longitude) {
		return CommonResponse.success(SuccessCode.SUCCESS, attractionService.getMainAttractions(latitude, longitude));
	}

	/**
	 * 사용자의 여행 계획을 기반으로 AI를 사용하여 관광지를 추천합니다.
	 *
	 * @param suggest     관광지 추천 요청 정보
	 * @param userDetails 현재 로그인한 사용자 정보
	 * @return 추천된 관광지 목록
	 */
	@PostMapping("/suggest")
	public CommonResponse<List<AiResponse.SuggestAttraction>> suggestAttractions(@RequestBody AttractionRequest.Suggest suggest,
	                                                                             @AuthenticationPrincipal UserDetails userDetails) {
		return CommonResponse.success(SuccessCode.SUCCESS, attractionService.suggestAttractions(suggest));
	}

	/**
	 * 시도 구군 코드를 기반으로 AI를 사용하여 관광지를 추천합니다.
	 *
	 * @param suggest 시도 구군 코드
	 * @param userDetails 현재 로그인한 사용자 정보
	 * @return 추천된 관광지 목록
	 */
	@GetMapping("/suggest/sido-guguns")
	public CommonResponse<List<AiResponse.SuggestAttraction>> suggestAttractionsBySidoGuguns(@ModelAttribute AttractionRequest.SuggestBySidoGuguns suggest,
																							 @AuthenticationPrincipal UserDetails userDetails) {
		log.info("suggest={}", suggest);
		return CommonResponse.success(SuccessCode.SUCCESS, null);
	}


    /**
     * 사용자의 여행 계획을 기반으로 AI를 사용하여 관광지를 추천합니다.
     *
     * @param no 관광지 번호
     * @return 추천된 관광지 목록
     */
    @Override
    @GetMapping("/{no}")
    public CommonResponse<AttractionResponse.Details> getAttractionDetail(@PathVariable Long no) {
        return CommonResponse.success(SuccessCode.SUCCESS, attractionService.findByNo(no));
    }
}