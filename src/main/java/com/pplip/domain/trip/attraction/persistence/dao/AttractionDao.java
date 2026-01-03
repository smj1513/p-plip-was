package com.pplip.domain.trip.attraction.persistence.dao;

import com.pplip.domain.trip.attraction.api.request.AttractionRequest;
import com.pplip.domain.trip.attraction.api.response.AttractionResponse;
import com.pplip.domain.trip.attraction.persistence.entity.Attraction;
import com.pplip.global.page.PageRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

/**
 * 여행지 데이터에 접근하는 DAO 인터페이스
 */
@Mapper
public interface AttractionDao {

    /**
     * https://gemini.google.com/share/baed178b13cf
     *
     * mysql geolocation 검색 방법
     * SELECT id, name, lat, lng,
     *        ST_Distance_Sphere(
     *            POINT(lng, lat),             -- 저장된 컬럼 (순서 주의: 경도, 위도)
     *            POINT(57.2342, 27.2345)      -- 기준 위치 (순서 주의: 경도, 위도)
     *        ) AS distance_in_meters
     * FROM places
     * WHERE ST_Distance_Sphere(
     *            POINT(lng, lat),
     *            POINT(57.2342, 27.2345)
     *        ) <= 5000  -- 5km (5000m) 이내
     * ORDER BY distance_in_meters;
     */

    List<AttractionResponse.Summary> findAllBySearch(AttractionRequest.Search search, PageRequest pageRequest);

    Optional<AttractionResponse.Details> findByNo(Long no);

    List<AttractionResponse.Summary> findAllByNos(List<Long> nos);

    int insert(Attraction attraction);

    int countAllBySearch(AttractionRequest.Search search);

    Optional<AttractionResponse.Details> findRandomFirstBySidoGuguns(AttractionRequest.SuggestBySidoGuguns search);

    Attraction findRandom(@Param("sidoCode") int sidoCode, @Param("gugunCode") int gugunCode);

    List<AttractionResponse.NearByAttraction> findMainAttractions(@Param("latitude") Double latitude, @Param("longitude") Double longitude);
}
