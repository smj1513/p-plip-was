package com.pplip.domain.trip.plan.api.controller;

import com.pplip.domain.trip.plan.api.request.PlanRequest;
import com.pplip.domain.trip.plan.api.response.PlanResponse;
import com.pplip.domain.trip.plan.usecase.PlanService;
import com.pplip.global.api.code.SuccessCode;
import com.pplip.global.api.response.CommonResponse;
import com.pplip.global.docs.PlanDocsController;
import com.pplip.global.page.Page;
import com.pplip.global.page.PageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

/**
 * 여행 계획 관련 API를 처리하는 컨트롤러
 */
@RestController
@RequestMapping("/trip/plan")
@RequiredArgsConstructor
public class PlanController implements PlanDocsController {

    private final PlanService planService;

    /**
     * 사용자의 모든 여행 계획 리스트를 조회합니다.
     * @param userDetails 로그인한 사용자 정보
     * @return PlanResponse.Summary 여행 계획 요약 정보 리스트
     */
    @Override
    @GetMapping
    public CommonResponse<Page<PlanResponse.Summary>> getPlanList(@ModelAttribute PageRequest pageRequest, @AuthenticationPrincipal UserDetails userDetails) {
        return CommonResponse.success(SuccessCode.SUCCESS, planService.getPlans(pageRequest, userDetails));
    }

    /**
     * 특정 여행 계획의 상세 정보를 조회합니다.
     * @param id 조회할 여행 계획 ID
     * @return PlanResponse.Detail 여행 계획 상세 정보
     */
    @Override
    @GetMapping("/{id}")
    public CommonResponse<PlanResponse.PlanDetail> getPlanDetail(@PathVariable("id") Long id) {
        return CommonResponse.success(SuccessCode.SUCCESS, planService.getPlanDetail(id));
    }

    /**
     * 새로운 여행 계획을 생성합니다.
     * @param request 여행 계획 생성 요청 정보
     * @param userDetails 로그인한 사용자 정보
     * @return PlanResponse.Detail 생성된 여행 계획 상세 정보
     */
    @Override
    @PostMapping
    public CommonResponse<PlanResponse.PlanDetail> postPlan(@RequestBody PlanRequest.Post request, @AuthenticationPrincipal UserDetails userDetails) {
        return CommonResponse.success(SuccessCode.CREATED, planService.createPlan(request, userDetails));
    }

    /**
     * 기존 여행 계획을 수정합니다.
     * @param update 수정할 여행 계획 정보
     * @param id 수정할 여행 계획 ID
     * @return PlanResponse.Update 수정된 여행 계획 정보
     */
    @Override
    @PutMapping("/{id}")
    public CommonResponse<PlanResponse.Update> updatePlan(@RequestBody PlanRequest.Update update, @PathVariable Long id) {
        return CommonResponse.success(SuccessCode.UPDATED, planService.updatePlan(update, id));
    }

    /**
     * 특정 여행 계획을 삭제합니다.
     * @param id 삭제할 여행 계획 ID
     * @return 203
     */
    @Override
    @DeleteMapping("/{id}")
    public CommonResponse<?> deletePlan(@PathVariable Long id) {
        return CommonResponse.success(SuccessCode.REMOVED, planService.removePlan(id));
    }

    @PostMapping("/suggest")
    public CommonResponse<PlanResponse.PlanDetail> suggestPlan(@RequestBody PlanRequest.SuggestPlan suggest){
        return CommonResponse.success(SuccessCode.CREATED, planService.suggestPlan(suggest));
    }

    @PostMapping("/suggest/random")
    public CommonResponse<PlanResponse.PlanDetail> suggestRandomPlan(@RequestBody PlanRequest.RandomPlan suggest){
        return CommonResponse.success(SuccessCode.CREATED, planService.randomPlan(suggest));
    }
}
