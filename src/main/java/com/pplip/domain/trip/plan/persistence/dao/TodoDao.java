package com.pplip.domain.trip.plan.persistence.dao;

import com.pplip.domain.trip.plan.api.response.ToDoResponse;
import com.pplip.domain.trip.plan.persistence.entity.ToDo;
import com.pplip.global.page.PageRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

/**
 * 할 일 데이터에 접근하는 DAO 인터페이스
 */
@Mapper
public interface TodoDao {
	List<ToDoResponse.ToDoSummary> findAllByUserId(Long userId);

	List<ToDoResponse.ToDoSummary> findAllByPlanId(Long planId);

	List<ToDoResponse.ToDoSummary> findAllByPlanIdAndUserId(Long planId, Long userId);

	Optional<ToDoResponse.ToDoDetail> findById(Long id);

	int insert(ToDo toDo);

	int insertAll(List<ToDo> toDoList);

	int update(ToDo toDo);

	int updateAll(List<ToDo> toDoList);

	int delete(Long id);

	void deleteAll(@Param("toDoIds") List<Long> deleteIds);

}
