package com.pplip.domain.trip.attraction.api.request;

import com.pplip.domain.trip.attraction.persistence.entity.ContentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

public class AttractionRequest {
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Suggest{
        private String query;
        private BigDecimal lat;
        private BigDecimal lng;
    }

}
