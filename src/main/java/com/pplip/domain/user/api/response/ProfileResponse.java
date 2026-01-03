package com.pplip.domain.user.api.response;

import com.pplip.domain.file.api.response.FileResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

public class ProfileResponse {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UpdatedInfo {
        private String nickname;
        private String desc;
        private FileResponse profileImage;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ImageUrl {
        private String url;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Info {
        private String name;
        private String nickname;
        private String email;
        private LocalDate birth;
        private String desc;
        private FileResponse profileImage;
    }
}
