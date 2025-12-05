package com.pplip.domain.trip.plan.api.controller;

import com.pplip.domain.trip.plan.api.request.PlanRequest;
import com.pplip.domain.trip.plan.api.response.PlanResponse;
import com.pplip.global.api.code.SuccessCode;
import com.pplip.global.api.response.CommonResponse;
import com.pplip.global.docs.PlanDocsController;
import com.pplip.global.page.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 여행 계획 관련 API를 처리하는 컨트롤러
 */
@RestController
@RequestMapping("/trip/plan")
@RequiredArgsConstructor
public class PlanController implements PlanDocsController {
    /**
     * 사용자의 모든 여행 계획 리스트를 조회합니다.
     * @param userDetails 로그인한 사용자 정보
     * @return PlanResponse.Summary 여행 계획 요약 정보 리스트
     */
    @Override
    @GetMapping
    public CommonResponse<Page<PlanResponse.Summary>> listPlan(@AuthenticationPrincipal UserDetails userDetails) {
        return CommonResponse.success(SuccessCode.SUCCESS, null);
    }

    /**
     * 특정 여행 계획의 상세 정보를 조회합니다.
     * @param id 조회할 여행 계획 ID
     * @return PlanResponse.Detail 여행 계획 상세 정보
     */
    @Override
    @GetMapping("/{id}")
    public CommonResponse<PlanResponse.Detail> getPlan(Long id) {
        return CommonResponse.success(SuccessCode.SUCCESS, null);
    }

    /**
     * 새로운 여행 계획을 생성합니다.
     * @param request 여행 계획 생성 요청 정보
     * @param userDetails 로그인한 사용자 정보
     * @return PlanResponse.Detail 생성된 여행 계획 상세 정보
     */
    @Override
    @PostMapping
    public CommonResponse<PlanResponse.Detail> postPlan(PlanRequest.Post request, @AuthenticationPrincipal UserDetails userDetails) {
        return CommonResponse.success(SuccessCode.CREATED, null);
    }

    /**
     * 기존 여행 계획을 수정합니다.
     * @param update 수정할 여행 계획 정보
     * @param id 수정할 여행 계획 ID
     * @return PlanResponse.Update 수정된 여행 계획 정보
     */
    @Override
    @PutMapping("/{id}")
    public CommonResponse<PlanResponse.Update> updatePlan(PlanRequest.Update update, @PathVariable Long id) {
        return CommonResponse.success(SuccessCode.UPDATED, null);
    }

    /**
     * 특정 여행 계획을 삭제합니다.
     * @param id 삭제할 여행 계획 ID
     * @return 203
     */
    @Override
    @DeleteMapping("/{id}")
    public CommonResponse<?> deletePlan(Long id) {
        return CommonResponse.success(SuccessCode.REMOVED, null);
    }
}
