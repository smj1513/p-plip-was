package com.pplip.domain.user.usecase;

import com.pplip.domain.user.api.request.UserRequest;
import com.pplip.domain.user.api.response.UserResponse;

/**
 * 사용자 관련 비즈니스 로직을 처리하는 서비스 인터페이스
 */
public interface UserService {


	UserResponse.DupCheck nicknameDupCheck(String nickname);

	Void join(UserRequest.Join join);
}
