package com.pplip.domain.board.freeboard.persistence.dao;

import com.pplip.domain.auth.persistence.dao.AccountDao;
import com.pplip.domain.auth.persistence.entity.Account;
import com.pplip.domain.auth.persistence.entity.Role;
import com.pplip.domain.board.freeboard.api.response.FreeBoardCommentResponse;
import com.pplip.domain.board.freeboard.persistence.entity.FreeBoard;
import com.pplip.domain.board.freeboard.persistence.entity.FreeComment;
import com.pplip.domain.user.persistence.dao.ProfileDao;
import com.pplip.domain.user.persistence.dao.UserDao;
import com.pplip.domain.user.persistence.entity.Profile;
import com.pplip.domain.user.persistence.entity.User;
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
@DisplayName("FreeBoardCommentDao 테스트")
class FreeBoardCommentDaoTest {

    @Autowired
    private FreeBoardCommentDao freeBoardCommentDao;

    @Autowired
    private FreeBoardDao freeBoardDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private AccountDao accountDao;

    private User user;
    private Account account;
    private FreeBoard freeBoard;
	@Autowired
	private ProfileDao profileDao;


    @BeforeEach
    void setUp() {
        user = User.builder()
                .name("testUser")
                .birth(LocalDate.now())
                .build();
        userDao.insert(user);

        Profile profile = Profile.builder().nickname("testest").description("teststests").userId(user.getId()).build();
        profileDao.insert(profile);

        account = Account.builder()
                .userId(user.getId())
                .email("test@test.com")
                .password("password")
                .role(Role.USER)
                .build();
        accountDao.insert(account);

        freeBoard = FreeBoard.builder()
                .authorId(user.getId())
                .title("Test Title")
                .content("Test Content")
                .build();
        freeBoardDao.insert(freeBoard);
    }


    @Test
    @DisplayName("성공: 특정 게시글의 모든 댓글 조회")
    void findAll_Success() {
        // Given
        // Test data is assumed to be in the database or inserted here

        // When
        List<FreeBoardCommentResponse.Retrieve> comments = freeBoardCommentDao.findAll(freeBoard.getId());

        // Then
        assertNotNull(comments, "댓글 목록은 null이 아니어야 합니다.");
    }

    @Test
    @DisplayName("성공: 새 댓글 추가")
    void insert_Success() {
        // Given
        FreeComment newComment = FreeComment.builder()
                .boardId(freeBoard.getId())
                .authorId(user.getId())
                .content("새로운 자유게시판 댓글입니다.")
                .isRemoved(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // When
        freeBoardCommentDao.insert(newComment);

        // Then
        assertThat(newComment.getId()).isNotNull();
        List<FreeBoardCommentResponse.Retrieve> comments = freeBoardCommentDao.findAll(freeBoard.getId());
        Optional<FreeBoardCommentResponse.Retrieve> foundComment = comments.stream()
                .filter(c -> c.getId().equals(newComment.getId()))
                .findFirst();
        assertThat(foundComment).isPresent();
        assertThat(foundComment.get().getContent()).isEqualTo("새로운 자유게시판 댓글입니다.");
    }
    
    @Test
    @DisplayName("실패: 존재하지 않는 게시글에 댓글 추가 시 예외 발생")
    void insert_Fail_WithNonExistingBoardId() {
        // Given
        Long nonExistingBoardId = -1L;
        FreeComment newComment = FreeComment.builder()
                .boardId(nonExistingBoardId)
                .authorId(user.getId())
                .content("이 댓글은 추가되어서는 안됩니다.")
                .isRemoved(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // When & Then
        assertThrows(DataIntegrityViolationException.class, () -> {
            freeBoardCommentDao.insert(newComment);
        });
    }

    @Test
    @DisplayName("성공: 댓글 내용 수정")
    void update_Success() {
        // Given
        // First, insert a comment to update
        FreeComment commentToUpdate = FreeComment.builder()
                .boardId(freeBoard.getId())
                .authorId(user.getId())
                .content("원본 댓글 내용")
                .build();
        freeBoardCommentDao.insert(commentToUpdate);
        
        String updatedContent = "수정된 자유게시판 댓글입니다.";
        commentToUpdate.updateContent(updatedContent);

        // When
        int affectedRows = freeBoardCommentDao.update(commentToUpdate);

        // Then
        assertThat(affectedRows).isEqualTo(1);
        List<FreeBoardCommentResponse.Retrieve> comments = freeBoardCommentDao.findAll(freeBoard.getId());
        Optional<FreeBoardCommentResponse.Retrieve> foundComment = comments.stream()
                .filter(c -> c.getId().equals(commentToUpdate.getId()))
                .findFirst();
        assertThat(foundComment).isPresent();
        assertThat(foundComment.get().getContent()).isEqualTo(updatedContent);
    }

    @Test
    @DisplayName("성공: 댓글 삭제 (논리적 삭제)")
    void delete_Success() {
        // Given
        // First, insert a comment to delete
        FreeComment commentToDelete = FreeComment.builder()
                .boardId(freeBoard.getId())
                .authorId(user.getId())
                .content("삭제될 댓글")
                .build();
        freeBoardCommentDao.insert(commentToDelete);
        Long commentIdToDelete = commentToDelete.getId();

        // When
        int affectedRows = freeBoardCommentDao.delete(commentIdToDelete);

        // Then
        assertThat(affectedRows).isEqualTo(1);
        // Assuming soft delete removes it from the list returned by findAll
        List<FreeBoardCommentResponse.Retrieve> comments = freeBoardCommentDao.findAll(freeBoard.getId());
        Optional<FreeBoardCommentResponse.Retrieve> foundComment = comments.stream()
                .filter(c -> c.getId().equals(commentIdToDelete))
                .findFirst();
        assertThat(foundComment).isNotPresent();
    }
}
