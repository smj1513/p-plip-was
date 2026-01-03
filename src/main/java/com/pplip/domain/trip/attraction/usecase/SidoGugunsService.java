package com.pplip.domain.trip.attraction.usecase;

import com.pplip.domain.trip.attraction.api.response.AttractionResponse;

import java.util.List;

public interface SidoGugunsService {
    void validSidoGuguns(Integer sidoCode, Integer gugunCode);

    List<AttractionResponse.Region> findAllRegion();
}
