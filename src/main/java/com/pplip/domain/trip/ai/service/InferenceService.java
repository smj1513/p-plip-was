package com.pplip.domain.trip.ai.service;

import com.pplip.domain.trip.ai.dto.request.AiRequest;
import com.pplip.domain.trip.ai.dto.response.AiResponse;
import com.pplip.domain.trip.plan.api.request.PlanRequest;
import com.pplip.domain.trip.plan.api.response.PlanResponse;

import java.util.List;

public interface InferenceService {
	List<AiResponse.SuggestAttraction> suggestAttraction(AiRequest.SuggestAttractions request);

	AiResponse.SuggestPlan suggestPlan(PlanRequest.SuggestPlan request);
}
