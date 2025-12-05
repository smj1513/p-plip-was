package com.pplip.global.docs;

import com.pplip.domain.trip.plan.api.request.PlanRequest;
import com.pplip.domain.trip.plan.api.response.PlanResponse;
import com.pplip.global.api.response.CommonResponse;
import com.pplip.global.page.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

/**
 * 여행 계획 관련 API 명세서
 */
@Tag(name = "여행 계획 API", description = "여행 계획 api")
public interface PlanDocsController {

    /**
     * 사용자의 모든 여행 계획 리스트를 조회합니다.
     * @param userDetails 로그인한 사용자 정보
     * @return PlanResponse.Summary 여행 계획 요약 정보 리스트
     */
    @Operation(summary = "여행 계획 리스트 조회")
    @ApiResponse(responseCode = "200", description = "성공")
    CommonResponse<Page<PlanResponse.Summary>> listPlan(UserDetails userDetails);

    /**
     * 특정 여행 계획의 상세 정보를 조회합니다.
     * @param id 조회할 여행 계획 ID
     * @return PlanResponse.Detail 여행 계획 상세 정보
     */
    @Operation(summary = "여행 계획 단건 조회")
    @ApiResponse(responseCode = "200", description = "성공")
    CommonResponse<PlanResponse.Detail> getPlan(Long id);

    /**
     * 새로운 여행 계획을 생성합니다.
     * @param request 여행 계획 생성 요청 정보
     * @param userDetails 로그인한 사용자 정보
     * @return PlanResponse.Detail 생성된 여행 계획 상세 정보
     */
    @Operation(summary = "여행 계획 생성")
    @ApiResponse(responseCode = "201", description = "생성")
    CommonResponse<PlanResponse.Detail> postPlan(PlanRequest.Post request, UserDetails userDetails);

    /**
     * 기존 여행 계획을 수정합니다.
     * @param update 수정할 여행 계획 정보
     * @param id 수정할 여행 계획 ID
     * @return PlanResponse.Update 수정된 여행 계획 정보
     */
    @Operation(summary = "여행 계획 수정")
    @ApiResponse(responseCode = "202", description = "수정")
    CommonResponse<PlanResponse.Update> updatePlan(PlanRequest.Update update, Long id);

    /**
     * 특정 여행 계획을 삭제합니다.
     * @param id 삭제할 여행 계획 ID
     * @return 203
     */
    @Operation(summary = "여행 계획 삭제")
    @ApiResponse(responseCode = "203", description = "삭제")
    CommonResponse<?> deletePlan(Long id);
}
