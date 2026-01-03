package com.pplip.domain.trip.ai.service;

import com.pplip.domain.trip.ai.dto.request.AiRequest;
import com.pplip.domain.trip.ai.dto.response.AiResponse;
import com.pplip.domain.trip.attraction.persistence.entity.ContentType;
import com.pplip.domain.trip.plan.api.request.PlanRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

//임시 데이터 반환 추후 ai 서비스와 연결시 새로운 서비스로 구현

public class MockInferenceService implements InferenceService {


	@Override
	public List<AiResponse.SuggestAttraction> suggestAttraction(AiRequest.SuggestAttractions request) {
		AiResponse.SuggestAttraction mock = AiResponse.SuggestAttraction.
				builder()
				.no(56644L)
				.title("가회동 성당")
				.latitude(new BigDecimal(37.582085))
				.longitude(new BigDecimal(126.984))
				.contentType(ContentType.ATTRACTION)
				.address("서울특별시종로구 북촌로 57 (가회동)")
				.bigImage("http://tong.visitkorea.or.kr/cms/resource/09/3303909_image2_1.jpg")
				.thumbnail("http://tong.visitkorea.or.kr/cms/resource/09/3303909_image3_1.jpg")
				.tags(List.of("기도하기좋은", "고즈넉한", "소원성취", "문학산책", "가족나들이", "데이트코스"))
				.homepage("<a href=\"https://gahoe.or.kr\" target=\"_blank\" title=\"새창 : 가회동성당 사이트로 이동\">https://gahoe.or.kr</a>")
				.build();
		return List.of(mock);
	}

	@Override
	public AiResponse.SuggestPlan suggestPlan(PlanRequest.SuggestPlan request) {
		return null;
	}
}

