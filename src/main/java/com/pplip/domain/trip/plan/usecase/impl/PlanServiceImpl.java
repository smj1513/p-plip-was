package com.pplip.domain.trip.plan.usecase.impl;

import com.pplip.domain.auth.persistence.entity.Account;
import com.pplip.domain.auth.utils.SecurityUtils;
import com.pplip.domain.trip.ai.dto.response.AiResponse;
import com.pplip.domain.trip.ai.service.InferenceService;
import com.pplip.domain.trip.attraction.api.response.AttractionResponse;
import com.pplip.domain.trip.attraction.persistence.dao.AttractionDao;
import com.pplip.domain.trip.attraction.persistence.dao.SidoGugunsDao;
import com.pplip.domain.trip.attraction.persistence.entity.Attraction;
import com.pplip.domain.trip.plan.api.request.PlanRequest;
import com.pplip.domain.trip.plan.api.response.PlanResponse;
import com.pplip.domain.trip.plan.persistence.dao.PlanDao;
import com.pplip.domain.trip.plan.persistence.dao.TodoDao;
import com.pplip.domain.trip.plan.persistence.entity.Plan;
import com.pplip.domain.trip.plan.persistence.entity.ToDo;
import com.pplip.domain.trip.plan.usecase.PlanService;
import com.pplip.global.api.code.ErrorCode;
import com.pplip.global.exception.BusinessLogicException;
import com.pplip.global.page.Page;
import com.pplip.global.page.PageRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PlanServiceImpl implements PlanService {

	private final PlanDao planDao;
	private final InferenceService inferenceService;
	private final TodoDao todoDao;
	private final AttractionDao attractionDao;
	private final SidoGugunsDao sidoGugunsDao;
	private final Cache planCache;


	@Override
	public Page<PlanResponse.Summary> getPlans(PageRequest pageRequest, UserDetails userDetails) {
		Long userId = SecurityUtils.resolveUserId(userDetails);
		List<PlanResponse.Summary> all = planDao.findAll(pageRequest, userId);
		int count = planDao.count(userId);
		return new Page<>(all, pageRequest.getPageNum(), pageRequest.getPageSize(), count);
	}

	@Override
	public PlanResponse.PlanDetail getPlanDetail(Long id) {
		Account loginUser = SecurityUtils.getCurrentUser();
		PlanResponse.PlanDetail planDetail = planDao.findByIdToDto(id).orElseThrow(() -> new BusinessLogicException(ErrorCode.PLAN_NOT_FOUND));
		if (!planDetail.getUserId().equals(loginUser.getUserId())) {
			throw new BusinessLogicException(ErrorCode.FORBIDDEN, "작성자만 조회 할 수 있습니다.");
		}
		return planDetail;
	}

	@Override
	public PlanResponse.PlanDetail createPlan(PlanRequest.Post request, UserDetails userDetails) {
		Long userId = SecurityUtils.resolveUserId(userDetails);
		Plan plan = Plan.builder()
				.title(request.getTitle())
				.userId(userId)
				.createdAt(LocalDateTime.now()).build();
		planDao.insert(plan);
		return planDao.findByIdToDto(plan.getId()).orElseThrow(() -> new BusinessLogicException(ErrorCode.PLAN_NOT_FOUND, "플랜 저장에 실패했습니다."));
	}

	@Override
	public PlanResponse.Update updatePlan(PlanRequest.Update update, Long id) {
		Account currentUser = SecurityUtils.getCurrentUser();
		Plan plan = planDao.findById(id).orElseThrow(() -> new BusinessLogicException(ErrorCode.PLAN_NOT_FOUND));
		if (!currentUser.getUserId().equals(plan.getUserId())) {
			throw new BusinessLogicException(ErrorCode.FORBIDDEN, "작성자만 수정할 수 있습니다.");
		}
		plan.setTitle(update.getTitle());
		plan.setUpdatedAt(LocalDateTime.now());
		planDao.update(plan);
		return PlanResponse.Update.builder().id(plan.getId())
				.title(plan.getTitle())
				.startDate(plan.getStartDate())
				.endDate(plan.getEndDate())
				.updatedAt(plan.getUpdatedAt()).build();
	}

	@Override
	public PlanResponse.Remove removePlan(Long id) {
		Account currentUser = SecurityUtils.getCurrentUser();
		Plan plan = planDao.findById(id).orElseThrow(() -> new BusinessLogicException(ErrorCode.PLAN_NOT_FOUND));
		if (!plan.getUserId().equals(currentUser.getUserId())) {
			throw new BusinessLogicException(ErrorCode.FORBIDDEN, "작성자만 삭제할 수 있습니다.");
		}
		int delete = planDao.delete(id);
		if (delete < 1) {
			throw new BusinessLogicException(ErrorCode.PLAN_PROCESS_FAIL, "삭제에 실패했습니다.");
		}
		return PlanResponse.Remove.builder().id(plan.getId()).title(plan.getTitle()).build();
	}

	@Override
	public PlanResponse.PlanDetail suggestPlan(PlanRequest.SuggestPlan suggest) {
		Long userId = SecurityUtils.getCurrentUser().getUserId();
		if (planCache.get(userId) == null) {
			planCache.putIfAbsent(userId, true);
		} else {
			throw new BusinessLogicException(ErrorCode.PLAN_PROCESS_FAIL, "현재 플랜 생성이 진행중입니다. 잠시 후 다시 시도해주세요.");
		}
		AiResponse.SuggestPlan suggestPlan = inferenceService.suggestPlan(suggest);
		if(planCache.evictIfPresent(userId)){
			log.info("evict plan cache for userId: {}", userId);
		}
		return savePlans(suggestPlan, Long.parseLong(suggest.getAttractionId()));
	}

	@Override
	public PlanResponse.PlanDetail randomPlan(PlanRequest.RandomPlan suggest) {
		log.info("suggest : {}", suggest);
		Attraction random = attractionDao.findRandom(suggest.getSidoCode(), suggest.getGugunCode());
		log.info("random : {}", random);
		AiResponse.SuggestPlan suggestPlan = inferenceService.suggestPlan(PlanRequest.SuggestPlan.builder()
				.query(suggest.getRegionName() + "에서 즐기는 즉흥 여행")
				.attractionId(String.valueOf(random.getNo()))
				.startDate(suggest.getStartDate())
				.endDate(suggest.getEndDate())
				.build()
		);
		return savePlans(suggestPlan, random.getNo());
	}

	private PlanResponse.PlanDetail savePlans(AiResponse.SuggestPlan suggestPlan, Long mainAttractionId) {
		Account currentUser = SecurityUtils.getCurrentUser();

		AttractionResponse.Details details = attractionDao.findByNo(mainAttractionId).orElseThrow(() -> new BusinessLogicException(ErrorCode.ATTRACTION_NOT_FOUND, "요청한 관광지를 찾을 수 없습니다."));
		String thumbnail = details.getFirstImage1();

		log.info("suggestPlan: {}", suggestPlan);

		Plan plan = Plan.builder().title(suggestPlan.getTitle())
				.createdAt(LocalDateTime.now())
				.startDate(suggestPlan.getStartDate())
				.endDate(suggestPlan.getEndDate())
				.userId(currentUser.getUserId()).build();
		if (thumbnail == null || thumbnail.isBlank()) {
			thumbnail = suggestPlan.getToDos().stream().map(toDoItem -> toDoItem.getAttraction().getFirstImage1())
					.filter(img -> img != null && !img.isBlank())
					.findFirst()
					.orElse(null);
		}
		plan.setThumbnail(thumbnail);
		planDao.insert(plan);
		List<ToDo> toDoList = suggestPlan.getToDos().stream().map(toDoItem -> {
			return ToDo.builder().planId(plan.getId())
					.attractionId(toDoItem.getAttraction().getId())
					.title(toDoItem.getName())
					.description(toDoItem.getDetailPlanDesc())
					.willStartAt(toDoItem.getStartAt())
					.willEndAt(toDoItem.getEndAt())
					.createdAt(LocalDateTime.now())
					.build();
		}).toList();
		todoDao.insertAll(toDoList);


		return PlanResponse.PlanDetail.builder()
				.id(plan.getId())
				.title(plan.getTitle())
				.createdAt(plan.getCreatedAt())
				.startDate(plan.getStartDate())
				.endDate(plan.getEndDate())
				.userId(currentUser.getUserId())
				.thumbnail(thumbnail)
				.build();
	}
}
