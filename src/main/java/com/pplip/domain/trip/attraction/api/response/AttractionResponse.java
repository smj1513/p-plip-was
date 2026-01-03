package com.pplip.domain.trip.attraction.api.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pplip.domain.trip.attraction.persistence.entity.ContentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

public class AttractionResponse {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Region {
        private Sido sido;
        private List<Gugun> guguns;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Sido {
        private Integer sidoCode;
        private String sidoName;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Gugun {
        private Integer gugunCode;
        private String gugunName;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Summary {
        private Long no;

        @JsonIgnore
        private Long contentTypeId;
        private ContentType contentType;

        private String title;

        private String firstImage1;
        private String firstImage2;
        private String homepage;

        private String addr1;
        private String addr2;

        private BigDecimal latitude;
        private BigDecimal longitude;
	}


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Details{
        private Long no;

        @JsonIgnore
        private Long contentTypeId;

        private ContentType contentType;

        private String title;

        private String firstImage1;
        private String firstImage2;

        private BigDecimal latitude;
        private BigDecimal longitude;

        private String addr1;
        private String addr2;

        private int mapLevel;
        private String overview;
        private String tel;

        private List<String> tagNames;

        private int reviewCnt;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
	public static class NearByAttraction {
	    private Long no;
        private String title;
        private String thumbnail;
        private int reviewCnt;
        private String overview;
        private float distance;
    }
}
