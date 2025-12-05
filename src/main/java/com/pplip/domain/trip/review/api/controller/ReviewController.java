package com.pplip.domain.trip.review.api.controller;

import com.pplip.domain.trip.review.api.request.ReviewRequest;
import com.pplip.domain.trip.review.api.response.ReviewResponse;
import com.pplip.global.api.code.SuccessCode;
import com.pplip.global.api.response.CommonResponse;
import com.pplip.global.docs.ReviewDocsController;
import com.pplip.global.page.Page;
import org.springframework.web.bind.annotation.*;

/**
 * 여행 리뷰와 관련된 요청을 처리합니다.
 */
@RestController
@RequestMapping("/trip/attraction")
public class ReviewController implements ReviewDocsController {

    /**
     * 특정 관광지의 리뷰 목록을 가져옵니다.
     *
     * @param attractionId 관광지 ID
     * @return 리뷰 상세 정보 페이지를 담은 CommonResponse
     */
    @GetMapping("/{attractionId}/review")
    public CommonResponse<Page<ReviewResponse.Detail>> listReview(@PathVariable Long attractionId) {
        return CommonResponse.success(SuccessCode.SUCCESS, null);
    }

    /**
     * 특정 관광지에 대한 새 리뷰를 작성합니다.
     *
     * @param request 리뷰 생성 요청
     * @param attractionId 관광지 ID
     * @return 생성된 리뷰의 상세 정보를 담은 CommonResponse
     */
    @PostMapping("/{attractionId}/review")
    public CommonResponse<ReviewResponse.Detail> postReview(ReviewRequest.Post request, @PathVariable Long attractionId) {
        return CommonResponse.success(SuccessCode.CREATED, null);
    }

    /**
     * 기존 리뷰를 업데이트합니다.
     *
     * @param update 리뷰 업데이트 요청
     * @param id 업데이트할 리뷰 ID
     * @return 업데이트된 리뷰의 상세 정보를 담은 CommonResponse
     */
    @PutMapping("/review/{id}")
    public CommonResponse<ReviewResponse.Update> updateReview(ReviewRequest.Update update, @PathVariable Long id) {
        return CommonResponse.success(SuccessCode.UPDATED, null);
    }

    /**
     * 리뷰를 삭제합니다.
     *
     * @param delete 리뷰 삭제 요청
     * @param id 삭제할 리뷰 ID
     * @return 삭제 결과를 나타내는 CommonResponse
     */
    @DeleteMapping("/review/{id}")
    public CommonResponse<?> deleteReview(ReviewRequest.Delete delete, @PathVariable Long id) {
        return CommonResponse.success(SuccessCode.REMOVED, null);
    }
}
