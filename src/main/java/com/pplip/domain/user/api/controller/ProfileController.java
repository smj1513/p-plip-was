package com.pplip.domain.user.api.controller;

import com.pplip.domain.user.api.request.ProfileRequest;
import com.pplip.domain.user.api.response.ProfileResponse;
import com.pplip.domain.user.usecase.ProfileService;
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
	 * 사용자의 정보를 변경합니다.
	 *
	 * @param request 변경할 정보
	 * @param principal 현재 인증된 사용자 정보
	 * @return 변경된 정보
	 */

	private final ProfileService profileService;


	@PatchMapping("/info")
	@Override
	public CommonResponse<ProfileResponse.UpdatedInfo> modifyNickname(@RequestBody ProfileRequest.UpdateInfo req,
	                                                                  @AuthenticationPrincipal UserDetails principal) {
		return CommonResponse.success(SuccessCode.UPDATED, profileService.modifyInfo(req, principal));
	}

	@GetMapping("/info")
	public CommonResponse<ProfileResponse.Info> getInfo(@AuthenticationPrincipal UserDetails userDetails){
		return CommonResponse.success(SuccessCode.SUCCESS, profileService.getInfo(userDetails));
	}


}
