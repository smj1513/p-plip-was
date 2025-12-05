package com.pplip.domain.user.api.controller;

import com.pplip.domain.user.api.request.ProfileRequest;
import com.pplip.domain.user.api.response.ProfileResponse;
import com.pplip.global.api.code.SuccessCode;
import com.pplip.global.api.response.CommonResponse;
import com.pplip.global.docs.ProfileDocsController;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

/**
 * 사용자 프로필 관련 API 요청을 처리하는 컨트롤러
 */
@RestController
@RequestMapping("/user/profile")
@RequiredArgsConstructor
public class ProfileController implements ProfileDocsController {

    /**
     * 사용자의 닉네임을 변경합니다.
     *
     * @param nickname  변경할 새 닉네임
     * @param principal 현재 인증된 사용자 정보
     * @return 변경된 닉네임 정보
     */
    @PatchMapping("/nickname")
    @Override
    public CommonResponse<ProfileResponse.ModifyNickName> modifyNickname(@RequestParam String nickname,
                                                                         @AuthenticationPrincipal UserDetails principal){
        return CommonResponse.success(SuccessCode.UPDATED, null);
    }

    /**
     * 사용자의 프로필 이미지를 수정합니다.
     *
     * @param image     새 프로필 이미지 정보
     * @param principal 현재 인증된 사용자 정보
     * @return 수정된 이미지의 URL
     */
    @PatchMapping("/image")
    @Override
    public CommonResponse<ProfileResponse.ImageUrl> modifyProfileImage(@RequestBody ProfileRequest.Image image,
                                                                   @AuthenticationPrincipal UserDetails principal) {
        return CommonResponse.success(SuccessCode.UPDATED, null);
    }
}
