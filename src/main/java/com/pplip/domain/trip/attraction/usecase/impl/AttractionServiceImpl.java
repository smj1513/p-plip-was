package com.pplip.domain.trip.attraction.usecase.impl;

import com.pplip.domain.auth.utils.SecurityUtils;
import com.pplip.domain.trip.ai.dto.request.AiRequest;
import com.pplip.domain.trip.ai.dto.response.AiResponse;
import com.pplip.domain.trip.ai.service.InferenceService;
import com.pplip.domain.trip.attraction.api.request.AttractionRequest;
import com.pplip.domain.trip.attraction.api.response.AttractionResponse;
import com.pplip.domain.trip.attraction.persistence.dao.AttractionDao;
import com.pplip.domain.trip.attraction.persistence.dao.TagDao;
import com.pplip.domain.trip.attraction.persistence.entity.ContentType;
import com.pplip.domain.trip.attraction.usecase.AttractionService;
import com.pplip.domain.trip.attraction.usecase.SearchHistoryService;
import com.pplip.domain.trip.attraction.usecase.SidoGugunsService;
import com.pplip.global.api.code.ErrorCode;
import com.pplip.global.exception.BusinessLogicException;
import com.pplip.global.page.Page;
import com.pplip.global.page.PageRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AttractionServiceImpl implements AttractionService {

	private final AttractionDao attractionDao;
	private final TagDao tagDao;

	private final InferenceService inferenceService;
	private final SearchHistoryService historyService;
	private final SidoGugunsService sidoGugunsService;
	private final Cache attractionCache;


	public Page<AttractionResponse.Summary> findAllBySearch(AttractionRequest.Search search, PageRequest pageRequest) {
		List<AttractionResponse.Summary> resData = attractionDao.findAllBySearch(search, pageRequest);
		resData.forEach(data -> data.setContentType(ContentType.getContentType(data.getContentTypeId().intValue())));

		if (!SecurityUtils.isAnonymous() && !search.getQuery().isEmpty()) {
			historyService.post(search, SecurityUtils.getCurrentUser());
		}
		return new Page<>(resData, pageRequest.getPageNum(), pageRequest.getPageSize(), attractionDao.countAllBySearch(search));
	}

	@Override
	public List<AiResponse.SuggestAttraction> suggestAttractions(AttractionRequest.Suggest suggest) {
		Long userId = SecurityUtils.getCurrentUser().getUserId();
		synchronized (attractionCache) {
			if (attractionCache.get(userId) == null) {
				attractionCache.putIfAbsent(userId, true);
			} else {
				throw new BusinessLogicException(ErrorCode.ATTRACTION_REQUEST_BLOCKING_ERROR, "이미 생성 중 입니다.");
			}
		}
		List<AiResponse.SuggestAttraction> response = inferenceService.suggestAttraction(AiRequest.SuggestAttractions.builder()
				.lat(suggest.getLat())
				.lng(suggest.getLng())
				.query(suggest.getQuery())
				.m(suggest.getM())
				.k(suggest.getK())
				.contentTypes(suggest.getContentTypes() == null ? null : suggest.getContentTypes().stream().map(ContentType::getDescription).toList())
				.build());
		if(attractionCache.evictIfPresent(userId)){
			log.info("attraction_cache_evicted:{}", userId);
		}
		return response;
	}

	@Override
	public AttractionResponse.Details findByNo(Long no) {
		AttractionResponse.Details resData = attractionDao.findByNo(no)
				.orElseThrow(() -> new BusinessLogicException(ErrorCode.ATTRACTION_NOT_FOUND, "장소의 정보가 존재하지 않습니다."));

		resData.setTagNames(tagDao.findAllByAttractionNo(no).stream().map(tag -> tag.getName()).toList());
		resData.setContentType(ContentType.getContentType(resData.getContentTypeId().intValue()));

		return resData;
	}

	@Override
	public List<AiResponse.SuggestAttraction> suggestAttractionsBySidoGuguns(AttractionRequest.SuggestBySidoGuguns suggest) {
		sidoGugunsService.validSidoGuguns(suggest.getSidoCode(), suggest.getGugunCode());

		AttractionResponse.Details data = attractionDao.findRandomFirstBySidoGuguns(suggest)
				.orElseThrow(() -> new BusinessLogicException(ErrorCode.ATTRACTION_NOT_FOUND, "해당 지역의 ATTRACTION을 찾을 수 없습니다."));

		return null;
	}

	@Override
	public List<AttractionResponse.NearByAttraction> getMainAttractions(Double latitude, Double longitude) {
		return attractionDao.findMainAttractions(latitude, longitude);
	}
}
