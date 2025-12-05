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

    /**
     * 사용자의 닉네임을 변경합니다.
     *
     * @param nickname  변경할 새 닉네임
     * @param principal 현재 인증된 사용자 정보
     * @return 변경된 닉네임 정보
     */
    @Operation(summary = "닉네임 변경")
    @ApiResponse(responseCode = "200", description = "성공")
    CommonResponse<ProfileResponse.ModifyNickName> modifyNickname(String nickname, @AuthenticationPrincipal UserDetails principal);

    /**
     * 사용자의 프로필 이미지를 수정합니다.
     *
     * @param image     새 프로필 이미지 정보
     * @param principal 현재 인증된 사용자 정보
     * @return 수정된 이미지의 URL
     */
    @Operation(summary = "프로필 이미지 수정")
    @ApiResponse(responseCode = "200", description = "성공")
    CommonResponse<ProfileResponse.ImageUrl> modifyProfileImage(ProfileRequest.Image image, @AuthenticationPrincipal UserDetails principal);
}
