package com.pplip.global.docs;

import com.pplip.domain.trip.review.api.request.ReviewRequest;
import com.pplip.domain.trip.review.api.response.ReviewResponse;
import com.pplip.global.api.response.CommonResponse;
import com.pplip.global.page.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 리뷰 관련 엔드포인트 API 문서입니다.
 */
@Tag(name = "관광지 리뷰 API", description = "관광지 리뷰 API")
public interface ReviewDocsController {

    /**
     * 특정 관광지의 리뷰 목록을 가져옵니다.
     *
     * @param attractionId 관광지 ID
     * @return 리뷰 상세 정보 페이지를 담은 CommonResponse
     */
    @Operation(summary = "관광지 리뷰 목록 조회", description = "특정 관광지의 리뷰 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "리뷰 목록 조회 성공")
    CommonResponse<Page<ReviewResponse.Detail>> listReview(Long attractionId);

    /**
     * 특정 관광지에 대한 새 리뷰를 작성합니다.
     *
     * @param request 리뷰 생성 요청
     * @param attractionId 관광지 ID
     * @return 생성된 리뷰의 상세 정보를 담은 CommonResponse
     */
    @Operation(summary = "관광지 리뷰 작성", description = "특정 관광지에 리뷰를 작성합니다.")
    @ApiResponse(responseCode = "201", description = "리뷰 작성 성공")
    CommonResponse<ReviewResponse.Detail> postReview(ReviewRequest.Post request, Long attractionId);

    /**
     * 기존 리뷰를 업데이트합니다.
     *
     * @param update 리뷰 업데이트 요청
     * @param id 업데이트할 리뷰 ID
     * @return 업데이트된 리뷰의 상세 정보를 담은 CommonResponse
     */
    @Operation(summary = "관광지 리뷰 수정", description = "특정 관광지의 리뷰를 수정합니다.")
    @ApiResponse(responseCode = "200", description = "리뷰 수정 성공")
    CommonResponse<ReviewResponse.Update> updateReview(ReviewRequest.Update update, Long id);

    /**
     * 리뷰를 삭제합니다.
     *
     * @param delete 리뷰 삭제 요청
     * @param id 삭제할 리뷰 ID
     * @return 삭제 결과를 나타내는 CommonResponse
     */
    @Operation(summary = "관광지 리뷰 삭제", description = "특정 관광지의 리뷰를 삭제합니다.")
    @ApiResponse(responseCode = "200", description = "리뷰 삭제 성공")
    CommonResponse<?> deleteReview(ReviewRequest.Delete delete, Long id);
}
