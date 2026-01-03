package com.pplip.domain.trip.plan.persistence.dao;

import com.pplip.domain.trip.plan.api.response.PlanResponse;
import com.pplip.domain.trip.plan.persistence.entity.Plan;
import com.pplip.domain.user.persistence.dao.UserDao;
import com.pplip.domain.user.persistence.entity.User;
import com.pplip.global.page.PageRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@MybatisTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PlanDaoTest {

    @Autowired
    PlanDao dao;

    @Autowired
    UserDao userDao;

    private Long userId;

    @BeforeEach
    void setUp() {
        User user = User.builder()
                .name("testuser")
                .birth(LocalDate.of(1990, 1, 1))
                .build();
        userDao.insert(user);
        userId = user.getId();
    }

    @Test
    @DisplayName("성공: 새 여행 계획을 저장한 후 ID로 조회할 수 있다")
    void insertAndFindById_ToDto_Success() {
        // given
        Plan plan = Plan.builder()
                .userId(userId)
                .title("서울 여행")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(3))
                .createdAt(LocalDateTime.now())
                .build();
        dao.insert(plan);

        // when
        Optional<PlanResponse.PlanDetail> foundPlanOptional = dao.findByIdToDto(plan.getId());

        // then
        assertThat(foundPlanOptional).isPresent();
        PlanResponse.PlanDetail foundPlan = foundPlanOptional.get();
        assertThat(foundPlan.getTitle()).isEqualTo("서울 여행");
    }

    @Test
    @DisplayName("실패: 존재하지 않는 ID로 조회 시 빈 Optional을 반환한다")
    void findById_ToDto_Fail_WhenPlanDoesNotExist() {
        // given
        long nonExistentId = 999L;

        // when
        Optional<PlanResponse.PlanDetail> detail = dao.findByIdToDto(nonExistentId);

        // then
        assertThat(detail).isNotPresent();
    }

    @Test
    @DisplayName("성공: 특정 사용자의 모든 여행 계획을 조회한다")
    void findAll_Success() {
        // given
        dao.insert(Plan.builder().userId(userId).title("p1").startDate(LocalDate.now()).endDate(LocalDate.now().plusDays(1)).createdAt(LocalDateTime.now()).build());
        dao.insert(Plan.builder().userId(userId).title("p2").startDate(LocalDate.now()).endDate(LocalDate.now().plusDays(1)).createdAt(LocalDateTime.now()).build());

        PageRequest pageRequest = new PageRequest(0, 10);

        // when
        List<PlanResponse.Summary> all = dao.findAll(pageRequest, userId);

        // then
        assertThat(all).isNotNull();
        assertThat(all.size()).isGreaterThanOrEqualTo(2);
    }
}