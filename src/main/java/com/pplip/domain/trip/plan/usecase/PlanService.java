package com.pplip.domain.trip.plan.usecase;

import com.pplip.domain.trip.plan.api.request.PlanRequest;
import com.pplip.domain.trip.plan.api.response.PlanResponse;
import com.pplip.global.page.Page;
import com.pplip.global.page.PageRequest;
import org.springframework.security.core.userdetails.UserDetails;

public interface PlanService {
	/**
	 * 여행 계획 목록을 조회합니다.
	 *
	 * @param pageRequest 페이징 정보
	 * @param userDetails 사용자 정보
	 * @return 페이징 처리된 여행 계획 요약 목록
	 */
	Page<PlanResponse.Summary> getPlans(PageRequest pageRequest, UserDetails userDetails);

	/**
	 * 여행 계획 상세 정보를 조회합니다.
	 *
	 * @param id 조회할 여행 계획 ID
	 * @return 여행 계획 상세 정보
	 */
	PlanResponse.PlanDetail getPlanDetail(Long id);

	/**
	 * 새로운 여행 계획을 생성합니다.
	 *
	 * @param request 생성할 여행 계획 정보
	 * @param userDetails 사용자 정보
	 * @return 생성된 여행 계획 상세 정보
	 */
	PlanResponse.PlanDetail createPlan(PlanRequest.Post request, UserDetails userDetails);

	/**
	 * 여행 계획을 수정합니다.
	 *
	 * @param update 수정할 여행 계획 정보
	 * @param id 수정할 여행 계획 ID
	 * @return 수정된 여행 계획 정보
	 */
	PlanResponse.Update updatePlan(PlanRequest.Update update, Long id);

	/**
	 * 여행 계획을 삭제합니다.
	 *
	 * @param id 삭제할 여행 계획 ID
	 * @return 삭제된 여행 계획 정보
	 */
	PlanResponse.Remove removePlan(Long id);

	PlanResponse.PlanDetail suggestPlan(PlanRequest.SuggestPlan suggest);

	PlanResponse.PlanDetail randomPlan(PlanRequest.RandomPlan suggest);
}
