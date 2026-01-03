package com.pplip.domain.trip.plan.usecase.impl;

import com.pplip.domain.auth.persistence.entity.Account;
import com.pplip.domain.auth.persistence.entity.Role;
import com.pplip.domain.auth.utils.SecurityUtils;
import com.pplip.domain.trip.plan.api.request.PlanRequest;
import com.pplip.domain.trip.plan.api.response.PlanResponse;
import com.pplip.domain.trip.plan.persistence.dao.PlanDao;
import com.pplip.domain.trip.plan.persistence.entity.Plan;
import com.pplip.global.api.code.ErrorCode;
import com.pplip.global.exception.BusinessLogicException;
import com.pplip.global.page.Page;
import com.pplip.global.page.PageRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
@DisplayName("PlanServiceImpl 테스트")
class PlanServiceImplTest {

    @InjectMocks
    private PlanServiceImpl planService;

    @Mock
    private PlanDao planDao;

    private Account testUser;

    @BeforeEach
    void setUp() {
        testUser = Account.builder()
                .userId(1L)
                .email("test@test.com")
                .role(Role.USER)
                .build();
    }

    @Nested
    @DisplayName("getPlans 메소드 테스트")
    class GetPlansTest {
        @Test
        @DisplayName("성공: 사용자의 여행 계획 목록을 정상적으로 조회한다")
        void getPlans_success() {
            // given
            PageRequest pageRequest = new PageRequest(1, 10);
            List<PlanResponse.Summary> summaries = List.of(new PlanResponse.Summary());
            when(planDao.findAll(pageRequest, testUser.getUserId())).thenReturn(summaries);
            when(planDao.count(testUser.getUserId())).thenReturn(1);

            // when
            Page<PlanResponse.Summary> result = planService.getPlans(pageRequest, testUser);

            // then
            assertThat(result).isNotNull();
            assertThat(result.getList()).isEqualTo(summaries);
            assertThat(result.getTotalCount()).isEqualTo(1);
        }

        @Test
        @DisplayName("성공: 여행 계획이 없는 경우 빈 목록을 반환한다")
        void getPlans_empty() {
            // given
            PageRequest pageRequest = new PageRequest(1, 10);
            when(planDao.findAll(pageRequest, testUser.getUserId())).thenReturn(List.of());
            when(planDao.count(testUser.getUserId())).thenReturn(0);

            // when
            Page<PlanResponse.Summary> result = planService.getPlans(pageRequest, testUser);

            // then
            assertThat(result).isNotNull();
            assertThat(result.getList()).isEmpty();
            assertThat(result.getTotalCount()).isEqualTo(0);
        }
    }


    @Nested
    @DisplayName("getPlanDetail 메소드 테스트")
    class GetPlanPlanToDoDetailTest {
        @Test
        @DisplayName("성공: 특정 여행 계획의 상세 정보를 조회한다")
        void getPlanDetail_success() {
            try (MockedStatic<SecurityUtils> mockedSecurity = mockStatic(SecurityUtils.class)) {
                // given
                Long planId = 1L;
                PlanResponse.PlanDetail planDetail = PlanResponse.PlanDetail.builder().id(planId).userId(testUser.getUserId()).title("테스트 계획").build();
                mockedSecurity.when(SecurityUtils::getCurrentUser).thenReturn(testUser);
                when(planDao.findByIdToDto(planId)).thenReturn(Optional.of(planDetail));

                // when
                PlanResponse.PlanDetail result = planService.getPlanDetail(planId);

                // then
                assertThat(result).isNotNull();
                assertThat(result.getId()).isEqualTo(planId);
                assertThat(result.getUserId()).isEqualTo(testUser.getUserId());
            }
        }

        @Test
        @DisplayName("실패: 존재하지 않는 계획 ID로 조회 시 예외 발생")
        void getPlanDetail_notFound() {
            try (MockedStatic<SecurityUtils> mockedSecurity = mockStatic(SecurityUtils.class)) {
                // given
                Long nonExistPlanId = 99L;
                mockedSecurity.when(SecurityUtils::getCurrentUser).thenReturn(testUser);
                when(planDao.findByIdToDto(nonExistPlanId)).thenReturn(Optional.empty());

                // when & then
                BusinessLogicException exception = assertThrows(BusinessLogicException.class, () -> planService.getPlanDetail(nonExistPlanId));
                assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.PLAN_NOT_FOUND);
            }
        }

        @Test
        @DisplayName("실패: 다른 사용자의 계획 조회 시 예외 발생")
        void getPlanDetail_forbidden() {
            try (MockedStatic<SecurityUtils> mockedSecurity = mockStatic(SecurityUtils.class)) {
                // given
                Long planId = 1L;
                Long otherUserId = 2L;
                PlanResponse.PlanDetail planDetail = PlanResponse.PlanDetail.builder().id(planId).userId(otherUserId).title("다른 사용자 계획").build();

                mockedSecurity.when(SecurityUtils::getCurrentUser).thenReturn(testUser);
                when(planDao.findByIdToDto(planId)).thenReturn(Optional.of(planDetail));

                // when & then
                BusinessLogicException exception = assertThrows(BusinessLogicException.class, () -> planService.getPlanDetail(planId));
                assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.FORBIDDEN);
            }
        }
    }

    @Nested
    @DisplayName("createPlan 메소드 테스트")
    class CreatePlanTest {

        @Test
        @DisplayName("성공: 새로운 여행 계획을 생성한다")
        void createPlan_success() {
            // given
            PlanRequest.Post request = new PlanRequest.Post("새로운 여행");
            Plan plan = Plan.builder().id(1L).build();
            PlanResponse.PlanDetail planDetail = PlanResponse.PlanDetail.builder().id(1L).title(request.getTitle()).build();

            when(planDao.insert(any(Plan.class))).thenAnswer(invocation -> {
                Plan p = invocation.getArgument(0);
                p.setId(1L); // Simulate generated key
                return 1;
            });
            when(planDao.findByIdToDto(anyLong())).thenReturn(Optional.of(planDetail));

            // when
            PlanResponse.PlanDetail result = planService.createPlan(request, testUser);

            // then
            assertThat(result).isNotNull();
            assertThat(result.getTitle()).isEqualTo(request.getTitle());
        }
    }

    @Nested
    @DisplayName("updatePlan 메소드 테스트")
    class ToDoUpdatedPlanTest {
        @Test
        @DisplayName("성공: 여행 계획을 수정한다")
        void updatePlan_success() {
            try (MockedStatic<SecurityUtils> mockedSecurity = mockStatic(SecurityUtils.class)) {
                // given
                Long planId = 1L;
                PlanRequest.Update request = new PlanRequest.Update("수정된 여행");
                Plan existingPlan = Plan.builder()
                        .id(planId)
                        .userId(testUser.getUserId())
                        .title("원본 여행")
                        .startDate(LocalDate.now())
                        .endDate(LocalDate.now().plusDays(1))
                        .build();

                mockedSecurity.when(SecurityUtils::getCurrentUser).thenReturn(testUser);
                when(planDao.findById(planId)).thenReturn(Optional.of(existingPlan));
                when(planDao.update(any(Plan.class))).thenReturn(1);

                // when
                PlanResponse.Update result = planService.updatePlan(request, planId);

                // then
                assertThat(result).isNotNull();
                assertThat(result.getTitle()).isEqualTo(request.getTitle());
            }
        }

        @Test
        @DisplayName("실패: 존재하지 않는 계획 수정 시 예외 발생")
        void updatePlan_notFound() {
            try (MockedStatic<SecurityUtils> mockedSecurity = mockStatic(SecurityUtils.class)) {
                // given
                Long nonExistPlanId = 99L;
                PlanRequest.Update request = new PlanRequest.Update("수정");
                mockedSecurity.when(SecurityUtils::getCurrentUser).thenReturn(testUser);
                when(planDao.findById(nonExistPlanId)).thenReturn(Optional.empty());

                // when & then
                BusinessLogicException exception = assertThrows(BusinessLogicException.class, () -> planService.updatePlan(request, nonExistPlanId));
                assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.PLAN_NOT_FOUND);
            }
        }

        @Test
        @DisplayName("실패: 다른 사용자의 계획 수정 시 예외 발생")
        void updatePlan_forbidden() {
            try (MockedStatic<SecurityUtils> mockedSecurity = mockStatic(SecurityUtils.class)) {
                // given
                Long planId = 1L;
                Long otherUserId = 2L;
                PlanRequest.Update request = new PlanRequest.Update("수정");
                Plan otherUserPlan = Plan.builder().id(planId).userId(otherUserId).build();

                mockedSecurity.when(SecurityUtils::getCurrentUser).thenReturn(testUser);
                when(planDao.findById(planId)).thenReturn(Optional.of(otherUserPlan));

                // when & then
                BusinessLogicException exception = assertThrows(BusinessLogicException.class, () -> planService.updatePlan(request, planId));
                assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.FORBIDDEN);
            }
        }
    }

    @Nested
    @DisplayName("removePlan 메소드 테스트")
    class RemovePlanTest {

        @Test
        @DisplayName("성공: 여행 계획을 삭제한다")
        void removePlan_success() {
            try (MockedStatic<SecurityUtils> mockedSecurity = mockStatic(SecurityUtils.class)) {
                // given
                Long planId = 1L;
                Plan existingPlan = Plan.builder().id(planId).userId(testUser.getUserId()).title("삭제될 여행").build();

                mockedSecurity.when(SecurityUtils::getCurrentUser).thenReturn(testUser);
                when(planDao.findById(planId)).thenReturn(Optional.of(existingPlan));
                when(planDao.delete(planId)).thenReturn(1);

                // when
                PlanResponse.Remove result = planService.removePlan(planId);

                // then
                assertThat(result).isNotNull();
                assertThat(result.getId()).isEqualTo(planId);
                assertThat(result.getTitle()).isEqualTo("삭제될 여행");
            }
        }

        @Test
        @DisplayName("실패: 존재하지 않는 계획 삭제 시 예외 발생")
        void removePlan_notFound() {
            try (MockedStatic<SecurityUtils> mockedSecurity = mockStatic(SecurityUtils.class)) {
                // given
                Long nonExistPlanId = 99L;
                mockedSecurity.when(SecurityUtils::getCurrentUser).thenReturn(testUser);
                when(planDao.findById(nonExistPlanId)).thenReturn(Optional.empty());

                // when & then
                BusinessLogicException exception = assertThrows(BusinessLogicException.class, () -> planService.removePlan(nonExistPlanId));
                assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.PLAN_NOT_FOUND);
            }
        }

        @Test
        @DisplayName("실패: 다른 사용자의 계획 삭제 시 예외 발생")
        void removePlan_forbidden() {
            try (MockedStatic<SecurityUtils> mockedSecurity = mockStatic(SecurityUtils.class)) {
                // given
                Long planId = 1L;
                Long otherUserId = 2L;
                Plan otherUserPlan = Plan.builder().id(planId).userId(otherUserId).build();

                mockedSecurity.when(SecurityUtils::getCurrentUser).thenReturn(testUser);
                when(planDao.findById(planId)).thenReturn(Optional.of(otherUserPlan));

                // when & then
                BusinessLogicException exception = assertThrows(BusinessLogicException.class, () -> planService.removePlan(planId));
                assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.FORBIDDEN);
            }
        }

        @Test
        @DisplayName("실패: DAO에서 삭제가 실패했을 때 예외 발생")
        void removePlan_daoFail() {
            try (MockedStatic<SecurityUtils> mockedSecurity = mockStatic(SecurityUtils.class)) {
                // given
                Long planId = 1L;
                Plan existingPlan = Plan.builder().id(planId).userId(testUser.getUserId()).title("삭제될 여행").build();

                mockedSecurity.when(SecurityUtils::getCurrentUser).thenReturn(testUser);
                when(planDao.findById(planId)).thenReturn(Optional.of(existingPlan));
                when(planDao.delete(planId)).thenReturn(0); // Simulate delete failure

                // when & then
                BusinessLogicException exception = assertThrows(BusinessLogicException.class, () -> planService.removePlan(planId));
                assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.PLAN_PROCESS_FAIL);
            }
        }
    }
}
