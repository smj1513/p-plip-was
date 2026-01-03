package com.pplip.domain.trip.plan.usecase.impl;

import com.pplip.domain.auth.utils.SecurityUtils;
import com.pplip.domain.trip.plan.api.request.ToDoRequest;
import com.pplip.domain.trip.plan.api.response.ToDoResponse;
import com.pplip.domain.trip.plan.persistence.dao.PlanDao;
import com.pplip.domain.trip.plan.persistence.dao.TodoDao;
import com.pplip.domain.trip.plan.persistence.entity.Plan;
import com.pplip.domain.trip.plan.persistence.entity.ToDo;
import com.pplip.domain.trip.plan.usecase.ToDoService;
import com.pplip.global.api.code.ErrorCode;
import com.pplip.global.exception.BusinessLogicException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
public class ToDoServiceImpl implements ToDoService {

	private final TodoDao todoDao;
	private final PlanDao planDao;

	@Override
	public List<ToDoResponse.ToDoSummary> getList(Long planId) {
		return todoDao.findAllByPlanId(planId);
	}

	@Override
	public ToDoResponse.ToDoDetail createToDo(List<ToDoRequest.PostTodo> request, Long planId) {
		return null;
	}

	@Override
	public ToDoResponse.ToDoDetail getToDo(Long id) {
		return todoDao.findById(id).orElseThrow(() -> new BusinessLogicException(ErrorCode.PLAN_NOT_FOUND, "여행 세부 일정을 찾을 수 없습니다."));
	}

	@Override
	public ToDoResponse.ToDoUpdated updateToDoList(List<ToDoRequest.Update> update, Long planId) {
		// 1. 기존 DB 데이터 조회
		List<ToDoResponse.ToDoSummary> currentToDoList = todoDao.findAllByPlanId(planId);
		log.info(update);
		// [성능 최적화] 비교를 위해 요청 들어온 ID들을 Set으로 추출 (null 제외)
		Set<Long> requestIds = update.stream()
				.map(ToDoRequest.Update::getId)
				.filter(Objects::nonNull)
				.collect(Collectors.toSet());

		// 2. DELETE 대상: 기존 DB에는 있는데, 요청(Set)에는 없는 것
		List<Long> deleteIds = currentToDoList.stream()
				.map(ToDoResponse.ToDoSummary::getId)
				.filter(id -> !requestIds.contains(id))
				.toList();

		// 3. INSERT 대상: 요청 중 ID가 없는 것 (신규 생성)
		List<ToDo> insertList = update.stream()
				.filter(req -> req.getId() == null)
				.map(req -> ToDo.builder().title(req.getTitle())
						.attractionId(req.getAttractionId())
						.id(req.getId())
						.description(req.getDescription())
						.willStartAt(req.getWillStartAt())
						.willEndAt(req.getWillEndAt())
						.planId(req.getPlanId())
						.createdAt(LocalDateTime.now())
						.build()
				)
				.toList();

		// 4. UPDATE 대상: 요청 중 ID가 있고, 기존 DB 리스트에도 존재하는 것 (검증 포함)
		// (단순히 ID가 있다고 업데이트하는 게 아니라, 실제 DB에 있는 ID인지 확인하는 것이 안전함)
		// 기존 List를 Map으로 변환해두면 확인이 빠름
		Set<Long> currentIds = currentToDoList.stream()
				.map(ToDoResponse.ToDoSummary::getId)
				.collect(Collectors.toSet());

		List<ToDo> updateList = update.stream()
				.filter(req -> req.getId() != null && currentIds.contains(req.getId()))
				.map(req -> ToDo.builder()
						.id(req.getId())
						.attractionId(req.getAttractionId())
						.title(req.getTitle())
						.description(req.getDescription())
						.willStartAt(req.getWillStartAt())
						.willEndAt(req.getWillEndAt())
						.planId(planId)
						.updatedAt(LocalDateTime.now())
						.build())
				.toList();

		// 5. DB 반영
		if (!deleteIds.isEmpty()) {
			log.info("Deleting ToDo IDs: {}", deleteIds);
			todoDao.deleteAll(deleteIds);
		}
		if (!insertList.isEmpty()) {
			log.info("Inserting ToDo items: {}", insertList);
			todoDao.insertAll(insertList);
		}
		if (!updateList.isEmpty()) {
			log.info("Updating ToDo items: {}", updateList);
			todoDao.updateAll(updateList);
		}
		List<ToDoResponse.ToDoSummary> todosList = todoDao.findAllByPlanId(planId);
		if (!todosList.isEmpty()) {
			Plan plan = planDao.findById(planId).orElseThrow(() -> new BusinessLogicException(ErrorCode.PLAN_NOT_FOUND, "여행 계획을 찾을 수 없습니다."));
			log.info("Updating Plan summary info for Plan ID: {}", todosList);
			todosList.sort(Comparator.comparing(ToDoResponse.ToDoSummary::getWillStartAt));
			String thubmnail = todosList.stream().filter(todos -> todos.getAttractionImage() != null && !todos.getAttractionImage().isBlank())
					.map(ToDoResponse.ToDoSummary::getAttractionImage)
					.findFirst()
					.orElse(null);
			plan.setStartDate(todosList.get(0).getWillStartAt().toLocalDate());
			plan.setEndDate(todosList.get(todosList.size() - 1).getWillEndAt().toLocalDate());
			plan.setThumbnail(thubmnail);
			planDao.update(plan);
		}

		return ToDoResponse.ToDoUpdated.builder()
				.toDoItems(todoDao.findAllByPlanId(planId))
				.build();
	}

	@Override
	public Void removeToDo(Long id, UserDetails userDetails) {
		//이후에 사용자 검증 로직 추가.
		int delete = todoDao.delete(id);
		return null;
	}
}
