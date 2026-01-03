package com.pplip.domain.board.freeboard.usecase.impl;

import com.pplip.domain.auth.persistence.entity.Account;
import com.pplip.domain.board.freeboard.api.response.FreeBoardResponse;
import com.pplip.domain.board.freeboard.persistence.dao.FreeBoardDao;
import com.pplip.domain.board.freeboard.persistence.dao.UserLikeBoardDao;
import com.pplip.domain.board.freeboard.persistence.entity.FreeBoard;
import com.pplip.global.exception.BusinessLogicException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserLikeBoardServiceImpl 테스트")
class UserLikeBoardServiceImplTest {

    @InjectMocks
    private UserLikeBoardServiceImpl userLikeBoardService;

    @Mock
    private UserLikeBoardDao userLikeBoardDao;

    @Mock
    private FreeBoardDao freeBoardDao;

    private UserDetails userDetails;
    private Long userId = 1L;
    private Long boardId = 1L;

    @BeforeEach
    void setUp() {
        Account account = Account.builder().userId(userId).build();
        userDetails = account;
    }

    @Test
    @DisplayName("isLikeFreeBoard: 좋아요 상태 확인 성공")
    void isLikeFreeBoard_success() {
        // given
        when(freeBoardDao.findById(boardId)).thenReturn(Optional.of(new FreeBoard()));
        when(userLikeBoardDao.findByBoardIdUserId(boardId, userId)).thenReturn(true);

        // when
        FreeBoardResponse.BoardLike result = userLikeBoardService.getLikeFreeBoard(boardId, userDetails);

        // then
        assertThat(result.isLike()).isTrue();
        verify(freeBoardDao).findById(boardId);
        verify(userLikeBoardDao).findByBoardIdUserId(boardId, userId);
    }

    @Test
    @DisplayName("likeFreeBoard: 좋아요 성공")
    void likeFreeBoard_success() {
        // given
        when(freeBoardDao.findById(boardId)).thenReturn(Optional.of(new FreeBoard()));
        when(userLikeBoardDao.findByBoardIdUserId(boardId, userId)).thenReturn(false);
        when(userLikeBoardDao.insert(boardId, userId)).thenReturn(1);

        // when
        FreeBoardResponse.BoardLike result = userLikeBoardService.likeFreeBoard(boardId, userDetails);

        // then
        assertThat(result.isLike()).isTrue();
        verify(userLikeBoardDao).insert(boardId, userId);
    }

    @Test
    @DisplayName("likeFreeBoard: 이미 좋아요한 경우 예외 발생")
    void likeFreeBoard_throwsException_whenAlreadyLiked() {
        // given
        when(freeBoardDao.findById(boardId)).thenReturn(Optional.of(new FreeBoard()));
        when(userLikeBoardDao.findByBoardIdUserId(boardId, userId)).thenReturn(true);

        // when & then
        assertThrows(BusinessLogicException.class, () -> {
            userLikeBoardService.likeFreeBoard(boardId, userDetails);
        });
        verify(userLikeBoardDao, never()).insert(anyLong(), anyLong());
    }
    
    @Test
    @DisplayName("likeFreeBoard: 게시글이 없는 경우 예외 발생")
    void likeFreeBoard_throwsException_whenBoardNotFound() {
        // given
        when(freeBoardDao.findById(boardId)).thenReturn(Optional.empty());

        // when & then
        assertThrows(BusinessLogicException.class, () -> {
            userLikeBoardService.likeFreeBoard(boardId, userDetails);
        });
        verify(userLikeBoardDao, never()).findByBoardIdUserId(anyLong(), anyLong());
        verify(userLikeBoardDao, never()).insert(anyLong(), anyLong());
    }

    @Test
    @DisplayName("unlikeFreeBoard: 좋아요 취소 성공")
    void unlikeFreeBoard_success() {
        // given
        when(freeBoardDao.findById(boardId)).thenReturn(Optional.of(new FreeBoard()));
        when(userLikeBoardDao.delete(boardId, userId)).thenReturn(1);

        // when
        FreeBoardResponse.BoardLike result = userLikeBoardService.unlikeFreeBoard(boardId, userDetails);

        // then
        assertThat(result.isLike()).isFalse();
        verify(userLikeBoardDao).delete(boardId, userId);
    }

    @Test
    @DisplayName("unlikeFreeBoard: 좋아요 취소 실패 시 예외 발생")
    void unlikeFreeBoard_throwsException_whenDeletionFails() {
        // given
        when(freeBoardDao.findById(boardId)).thenReturn(Optional.of(new FreeBoard()));
        when(userLikeBoardDao.delete(boardId, userId)).thenReturn(0);

        // when & then
        assertThrows(BusinessLogicException.class, () -> {
            userLikeBoardService.unlikeFreeBoard(boardId, userDetails);
        });
        verify(userLikeBoardDao).delete(boardId, userId);
    }
    
    @Test
    @DisplayName("unlikeFreeBoard: 게시글이 없는 경우 예외 발생")
    void unlikeFreeBoard_throwsException_whenBoardNotFound() {
        // given
        when(freeBoardDao.findById(boardId)).thenReturn(Optional.empty());

        // when & then
        assertThrows(BusinessLogicException.class, () -> {
            userLikeBoardService.unlikeFreeBoard(boardId, userDetails);
        });
        verify(userLikeBoardDao, never()).delete(anyLong(), anyLong());
    }
}
