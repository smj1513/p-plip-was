package com.pplip.domain.board.freeboard.persistence.dao;

import com.pplip.domain.board.freeboard.persistence.entity.FreeBoard;
import com.pplip.domain.user.persistence.dao.UserDao;
import com.pplip.domain.user.persistence.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@MybatisTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("UserLikeBoardDao 테스트")
class UserLikeBoardDaoTest {

    @Autowired
    private UserLikeBoardDao userLikeBoardDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private FreeBoardDao freeBoardDao;

    private User testUser;
    private FreeBoard testBoard;

    @BeforeEach
    void setUp() {
        // 테스트 데이터베이스에 직접 필요한 데이터 삽입
        testUser = User.builder().name("testuser").birth(LocalDate.now()).build();
        userDao.insert(testUser); // id가 testUser 객체에 설정됨

        testBoard = FreeBoard.builder().authorId(testUser.getId()).title("test title").content("test content").build();
        freeBoardDao.insert(testBoard); // id가 testBoard 객체에 설정됨
    }

    @Test
    @DisplayName("좋아요 삽입 성공")
    void insert_should_succeed() {
        // when
        int result = userLikeBoardDao.insert(testBoard.getId(), testUser.getId());

        // then
        assertThat(result).isEqualTo(1);
        boolean isLiked = userLikeBoardDao.findByBoardIdUserId(testBoard.getId(), testUser.getId());
        assertThat(isLiked).isTrue();
    }

    @Test
    @DisplayName("좋아요 삭제 성공")
    void delete_should_succeed() {
        // given
        userLikeBoardDao.insert(testBoard.getId(), testUser.getId());

        // when
        int result = userLikeBoardDao.delete(testBoard.getId(), testUser.getId());

        // then
        assertThat(result).isEqualTo(1);
        boolean isLiked = userLikeBoardDao.findByBoardIdUserId(testBoard.getId(), testUser.getId());
        assertThat(isLiked).isFalse();
    }

    @Test
    @DisplayName("좋아요 존재 시 true 반환")
    void findByBoardIdUserId_should_return_true_when_like_exists() {
        // given
        userLikeBoardDao.insert(testBoard.getId(), testUser.getId());

        // when
        boolean result = userLikeBoardDao.findByBoardIdUserId(testBoard.getId(), testUser.getId());

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("좋아요 미존재 시 false 반환")
    void findByBoardIdUserId_should_return_false_when_like_not_exists() {
        // when
        boolean result = userLikeBoardDao.findByBoardIdUserId(testBoard.getId(), testUser.getId());

        // then
        assertThat(result).isFalse();
    }
}