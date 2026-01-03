package com.pplip.domain.user.usecase;

import com.pplip.domain.user.api.request.ProfileRequest;
import com.pplip.domain.user.api.response.ProfileResponse;
import org.springframework.security.core.userdetails.UserDetails;

public interface ProfileService {

	ProfileResponse.UpdatedInfo modifyInfo(ProfileRequest.UpdateInfo request, UserDetails userDetails);

	ProfileResponse.Info getInfo(UserDetails userDetails);
}
