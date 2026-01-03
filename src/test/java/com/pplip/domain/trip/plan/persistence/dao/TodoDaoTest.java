package com.pplip.domain.trip.plan.persistence.dao;

import com.pplip.domain.trip.plan.api.response.ToDoResponse;
import com.pplip.domain.trip.plan.persistence.entity.Plan;
import com.pplip.domain.trip.plan.persistence.entity.ToDo;
import com.pplip.domain.user.persistence.dao.UserDao;
import com.pplip.domain.user.persistence.entity.User;
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
class TodoDaoTest {

    @Autowired
    UserDao userDao;

    @Autowired
    TodoDao dao;

    @Autowired
    PlanDao planDao; // To create a plan first

    private Long planId;
    private Long userId;

    @BeforeEach
    void setUp() {
        User test = User.builder()
                .name("test")
                .birth(LocalDate.now())
                .build();
        userDao.insert(test);

        Plan plan = Plan.builder().userId(test.getId()).title("Test Plan")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(5))
                .createdAt(LocalDateTime.now())
                .build();
        planDao.insert(plan);
        planId = plan.getId();
        userId = test.getId();
    }
    // Issue Resolved: MyBatis useGeneratedKeys 설정 누락으로 인한 ID null 문제 해결 (2025-12-06)
    @Test
    @DisplayName("성공: 새 ToDo를 저장한 후 ID로 조회할 수 있다")
    void insertAndFindById_Success() {
        // given
        ToDo todo = ToDo.builder()
                .planId(planId)
                .description("경복궁 가기")
                .attractionId(56647L)
                .willStartAt(LocalDateTime.now())
                .willEndAt(LocalDateTime.now().plusDays(5))
                .createdAt(LocalDateTime.now())
                .build();
        dao.insert(todo);

        System.out.println("Gen key" + todo.getId());

        // when
        Optional<ToDoResponse.ToDoDetail> foundTodoOptional = dao.findById(todo.getId());

        // then
        assertThat(foundTodoOptional).isPresent();
        assertThat(foundTodoOptional.get().getDescription()).isEqualTo("경복궁 가기");
    }

    @Test
    @DisplayName("실패: 존재하지 않는 ID로 조회 시 빈 Optional을 반환한다")
    void findById_Fail_WhenTodoDoesNotExist() {
        // given
        long nonExistentId = 999L;

        // when
        Optional<ToDoResponse.ToDoDetail> detail = dao.findById(nonExistentId);

        // then
        assertThat(detail).isNotPresent();
    }



    @Test
    @DisplayName("성공: 특정 여행 계획에 속한 모든 ToDo를 조회한다")
    void findAllByPlanId_Success() {
        // given
        dao.insert(ToDo.builder().planId(planId).description("t1").createdAt(LocalDateTime.now()).attractionId(56647L).build());
        dao.insert(ToDo.builder().planId(planId).description("t2").createdAt(LocalDateTime.now()).attractionId(56647L).build());

        // when
        List<ToDoResponse.ToDoSummary> allByPlanId = dao.findAllByPlanId(planId);

        // then
        assertThat(allByPlanId).isNotNull();
        assertThat(allByPlanId.size()).isGreaterThanOrEqualTo(2);
    }
}