package com.pplip.domain.trip.attraction.persistence.dao;

import com.pplip.domain.trip.attraction.api.response.AttractionResponse;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SidoGugunsDao {

    List<AttractionResponse.Region> findAllRegion();

    List<AttractionResponse.Sido> findAllSido();

    List<AttractionResponse.Gugun> findAllGugun();

    List<AttractionResponse.Gugun> findAllGugunInSido(Long sidoCode);
}
