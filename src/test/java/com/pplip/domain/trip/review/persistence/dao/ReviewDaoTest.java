package com.pplip.domain.trip.review.persistence.dao;

import com.pplip.domain.trip.review.api.response.ReviewResponse;
import com.pplip.domain.trip.review.persistence.entity.Review;
import com.pplip.domain.trip.review.persistence.entity.ReviewSort;
import com.pplip.domain.user.persistence.dao.ProfileDao;
import com.pplip.domain.user.persistence.dao.UserDao;
import com.pplip.domain.user.persistence.entity.Profile;
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

import static org.assertj.core.api.Assertions.assertThat;

@MybatisTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ReviewDaoTest {

    @Autowired
    ReviewDao dao;

    @Autowired
    UserDao userDao;

    private Long authorId;
	@Autowired
	private ProfileDao profileDao;

    @BeforeEach
    void setUp() {
        User user = User.builder().name("testuser").birth(LocalDate.now()).build();
        userDao.insert(user);
        authorId = user.getId();

        Profile profile = Profile.builder().nickname("testse")
                .description("testste").userId(authorId).build();
        profileDao.insert(profile);

    }

    //[트러블 슈팅] : ReviewResponse에 authorId가 없었던 문제 해결
    @Test
    @DisplayName("성공: 새 리뷰를 저장한 후 특정 명소의 리뷰 목록에서 조회할 수 있다")
    void insertAndFindAllByAttractionNo_Success() {
        // given
        long attractionId = 56658L; // Gyeongbokgung Palace, assumed to exist
        Review review = Review.builder()
                .authorId(authorId)
                .createdAt(LocalDateTime.now())
                .attractionId(attractionId)
                .content("경복궁 방문 후기입니다.")
                .build();
        dao.insert(review);

        // when
        List<ReviewResponse.Detail> reviews = dao.findAllByAttractionNo(attractionId, new PageRequest(1, 20), ReviewSort.ASC);

        // then
        assertThat(reviews).isNotNull().isNotEmpty();
        assertThat(reviews).extracting(ReviewResponse.Detail::getContent)
                .contains("경복궁 방문 후기입니다.");
    }

    @Test
    @DisplayName("실패: 리뷰가 없는 명소 조회 시 빈 리스트를 반환한다")
    void findAllByAttractionNo_Fail_WhenNoReviews() {
        // given
        long attractionIdWithNoReviews = -1L;

        // when
        List<ReviewResponse.Detail> reviews = dao.findAllByAttractionNo(attractionIdWithNoReviews, new PageRequest(1, 20), ReviewSort.ASC);

        // then
        assertThat(reviews).isNotNull().isEmpty();
    }
}