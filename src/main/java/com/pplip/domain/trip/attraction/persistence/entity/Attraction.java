package com.pplip.domain.trip.attraction.persistence.entity;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Attraction {
    private Long no;
    private Long areaCode;
    private Long siGunGuCode;
    private Integer contentId;

    private ContentType contentType;

    private String title;

    private String firstImage1;
    private String firstImage2;
    private String homepage;

    private BigDecimal latitude;
    private BigDecimal longitude;

    private int mapLevel;
    private String overview;
    private String tel;
}
