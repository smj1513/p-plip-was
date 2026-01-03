package com.pplip.domain.board.freeboard.usecase.impl;

import com.pplip.domain.auth.persistence.entity.Account;
import com.pplip.domain.board.freeboard.api.request.FreeBoardCommentRequest;
import com.pplip.domain.board.freeboard.api.response.FreeBoardCommentResponse;
import com.pplip.domain.board.freeboard.persistence.dao.FreeBoardCommentDao;
import com.pplip.domain.board.freeboard.persistence.entity.FreeComment;
import com.pplip.global.api.code.ErrorCode;
import com.pplip.global.exception.BusinessLogicException;
import com.pplip.global.page.Page;
import com.pplip.global.page.PageRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("FreeBoardCommentServiceImpl 테스트")
class FreeBoardCommentServiceImplTest {

    @Mock
    private FreeBoardCommentDao freeBoardCommentDao;

    @InjectMocks
    private FreeBoardCommentServiceImpl freeBoardCommentService;

    private final Long boardId = 1L;
    private final Long userId = 100L;
    private final Long commentId = 1L;

    private Account createTestAccount(Long userId) {
        return Account.builder().userId(userId).build();
    }

    @Nested
    @DisplayName("getComment: 댓글 조회")
    class GetComment {

        @Test
        @DisplayName("성공: 특정 게시글의 댓글 목록을 페이징하여 반환한다")
        void getComment_Success() {
            // Given
            PageRequest pageRequest = new PageRequest(1, 10);
            List<FreeBoardCommentResponse.Retrieve> comments = List.of(new FreeBoardCommentResponse.Retrieve());
            int totalCount = comments.size();

            given(freeBoardCommentDao.findAllByBoardId(boardId, pageRequest)).willReturn(comments);
            given(freeBoardCommentDao.countAllByBoardId(boardId)).willReturn(totalCount);

            // When
            Page<FreeBoardCommentResponse.Retrieve> result = freeBoardCommentService.getComment(boardId, pageRequest);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getList()).hasSize(totalCount);
            assertThat(result.getTotalCount()).isEqualTo(totalCount);
            verify(freeBoardCommentDao, times(1)).findAllByBoardId(boardId, pageRequest);
            verify(freeBoardCommentDao, times(1)).countAllByBoardId(boardId);
        }

        @Test
        @DisplayName("엣지 케이스: 댓글이 없는 경우 빈 페이지를 반환한다")
        void getComment_Edge_NoComments() {
            // Given
            PageRequest pageRequest = new PageRequest(1, 10);
            given(freeBoardCommentDao.findAllByBoardId(boardId, pageRequest)).willReturn(Collections.emptyList());
            given(freeBoardCommentDao.countAllByBoardId(boardId)).willReturn(0);

            // When
            Page<FreeBoardCommentResponse.Retrieve> result = freeBoardCommentService.getComment(boardId, pageRequest);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getList()).isEmpty();
            assertThat(result.getTotalCount()).isEqualTo(0);
            verify(freeBoardCommentDao, times(1)).findAllByBoardId(boardId, pageRequest);
            verify(freeBoardCommentDao, times(1)).countAllByBoardId(boardId);
        }
    }

    @Nested
    @DisplayName("postComment: 댓글 작성")
    class PostTodoComment {
        @Test
        @DisplayName("성공: 새로운 댓글을 작성하고 작성된 댓글 정보를 반환한다")
        void postComment_Success() {
            // Given
            Long commentId = 1L; // 테스트에 사용할 ID 정의
            Long boardId = 100L; // 테스트에 사용할 Board ID

            FreeBoardCommentRequest.Create createRequest = new FreeBoardCommentRequest.Create("새 댓글 내용");
            UserDetails loginUser = createTestAccount(userId);
            FreeBoardCommentResponse.Detail expectedResponse = FreeBoardCommentResponse.Detail.builder()
                    .id(commentId)
                    .content(createRequest.getContent())
                    .build();

            willAnswer(invocation -> {
                FreeComment comment = invocation.getArgument(0);
                // comment 객체의 'id' 필드에 commentId 값을 주입 (Setter가 없다면 ReflectionTestUtils 사용)
                ReflectionTestUtils.setField(comment, "id", commentId);
                return 1; // insert의 리턴값 (영향받은 행 수)
            }).given(freeBoardCommentDao).insert(any(FreeComment.class));

            // [핵심 수정 2] 이제 서비스 로직이 commentId(1L)로 조회를 시도할 것이므로, eq(commentId)로 매칭합니다.
            given(freeBoardCommentDao.findByIdToDto(eq(commentId))).willReturn(Optional.of(expectedResponse));

            // When
            FreeBoardCommentResponse.Detail result = freeBoardCommentService.postComment(boardId, createRequest, loginUser);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(commentId);
            assertThat(result.getContent()).isEqualTo(createRequest.getContent());

            verify(freeBoardCommentDao, times(1)).insert(any(FreeComment.class));
            // verify도 구체적인 ID 호출을 확인하도록 변경하면 더 정확합니다.
            verify(freeBoardCommentDao, times(1)).findByIdToDto(eq(commentId));
        }
    }

    @Nested
    @DisplayName("deleteComment: 댓글 삭제")
    class DeleteComment {

        @Test
        @DisplayName("성공: 작성자가 자신의 댓글을 삭제한다")
        void deleteComment_Success() {
            // Given
            UserDetails loginUser = createTestAccount(userId);
            FreeBoardCommentResponse.Detail commentDetail = FreeBoardCommentResponse.Detail.builder().id(commentId).userId(userId).build();
            given(freeBoardCommentDao.findByIdToDto(commentId)).willReturn(Optional.of(commentDetail));
            given(freeBoardCommentDao.delete(commentId)).willReturn(1);

            // When
            assertDoesNotThrow(() -> freeBoardCommentService.deleteComment(commentId, loginUser));

            // Then
            verify(freeBoardCommentDao, times(1)).findByIdToDto(commentId);
            verify(freeBoardCommentDao, times(1)).delete(commentId);
        }

        @Test
        @DisplayName("실패: 존재하지 않는 댓글 삭제 시 예외가 발생한다")
        void deleteComment_Fail_NotFound() {
            // Given
            UserDetails loginUser = createTestAccount(userId);
            given(freeBoardCommentDao.findByIdToDto(commentId)).willReturn(Optional.empty());

            // When & Then
            BusinessLogicException exception = assertThrows(BusinessLogicException.class, () -> {
                freeBoardCommentService.deleteComment(commentId, loginUser);
            });
            assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.COMMENT_NOT_FOUND);
            verify(freeBoardCommentDao, times(1)).findByIdToDto(commentId);
            verify(freeBoardCommentDao, times(0)).delete(any(Long.class));
        }

        @Test
        @DisplayName("실패: 작성자가 아닌 사용자가 삭제 시 예외가 발생한다")
        void deleteComment_Fail_Forbidden() {
            // Given
            Long otherUserId = 999L;
            UserDetails loginUser = createTestAccount(otherUserId);
            FreeBoardCommentResponse.Detail commentDetail = FreeBoardCommentResponse.Detail.builder().id(commentId).userId(userId).build();
            given(freeBoardCommentDao.findByIdToDto(commentId)).willReturn(Optional.of(commentDetail));

            // When & Then
            BusinessLogicException exception = assertThrows(BusinessLogicException.class, () -> {
                freeBoardCommentService.deleteComment(commentId, loginUser);
            });
            assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.FORBIDDEN);
            verify(freeBoardCommentDao, times(1)).findByIdToDto(commentId);
            verify(freeBoardCommentDao, times(0)).delete(any(Long.class));
        }
    }

    @Nested
    @DisplayName("updateComment: 댓글 수정")
    class ToDoUpdatedComment {

        @Test
        @DisplayName("성공: 작성자가 자신의 댓글을 수정한다")
        void updateComment_Success() {
            // Given
            UserDetails loginUser = createTestAccount(userId);
            FreeBoardCommentRequest.Update updateRequest = new FreeBoardCommentRequest.Update("수정된 내용");
            FreeBoardCommentResponse.Detail originalCommentDetail = FreeBoardCommentResponse.Detail.builder().id(commentId).userId(userId).content("원본 내용").build();
            FreeComment originalComment = FreeComment.builder().id(commentId).authorId(userId).content("원본 내용").build();
            FreeBoardCommentResponse.Detail updatedCommentDetail = FreeBoardCommentResponse.Detail.builder().id(commentId).userId(userId).content(updateRequest.getContent()).build();

            given(freeBoardCommentDao.findByIdToDto(commentId)).willReturn(Optional.of(originalCommentDetail)).willReturn(Optional.of(updatedCommentDetail));
            given(freeBoardCommentDao.findById(commentId)).willReturn(Optional.of(originalComment));
            given(freeBoardCommentDao.update(any(FreeComment.class))).willReturn(1);

            // When
            FreeBoardCommentResponse.Detail result = freeBoardCommentService.updateComment(commentId, updateRequest, loginUser);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getContent()).isEqualTo(updateRequest.getContent());
            verify(freeBoardCommentDao, times(2)).findByIdToDto(commentId);
            verify(freeBoardCommentDao, times(1)).findById(commentId);
            verify(freeBoardCommentDao, times(1)).update(any(FreeComment.class));
        }

        @Test
        @DisplayName("실패: 존재하지 않는 댓글 수정 시 예외가 발생한다")
        void updateComment_Fail_NotFound() {
            // Given
            UserDetails loginUser = createTestAccount(userId);
            FreeBoardCommentRequest.Update updateRequest = new FreeBoardCommentRequest.Update("수정된 내용");
            given(freeBoardCommentDao.findByIdToDto(commentId)).willReturn(Optional.empty());

            // When & Then
            BusinessLogicException exception = assertThrows(BusinessLogicException.class, () -> {
                freeBoardCommentService.updateComment(commentId, updateRequest, loginUser);
            });

            assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.COMMENT_NOT_FOUND);
            verify(freeBoardCommentDao, times(1)).findByIdToDto(commentId);
            verify(freeBoardCommentDao, times(0)).findById(any(Long.class));
            verify(freeBoardCommentDao, times(0)).update(any(FreeComment.class));
        }

        @Test
        @DisplayName("실패: 작성자가 아닌 사용자가 수정 시 예외가 발생한다")
        void updateComment_Fail_Forbidden() {
            // Given
            Long otherUserId = 999L;
            UserDetails loginUser = createTestAccount(otherUserId);
            FreeBoardCommentRequest.Update updateRequest = new FreeBoardCommentRequest.Update("수정된 내용");
            FreeBoardCommentResponse.Detail originalCommentDetail = FreeBoardCommentResponse.Detail.builder().id(commentId).userId(userId).build();
            given(freeBoardCommentDao.findByIdToDto(commentId)).willReturn(Optional.of(originalCommentDetail));

            // When & Then
            BusinessLogicException exception = assertThrows(BusinessLogicException.class, () -> {
                freeBoardCommentService.updateComment(commentId, updateRequest, loginUser);
            });

            assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.FORBIDDEN);
            verify(freeBoardCommentDao, times(1)).findByIdToDto(commentId);
            verify(freeBoardCommentDao, times(0)).findById(any(Long.class));
            verify(freeBoardCommentDao, times(0)).update(any(FreeComment.class));
        }
    }
}
