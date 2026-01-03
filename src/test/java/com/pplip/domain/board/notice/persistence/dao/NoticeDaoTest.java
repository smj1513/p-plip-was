package com.pplip.domain.board.notice.persistence.dao;

import com.pplip.domain.board.notice.api.response.NoticeResponse;
import com.pplip.domain.board.notice.persistence.entity.NoticeBoard;
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
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@MybatisTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class NoticeDaoTest {

    @Autowired
    NoticeDao dao;

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

        Profile profile = Profile.builder().description("tests").nickname("teststest").userId(authorId).build();
        profileDao.insert(profile);

    }

    @Test
    @DisplayName("성공: 새 공지사항을 저장한 후 ID로 조회할 수 있다")
    void insertAndFindById_Success() {
        // given
        NoticeBoard board = NoticeBoard.builder()
                .authorId(authorId)
                .title("테스트 공지")
                .content("테스트 공지 내용")
                .build();
        dao.insert(board);

        // when
        Optional<NoticeResponse.Detail> foundBoardOptional = dao.findById(board.getId());

        // then
        assertThat(foundBoardOptional).isPresent();
        NoticeResponse.Detail foundBoard = foundBoardOptional.get();
        assertThat(foundBoard.getTitle()).isEqualTo("테스트 공지");
        assertThat(foundBoard.getContent()).isEqualTo("테스트 공지 내용");
    }

    @Test
    @DisplayName("실패: 존재하지 않는 ID로 조회 시 빈 Optional을 반환한다")
    void findById_Fail_WhenBoardDoesNotExist() {
        // given
        long nonExistentId = 999L;

        // when
        Optional<NoticeResponse.Detail> detail = dao.findById(nonExistentId);

        // then
        assertThat(detail).isNotPresent();
    }

    @Test
    @DisplayName("성공: 공지사항 목록을 페이지에 맞게 조회한다")
    void findAll_Success() {
        // given
        dao.insert(NoticeBoard.builder().authorId(authorId).title("t1").content("c1").build());
        dao.insert(NoticeBoard.builder().authorId(authorId).title("t2").content("c2").build());

        // when
        List<NoticeResponse.Summary> all = dao.findAll(new PageRequest(1, 10));

        // then
        assertThat(all).isNotNull();
        assertThat(all.size()).isGreaterThanOrEqualTo(2);
    }


}