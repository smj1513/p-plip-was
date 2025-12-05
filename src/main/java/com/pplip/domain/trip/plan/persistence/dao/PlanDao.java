package com.pplip.domain.trip.plan.persistence.dao;

import com.pplip.domain.trip.plan.api.response.PlanResponse;
import com.pplip.domain.trip.plan.persistence.entity.Plan;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface PlanDao {
    List<PlanResponse.Summary> findAll(Long userId);

    Optional<PlanResponse.Detail> findById(Long id);

    int insert(Plan plan);

    int update(Plan plan);

    int delete(Long id);
}
