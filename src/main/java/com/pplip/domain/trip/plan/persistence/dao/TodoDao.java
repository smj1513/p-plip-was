package com.pplip.domain.trip.plan.persistence.dao;

import com.pplip.domain.trip.plan.api.response.ToDoResponse;
import com.pplip.domain.trip.plan.persistence.entity.ToDo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface TodoDao {
    List<ToDoResponse.Summary> findAllByUserId(Long userId);

    List<ToDoResponse.Summary> findAllByPlanId(Long planId);

    Optional<ToDoResponse.Detail> findById(Long id);

    int insert(ToDo toDo);

    int update(ToDo toDo);

    int delete(Long id);
}
