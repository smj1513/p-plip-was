package com.pplip.domain.user.api.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class UserResponse {
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class DupCheck{
        private boolean isDup;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EmailCheck {
        private boolean isSuccess;
        private String message;
    }
}
