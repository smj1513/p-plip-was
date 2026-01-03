package com.pplip.domain.board.freeboard.usecase.impl;

import com.pplip.domain.auth.persistence.entity.Account;
import com.pplip.domain.board.freeboard.api.request.FreeBoardRequest;
import com.pplip.domain.board.freeboard.api.response.FreeBoardResponse;
import com.pplip.domain.board.freeboard.persistence.dao.FreeBoardDao;
import com.pplip.domain.board.freeboard.persistence.entity.FreeBoard;
import com.pplip.domain.board.freeboard.persistence.entity.FreeBoardSort;
import com.pplip.domain.file.persistence.dao.FreeBoardImagePropertyDao;
import com.pplip.domain.file.usecase.FileService;
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
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willAnswer;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("FreeBoardServiceImpl 테스트")
class FreeBoardServiceImplTest {

    @InjectMocks
    private FreeBoardServiceImpl freeBoardService;

    @Mock
    private FreeBoardDao freeBoardDao;

    @Mock
    private FreeBoardImagePropertyDao freeBoardImagePropertyDao;

    @Mock
    private FileService fileService;

    private Account account;
    private FreeBoard freeBoard;

    @BeforeEach
    void setUp() {
        account = Account.builder().userId(1L).email("test@test.com").build();
        freeBoard = FreeBoard.builder()
                .id(1L)
                .authorId(1L)
                .title("테스트 제목")
                .content("테스트 내용")
                .isRemoved(false)
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Nested
    @DisplayName("remove 메소드")
    class RemoveTest {
        @Test
        @DisplayName("성공: 작성자가 게시글을 삭제한다")
        void given_BoardIdAndPrincipal_when_RemoveBoard_then_Success() {
            // given
            given(freeBoardDao.findById(freeBoard.getId())).willReturn(Optional.of(freeBoard));

            // when
            FreeBoardResponse.Remove response = freeBoardService.remove(freeBoard.getId(), account);

            // then
            assertThat(response.getId()).isEqualTo(freeBoard.getId());
            assertThat(response.getTitle()).isEqualTo(freeBoard.getTitle());
            assertThat(freeBoard.isRemoved()).isTrue();
            verify(freeBoardDao).delete(freeBoard.getId());
        }

        @Test
        @DisplayName("실패: 존재하지 않는 게시글을 삭제하려고 하면 BusinessLogicException이 발생한다")
        void given_NonexistentBoardId_when_RemoveBoard_then_ThrowBusinessLogicException() {
            // given
            Long boardId = 999L;
            given(freeBoardDao.findById(boardId)).willReturn(Optional.empty());

            // when & then
            BusinessLogicException exception = assertThrows(BusinessLogicException.class, () -> freeBoardService.remove(boardId, account));
            assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.BOARD_NOT_FOUND_ERROR);
        }

        @Test
        @DisplayName("실패: 작성자가 아닌 사람이 삭제하려고 하면 BusinessLogicException이 발생한다")
        void given_OtherUserPrincipal_when_RemoveBoard_then_ThrowBusinessLogicException() {
            // given
            Account otherAccount = Account.builder().userId(2L).build();
            given(freeBoardDao.findById(freeBoard.getId())).willReturn(Optional.of(freeBoard));

            // when & then
            BusinessLogicException exception = assertThrows(BusinessLogicException.class, () -> freeBoardService.remove(freeBoard.getId(), otherAccount));
            assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.FORBIDDEN);
        }
    }


    @Nested
    @DisplayName("modifyFreeBoard 메소드")
    class ModifyFreeBoardTest {

        @Test
        @DisplayName("성공: 게시글을 성공적으로 수정한다")
        void given_UpdateRequestAndPrincipal_when_ModifyBoard_then_Success() {
            // given
            FreeBoardRequest.BoardUpdate updateRequest = FreeBoardRequest.BoardUpdate.builder()
                    .title("수정된 제목")
                    .content("수정된 내용")
                    .images(Collections.emptyList())
                    .build();
            given(freeBoardDao.findById(freeBoard.getId())).willReturn(Optional.of(freeBoard));
            given(freeBoardDao.findByIdToDto(freeBoard.getId())).willReturn(Optional.of(new FreeBoardResponse.Detail()));

            // when
            freeBoardService.modifyFreeBoard(updateRequest, freeBoard.getId(), account);

            // then
            assertThat(freeBoard.getTitle()).isEqualTo(updateRequest.getTitle());
            assertThat(freeBoard.getContent()).isEqualTo(updateRequest.getContent());
            verify(freeBoardDao).update(any(FreeBoard.class));
        }

        @Test
        @DisplayName("실패: 존재하지 않는 게시글을 수정하려고 하면 BusinessLogicException이 발생한다")
        void given_NonExistentBoard_when_Modify_then_ThrowException() {
            // given
            FreeBoardRequest.BoardUpdate updateRequest = new FreeBoardRequest.BoardUpdate();
            given(freeBoardDao.findById(any(Long.class))).willReturn(Optional.empty());

            // when & then
            assertThrows(BusinessLogicException.class, () -> freeBoardService.modifyFreeBoard(updateRequest, 999L, account));
        }

        @Test
        @DisplayName("실패: 작성자가 아닌 사람이 수정하려고 하면 BusinessLogicException이 발생한다")
        void given_OtherUser_when_Modify_then_ThrowException() {
            // given
            FreeBoardRequest.BoardUpdate updateRequest = new FreeBoardRequest.BoardUpdate();
            UserDetails otherUser = Account.builder().userId(2L).build();
            given(freeBoardDao.findById(freeBoard.getId())).willReturn(Optional.of(freeBoard));

            // when & then
            assertThrows(BusinessLogicException.class, () -> freeBoardService.modifyFreeBoard(updateRequest, freeBoard.getId(), otherUser));
        }
    }


    @Nested
    @DisplayName("post 메소드")
    class PostTodoTest {
        @Test
        @DisplayName("성공: 새로운 게시글을 작성한다")
        void given_PostRequestAndPrincipal_when_PostBoard_then_Success() {
            // given
            FreeBoardRequest.BoardPost postRequest = FreeBoardRequest.BoardPost.builder()
                    .title("새로운 제목")
                    .content("새로운 내용")
                    .ids(List.of(1L, 2L))
                    .build();

            // [수정 1] insert 호출 시 파라미터로 넘어온 FreeBoard 객체에 ID(1L)를 강제로 주입 (DB Auto Increment 흉내)
            willAnswer(invocation -> {
                FreeBoard board = invocation.getArgument(0);
                ReflectionTestUtils.setField(board, "id", 1L); // ID 세팅
                return 1;
            }).given(freeBoardDao).insert(any(FreeBoard.class));

            // [수정 2] 위에서 ID가 1L로 세팅되었으므로, findById(1L)이 호출될 것을 기대하고 Stubbing
            given(freeBoardDao.findByIdToDto(1L)).willReturn(Optional.of(new FreeBoardResponse.Detail()));

            // when
            freeBoardService.post(postRequest, account);

            // then
            verify(freeBoardDao).insert(any(FreeBoard.class));
            // [수정 3] insert 후 ID가 1L이 되었으므로, bulkUpdate도 1L로 호출되었는지 검증
            verify(freeBoardImagePropertyDao).bulkUpdate(postRequest.getIds(), 1L);
        }
    }

    @Nested
    @DisplayName("retrieveDetail 메소드")
    class RetrievePlanToDoDetailTest {
        @Test
        @DisplayName("성공: 게시글 상세 정보를 조회한다")
        void given_BoardId_when_RetrieveDetail_then_Success() {
            // given
            given(freeBoardDao.findByIdToDto(freeBoard.getId())).willReturn(Optional.of(new FreeBoardResponse.Detail()));

            // when
            freeBoardService.retrieveDetail(freeBoard.getId());

            // then
            verify(freeBoardDao).findByIdToDto(freeBoard.getId());
        }

        @Test
        @DisplayName("실패: 존재하지 않는 게시글을 조회하면 BusinessLogicException이 발생한다")
        void given_NonExistentBoardId_when_RetrieveDetail_then_ThrowBusinessLogicException() {
            // given
            Long boardId = 999L;
            given(freeBoardDao.findByIdToDto(boardId)).willReturn(Optional.empty());

            // when & then
            BusinessLogicException exception = assertThrows(BusinessLogicException.class, () -> freeBoardService.retrieveDetail(boardId));
            assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.BOARD_NOT_FOUND_ERROR);
        }
    }

    @Nested
    @DisplayName("retrieve 메소드")
    class RetrieveTest {

        @Test
        @DisplayName("성공: 게시글 목록을 조회한다")
        void given_PageRequest_when_Retrieve_then_Success() {
            // given
            PageRequest pageRequest = new PageRequest(1, 10);
            List<FreeBoardResponse.BoardList> boardList = Collections.singletonList(new FreeBoardResponse.BoardList());
            given(freeBoardDao.findAll(pageRequest, FreeBoardSort.LATEST)).willReturn(boardList);
            given(freeBoardDao.countAll()).willReturn(1);

            // when
            Page<FreeBoardResponse.BoardList> result = freeBoardService.retrieve(pageRequest, FreeBoardSort.LATEST);

            // then
            assertThat(result.getList()).hasSize(1);
            assertThat(result.getTotalCount()).isEqualTo(1);
            verify(freeBoardDao).findAll(pageRequest, FreeBoardSort.LATEST);
            verify(freeBoardDao).countAll();
        }
    }
}
