package com.pplip.domain.trip.review.usecase.impl;

import com.pplip.domain.auth.persistence.entity.Account;
import com.pplip.domain.auth.utils.SecurityUtils;
import com.pplip.domain.file.api.response.FileResponse;
import com.pplip.domain.file.persistence.dao.ReviewImagePropertyDao;
import com.pplip.domain.file.usecase.FileService;
import com.pplip.domain.trip.review.api.request.ReviewRequest;
import com.pplip.domain.trip.review.api.response.ReviewResponse;
import com.pplip.domain.trip.review.persistence.dao.ReviewDao;
import com.pplip.domain.trip.review.persistence.entity.Review;
import com.pplip.domain.trip.review.persistence.entity.ReviewSort;
import com.pplip.domain.trip.review.utils.ReviewParser;
import com.pplip.global.api.code.ErrorCode;
import com.pplip.global.exception.BoardLogicException;
import com.pplip.global.exception.BusinessLogicException;
import com.pplip.global.page.Page;
import com.pplip.global.page.PageRequest;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ReviewServiceImpl 테스트")
class ReviewServiceImplTest {

	@InjectMocks
	private ReviewServiceImpl reviewService;

	@Mock
	private ReviewDao reviewDao;

	@Mock
	private ReviewImagePropertyDao imagePropertyDao;

	@Mock
	private FileService fileService;

	@Mock
	private ReviewParser parser;

	private MockedStatic<SecurityUtils> securityUtilsMock;

	@BeforeEach
	void setUp() {
		securityUtilsMock = mockStatic(SecurityUtils.class);
	}

	@AfterEach
	void clear() {
		if (securityUtilsMock != null) {
			securityUtilsMock.close();
		}
	}

	private Account createAccount(Long userId) {
		return Account.builder().userId(userId).build();
	}

	private Review createReview(Long reviewId, Long authorId, Long attractionId) {
		return Review.builder()
				.id(reviewId)
				.authorId(authorId)
				.attractionId(attractionId)
				.content("리뷰 내용")
				.createdAt(LocalDateTime.now())
				.updatedAt(LocalDateTime.now())
				.build();
	}

	private ReviewResponse.Detail createReviewDetail(Long reviewId, Long authorId) {
		return ReviewResponse.Detail.builder()
				.id(reviewId)
				.authorId(authorId)
				.content("리뷰 내용")
				.reviewImages(Collections.emptyList())
				.userProfileImage(new FileResponse())
				.build();
	}

	@Nested
	@DisplayName("findAll 메소드")
	class FindAll {

		@Test
		@DisplayName("성공 - 리뷰 목록을 페이지네이션하여 반환한다")
		void findAll_success() {
			// given
			Long attractionId = 1L;
			PageRequest pageRequest = new PageRequest(1, 10);
			List<ReviewResponse.Detail> reviews = List.of(createReviewDetail(1L, 1L));
			when(reviewDao.findAllByAttractionNo(attractionId, new PageRequest(1, 20), ReviewSort.ASC)).thenReturn(reviews);
			when(reviewDao.countAllByAttractionNo(attractionId)).thenReturn(1);
			securityUtilsMock.when(SecurityUtils::isAnonymous).thenReturn(false);
			securityUtilsMock.when(SecurityUtils::getCurrentUser).thenReturn(createAccount(1L));

			// when
			Page<ReviewResponse.Detail> result = reviewService.findAll(attractionId, pageRequest, ReviewSort.ASC);

			// then
			assertThat(result).isNotNull();
			assertThat(result.getList()).hasSize(1);
			assertThat(result.getList().get(0).isAuthor()).isTrue();
		}

		@Test
		@DisplayName("성공 - 익명 사용자의 경우 isAuthor는 false이다")
		void findAll_anonymous_success() {
			// given
			Long attractionId = 1L;
			PageRequest pageRequest = new PageRequest(1, 10);
			List<ReviewResponse.Detail> reviews = List.of(createReviewDetail(1L, 1L));
			when(reviewDao.findAllByAttractionNo(attractionId, pageRequest, ReviewSort.ASC)).thenReturn(reviews);
			when(reviewDao.countAllByAttractionNo(attractionId)).thenReturn(1);
			securityUtilsMock.when(SecurityUtils::isAnonymous).thenReturn(true);

			// when
			Page<ReviewResponse.Detail> result = reviewService.findAll(attractionId, pageRequest,ReviewSort.ASC);

			// then
			assertThat(result).isNotNull();
			assertThat(result.getList()).hasSize(1);
			assertThat(result.getList().get(0).isAuthor()).isFalse();
		}

		@Test
		@DisplayName("엣지 - 리뷰가 없을 경우 빈 페이지를 반환한다")
		void findAll_empty() {
			// given
			Long attractionId = 1L;
			PageRequest pageRequest = new PageRequest(1, 10);
			when(reviewDao.findAllByAttractionNo(attractionId, pageRequest, ReviewSort.ASC)).thenReturn(Collections.emptyList());
			when(reviewDao.countAllByAttractionNo(attractionId)).thenReturn(0);
			securityUtilsMock.when(SecurityUtils::isAnonymous).thenReturn(true);

			// when
			Page<ReviewResponse.Detail> result = reviewService.findAll(attractionId, pageRequest, ReviewSort.ASC);

			// then
			assertThat(result).isNotNull();
			assertThat(result.getList()).isEmpty();
		}
	}

	@Nested
	@DisplayName("post 메소드")
	class PostTodo {

		@Test
		@DisplayName("성공 - 새로운 리뷰를 작성한다")
		void post_success() {
			// given
			Long attractionId = 1L;
			Long userId = 1L;
			UserDetails userDetails = createAccount(userId);
			ReviewRequest.Post postRequest = new ReviewRequest.Post("새로운 리뷰", null);

			// [수정] 헬퍼 메소드 대신, 내용을 "새로운 리뷰"로 맞춰서 직접 빌드하거나
			// 헬퍼 메소드로 만든 뒤 값을 변경해야 합니다. (ReflectionTestUtils 사용 예시)
			ReviewResponse.Detail reviewDetail = createReviewDetail(1L, userId);
			ReflectionTestUtils.setField(reviewDetail, "content", "새로운 리뷰"); // 내용을 강제로 맞춤

			when(reviewDao.insert(any(Review.class))).thenAnswer(invocation -> {
				Review savedReview = invocation.getArgument(0);
				ReflectionTestUtils.setField(savedReview, "id", 1L);
				return 1;
			});

			// 이제 findById는 "새로운 리뷰"가 담긴 객체를 반환합니다.
			when(reviewDao.findById(anyLong())).thenReturn(Optional.of(reviewDetail));

			// when
			ReviewResponse.Detail result = reviewService.post(postRequest, attractionId, userDetails);

			// then
			assertThat(result).isNotNull();
			assertThat(result.getId()).isEqualTo(reviewDetail.getId());
			assertThat(result.getContent()).isEqualTo(postRequest.getContent()); // 이제 통과됨
			verify(reviewDao, times(1)).insert(any(Review.class));
			verify(reviewDao, times(1)).findById(anyLong());
		}

		@Test
		@DisplayName("성공 - 파일과 함께 새로운 리뷰를 작성한다")
		void post_with_files_success() {
			// given
			Long attractionId = 1L;
			Long userId = 1L;
			UserDetails userDetails = createAccount(userId);
			List<Long> fileIds = List.of(1L, 2L);
			ReviewRequest.Post postRequest = new ReviewRequest.Post("새로운 리뷰", fileIds);
			Review review = createReview(1L, userId, attractionId);
			ReviewResponse.Detail reviewDetail = createReviewDetail(1L, userId);

			when(reviewDao.insert(any(Review.class))).thenAnswer(invocation -> {
				Review r = invocation.getArgument(0);
				r.setId(1L);
				return 1;
			});
			when(reviewDao.findById(anyLong())).thenReturn(Optional.of(reviewDetail));

			// when
			ReviewResponse.Detail result = reviewService.post(postRequest, attractionId, userDetails);

			// then
			assertThat(result).isNotNull();
			verify(imagePropertyDao, times(1)).bulkUpdate(fileIds, 1L);
		}

		@Test
		@DisplayName("실패 - 리뷰 생성에 실패하면 예외를 던진다")
		void post_fail_insert() {
			// given
			Long attractionId = 1L;
			UserDetails userDetails = createAccount(1L);
			ReviewRequest.Post postRequest = new ReviewRequest.Post("새로운 리뷰", null);
			when(reviewDao.insert(any(Review.class))).thenReturn(0);

			// when & then
			assertThatThrownBy(() -> reviewService.post(postRequest, attractionId, userDetails))
					.isInstanceOf(BusinessLogicException.class)
					.hasFieldOrPropertyWithValue("errorCode", ErrorCode.REVIEW_CREATE_FAILURE);
		}

		@Test
		@DisplayName("실패 - 생성 후 리뷰를 찾지 못하면 예외를 던진다")
		void post_fail_find() {
			// given
			Long attractionId = 1L;
			UserDetails userDetails = createAccount(1L);
			ReviewRequest.Post postRequest = new ReviewRequest.Post("새로운 리뷰", null);

			when(reviewDao.insert(any(Review.class))).thenAnswer(invocation -> {
				Review savedReview = invocation.getArgument(0); // 메서드에 전달된 첫 번째 인자(Review 객체)를 꺼냄
				ReflectionTestUtils.setField(savedReview, "id", 1L); // 강제로 ID에 1L 주입 (Setter가 있다면 setId 사용 가능)
				return 1;
			});			when(reviewDao.findById(anyLong())).thenReturn(Optional.empty());

			// when & then
			assertThatThrownBy(() -> reviewService.post(postRequest, attractionId, userDetails))
					.isInstanceOf(BusinessLogicException.class)
					.hasFieldOrPropertyWithValue("errorCode", ErrorCode.REVIEW_NOT_FOUND);
		}
	}


	@Nested
	@DisplayName("update 메소드")
	class ToDoUpdated {

		@Test
		@DisplayName("성공 - 리뷰를 수정한다")
		void update_success() {
			// given
			Long reviewId = 1L;
			Long userId = 1L;
			UserDetails userDetails = createAccount(userId);
			ReviewRequest.Update updateRequest = new ReviewRequest.Update("수정된 리뷰", null);
			Review review = createReview(reviewId, userId, 1L);
			ReviewResponse.Detail reviewDetail = createReviewDetail(reviewId, userId);
			ReviewResponse.Update reviewUpdateResponse = ReviewResponse.Update.builder().id(reviewId).build();

			when(reviewDao.findByIdToEntity(reviewId)).thenReturn(Optional.of(review));
			when(reviewDao.findById(reviewId)).thenReturn(Optional.of(reviewDetail));
			when(parser.detailResToUpdateRes(reviewDetail)).thenReturn(reviewUpdateResponse);

			// when
			ReviewResponse.Update result = reviewService.update(updateRequest, reviewId, userDetails);

			// then
			assertThat(result).isNotNull();
			assertThat(result.getId()).isEqualTo(reviewId);
			verify(reviewDao, times(1)).update(any(Review.class));
		}

		@Test
		@DisplayName("실패 - 다른 사용자가 리뷰를 수정하려고 하면 예외를 던진다")
		void update_fail_forbidden() {
			// given
			Long reviewId = 1L;
			Long authorId = 1L;
			Long otherUserId = 2L;
			UserDetails userDetails = createAccount(otherUserId);
			ReviewRequest.Update updateRequest = new ReviewRequest.Update("수정된 리뷰", null);
			Review review = createReview(reviewId, authorId, 1L);

			when(reviewDao.findByIdToEntity(reviewId)).thenReturn(Optional.of(review));

			// when & then
			assertThatThrownBy(() -> reviewService.update(updateRequest, reviewId, userDetails))
					.isInstanceOf(BusinessLogicException.class)
					.hasFieldOrPropertyWithValue("errorCode", ErrorCode.FORBIDDEN);
		}

		@Test
		@DisplayName("실패 - 존재하지 않는 리뷰를 수정하려고 하면 예외를 던진다")
		void update_fail_not_found() {
			// given
			Long reviewId = 1L;
			UserDetails userDetails = createAccount(1L);
			ReviewRequest.Update updateRequest = new ReviewRequest.Update("수정된 리뷰", null);

			when(reviewDao.findByIdToEntity(reviewId)).thenReturn(Optional.empty());

			// when & then
			assertThatThrownBy(() -> reviewService.update(updateRequest, reviewId, userDetails))
					.isInstanceOf(BusinessLogicException.class)
					.hasFieldOrPropertyWithValue("errorCode", ErrorCode.REVIEW_NOT_FOUND);
		}
	}

	@Nested
	@DisplayName("delete 메소드")
	class Delete {

		@Test
		@DisplayName("성공 - 리뷰를 삭제한다")
		void delete_success() {
			// given
			Long reviewId = 1L;
			Long userId = 1L;
			UserDetails userDetails = createAccount(userId);
			Review review = createReview(reviewId, userId, 1L);

			when(reviewDao.findByIdToEntity(reviewId)).thenReturn(Optional.of(review));
			when(reviewDao.delete(reviewId)).thenReturn(1);

			// when
			long deletedId = reviewService.delete(reviewId, userDetails);

			// then
			assertThat(deletedId).isEqualTo(reviewId);
			verify(reviewDao, times(1)).delete(reviewId);
		}

		@Test
		@DisplayName("실패 - 다른 사용자가 리뷰를 삭제하려고 하면 예외를 던진다")
		void delete_fail_forbidden() {
			// given
			Long reviewId = 1L;
			Long authorId = 1L;
			Long otherUserId = 2L;
			UserDetails userDetails = createAccount(otherUserId);
			Review review = createReview(reviewId, authorId, 1L);

			when(reviewDao.findByIdToEntity(reviewId)).thenReturn(Optional.of(review));

			// when & then
			assertThatThrownBy(() -> reviewService.delete(reviewId, userDetails))
					.isInstanceOf(BusinessLogicException.class)
					.hasFieldOrPropertyWithValue("errorCode", ErrorCode.FORBIDDEN);
		}

		@Test
		@DisplayName("실패 - 존재하지 않는 리뷰를 삭제하려고 하면 예외를 던진다")
		void delete_fail_not_found() {
			// given
			Long reviewId = 1L;
			UserDetails userDetails = createAccount(1L);

			when(reviewDao.findByIdToEntity(reviewId)).thenReturn(Optional.empty());

			// when & then
			assertThatThrownBy(() -> reviewService.delete(reviewId, userDetails))
					.isInstanceOf(BoardLogicException.class)
					.hasFieldOrPropertyWithValue("errorCode", ErrorCode.REVIEW_NOT_FOUND);
		}

		@Test
		@DisplayName("실패 - 리뷰 삭제에 실패하면 예외를 던진다")
		void delete_fail_delete() {
			// given
			Long reviewId = 1L;
			Long userId = 1L;
			UserDetails userDetails = createAccount(userId);
			Review review = createReview(reviewId, userId, 1L);

			when(reviewDao.findByIdToEntity(reviewId)).thenReturn(Optional.of(review));
			when(reviewDao.delete(reviewId)).thenReturn(0);

			// when & then
			assertThatThrownBy(() -> reviewService.delete(reviewId, userDetails))
					.isInstanceOf(BusinessLogicException.class)
					.hasFieldOrPropertyWithValue("errorCode", ErrorCode.REVIEW_FAIL_DELETE);
		}
	}
}
