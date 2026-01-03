package com.pplip.global.docs;

import com.pplip.domain.user.api.request.ProfileRequest;
import com.pplip.domain.user.api.response.ProfileResponse;
import com.pplip.global.api.response.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 사용자 프로필 관련 API 명세를 정의하는 인터페이스
 */
@Tag(name = "프로필 API", description = "사용자 프로필 api")
public interface ProfileDocsController {

    @Operation(summary = "사용자 프로필 수정")
    @ApiResponse(responseCode = "200", description = "성공")
    CommonResponse<ProfileResponse.UpdatedInfo> modifyNickname(ProfileRequest.UpdateInfo request, @AuthenticationPrincipal UserDetails principal);

}
