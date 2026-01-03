package com.pplip.domain.trip.attraction.usecase;

import com.pplip.domain.trip.ai.dto.response.AiResponse;
import com.pplip.domain.trip.attraction.api.request.AttractionRequest;
import com.pplip.domain.trip.attraction.api.response.AttractionResponse;
import com.pplip.global.page.Page;
import com.pplip.global.page.PageRequest;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AttractionService {
    Page<AttractionResponse.Summary> findAllBySearch(AttractionRequest.Search search, PageRequest pageRequest);

    List<AiResponse.SuggestAttraction> suggestAttractions(AttractionRequest.Suggest suggest);

    AttractionResponse.Details findByNo(Long no);

    List<AiResponse.SuggestAttraction> suggestAttractionsBySidoGuguns(AttractionRequest.SuggestBySidoGuguns suggest);

	List<AttractionResponse.NearByAttraction> getMainAttractions( Double latitude, Double longitude);
}
