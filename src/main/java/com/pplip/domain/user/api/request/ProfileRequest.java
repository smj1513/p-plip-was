package com.pplip.domain.user.api.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class ProfileRequest {
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateInfo {
        private String nickname;
        private String desc;
        private Long imageId;
    }

}
