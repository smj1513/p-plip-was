package com.pplip.global.docs;

import com.pplip.domain.trip.plan.api.request.ToDoRequest;
import com.pplip.domain.trip.plan.api.response.ToDoResponse;
import com.pplip.global.api.response.CommonResponse;
import com.pplip.global.page.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

@Tag(name = "여행 TODO API", description = "여행 TODO API")
public interface ToDoDocsController {

    /**
     * 유저의 TODO 리스트를 조회합니다.
     *
     * @param userDetails 유저 정보
     * @return 유저의 TODO 리스트
     */
    @Operation(summary = "유저종속 TODO 페이징 조회")
    @ApiResponse(responseCode = "200", description = "성공")
    CommonResponse<Page<ToDoResponse.Summary>> listToDo(UserDetails userDetails);

    /**
     * 계획의 TODO 리스트를 조회합니다.
     *
     * @param planId 계획 아이디
     * @return 계획의 TODO 리스트
     */
    @Operation(summary = "계획종속 TODO 페이징 조회")
    @ApiResponse(responseCode = "200", description = "성공")
    CommonResponse<List<ToDoResponse.Summary>> listToDo(Long planId);

    /**
     * TODO를 생성합니다.
     *
     * @param request 생성할 TODO 정보
     * @param planId 계획 아이디
     * @return 생성된 TODO 정보
     */
    @Operation(summary = "TODO 생성")
    @ApiResponse(responseCode = "201", description = "생성")
    CommonResponse<ToDoResponse.Detail> listToDo(List<ToDoRequest.Post> request, Long planId);

    /**
     * TODO를 단건 조회합니다.
     *
     * @param id TODO 아이디
     * @return TODO 정보
     */
    @Operation(summary = "TODO 단건 조회")
    @ApiResponse(responseCode = "200", description = "성공")
    CommonResponse<ToDoResponse.Detail> getToDo(Long id);

    /**
     * TODO를 수정합니다.
     *
     * @param update 수정할 TODO 정보
     * @param planId 계획 아이디
     * @return 수정된 TODO 정보
     */
    @Operation(summary = "TODO 수정")
    @ApiResponse(responseCode = "202", description = "수정")
    CommonResponse<ToDoResponse.Update> updateToDo(List<ToDoRequest.Update> update, Long planId);

    /**
     * TODO를 삭제합니다.
     *
     * @param id TODO 아이디
     * @return 203
     */
    @Operation(summary = "TODO 삭제")
    @ApiResponse(responseCode = "203", description = "삭제")
    CommonResponse<?> deleteToDo(Long id);
}
