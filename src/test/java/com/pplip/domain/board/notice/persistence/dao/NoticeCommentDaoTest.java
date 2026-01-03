package com.pplip.domain.board.notice.persistence.dao;

import com.pplip.domain.auth.persistence.dao.AccountDao;
import com.pplip.domain.auth.persistence.entity.Account;
import com.pplip.domain.auth.persistence.entity.Role;
import com.pplip.domain.board.notice.api.response.NoticeCommentResponse;
import com.pplip.domain.board.notice.persistence.entity.NoticeBoard;
import com.pplip.domain.board.notice.persistence.entity.NoticeComment;
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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@MybatisTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("NoticeCommentDao 테스트")
class NoticeCommentDaoTest {

    @Autowired
    private NoticeCommentDao noticeCommentDao;

    @Autowired
    private NoticeDao noticeDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private AccountDao accountDao;

    private User user;
    private Account account;
    private NoticeBoard noticeBoard;
	@Autowired
	private ProfileDao profileDao;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .name("testUser")
                .birth(LocalDate.now())
                .build();
        userDao.insert(user);

        Profile profile = Profile.builder().nickname("test").description("testset").userId(user.getId()).build();
        profileDao.insert(profile);

        account = Account.builder()
                .userId(user.getId())
                .email("test@test.com")
                .password("password")
                .role(Role.USER)
                .build();
        accountDao.insert(account);

        noticeBoard = NoticeBoard.builder()
                .authorId(user.getId())
                .title("Test Title")
                .content("Test Content")
                .build();
        noticeDao.insert(noticeBoard);
    }

    @Test
    @DisplayName("성공: 특정 게시글의 모든 댓글 조회")
    void findAll_Success() {
        // Given
        noticeCommentDao.insert(NoticeComment.builder().noticeBoardId(noticeBoard.getId()).authorId(user.getId()).content("c1").build());

        // When
        List<NoticeCommentResponse.Summary> comments = noticeCommentDao.findAll(noticeBoard.getId(), new PageRequest(0, 20));

        // Then
        assertNotNull(comments, "댓글 목록은 null이 아니어야 합니다.");
        assertThat(comments).isNotEmpty();
    }

    @Test
    @DisplayName("성공: 새 댓글 추가")
    void insert_Success() {
        // Given
        NoticeComment newComment = NoticeComment.builder()
                .noticeBoardId(noticeBoard.getId())
                .authorId(user.getId())
                .content("새로운 댓글입니다.")
                .isRemoved(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // When
        noticeCommentDao.insert(newComment);

        // Then
        assertThat(newComment.getId()).isNotNull();
        Optional<NoticeCommentResponse.Summary> foundComment = noticeCommentDao.findAll(noticeBoard.getId(), new PageRequest(0, 20)).stream()
                .filter(c -> c.getId().equals(newComment.getId()))
                .findFirst();
        assertThat(foundComment).isPresent();
        assertThat(foundComment.get().getContent()).isEqualTo("새로운 댓글입니다.");
    }
    
    @Test
    @DisplayName("실패: 존재하지 않는 게시글에 댓글 추가 시 예외 발생")
    void insert_Fail_WithNonExistingNoticeId() {
        // Given
        Long nonExistingNoticeId = -1L;
        NoticeComment newComment = NoticeComment.builder()
                .noticeBoardId(nonExistingNoticeId)
                .authorId(user.getId())
                .content("이 댓글은 추가되어서는 안됩니다.")
                .isRemoved(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // When & Then
        assertThrows(DataIntegrityViolationException.class, () -> {
            noticeCommentDao.insert(newComment);
        });
    }

    @Test
    @DisplayName("성공: 댓글 내용 수정")
    void update_Success() {
        // Given
        NoticeComment comment = NoticeComment.builder()
                .noticeBoardId(noticeBoard.getId())
                .authorId(user.getId())
                .content("원본 내용")
                .build();
        noticeCommentDao.insert(comment);
        
        String updatedContent = "수정된 댓글입니다.";
        comment.updateContent(updatedContent); // Assumes updateContent method exists

        // When
        int affectedRows = noticeCommentDao.update(comment);

        // Then
        assertThat(affectedRows).isEqualTo(1);
        Optional<NoticeCommentResponse.Summary> foundComment = noticeCommentDao.findAll(noticeBoard.getId(), new PageRequest(0, 20)).stream()
                .filter(c -> c.getId().equals(comment.getId()))
                .findFirst();
        assertThat(foundComment).isPresent();
        assertThat(foundComment.get().getContent()).isEqualTo(updatedContent);
    }

    @Test
    @DisplayName("성공: 댓글 삭제 (논리적 삭제)")
    void delete_Success() {
        // Given
        NoticeComment comment = NoticeComment.builder()
                .noticeBoardId(noticeBoard.getId())
                .authorId(user.getId())
                .content("삭제될 댓글")
                .build();
        noticeCommentDao.insert(comment);
        Long commentIdToDelete = comment.getId();

        // When
        int affectedRows = noticeCommentDao.delete(commentIdToDelete);

        // Then
        assertThat(affectedRows).isEqualTo(1);
        Optional<NoticeCommentResponse.Summary> foundComment = noticeCommentDao.findAll(noticeBoard.getId(), new PageRequest(0, 20)).stream()
                .filter(c -> c.getId().equals(commentIdToDelete))
                .findFirst();
        // Assuming soft delete means it's marked as removed, but DTO might exclude it
        // A better check would be against a DTO field like 'isRemoved' if it exists
        assertThat(foundComment).isNotPresent();
    }
}
