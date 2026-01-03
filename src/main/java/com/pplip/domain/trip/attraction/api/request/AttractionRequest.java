package com.pplip.domain.trip.attraction.api.request;

import com.pplip.domain.trip.attraction.persistence.entity.ContentType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

public class AttractionRequest {
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Suggest{
        private String query;
        private BigDecimal lat;
        private BigDecimal lng;
        // 관광지, 문화시설, 축제공연행사, 여행코스, 레포츠, 숙박, 쇼핑, 식당
        private List<ContentType> contentTypes;
        // default = 1000m
        @Builder.Default
        private Integer m = 1000;
        @Builder.Default
        private Integer k = 10;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Search {
        private String query;
        // 관광지, 문화시설, 축제공연행사, 여행코스, 레포츠, 숙박, 쇼핑, 식당
        private List<ContentType> contentTypes;
        private BigDecimal lat;
        private BigDecimal lng;

        // default = 1000m
        private Integer m = 1000;

        private int pageNum;
        private int pageSize;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SuggestBySidoGuguns {
        @NotNull
        private Integer sidoCode;
        private Integer gugunCode;
    }
}
