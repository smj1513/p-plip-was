package com.pplip.domain.file.usecase;

import com.pplip.domain.auth.persistence.entity.Account;
import com.pplip.domain.file.api.response.FileResponse;
import com.pplip.domain.file.persistence.dao.*;
import com.pplip.domain.file.persistence.entity.*;
import com.pplip.domain.file.utils.FilePropertyProvider;
import com.pplip.domain.file.utils.FileStorage;
import com.pplip.domain.user.persistence.entity.User;
import com.pplip.global.exception.BusinessLogicException;
import io.jsonwebtoken.lang.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("FileServiceImpl 테스트")
class FileServiceImplTest {

    private FileServiceImpl fileService; // InjectMocks 대신 직접 생성자 주입 사용

    @Mock
    private FilePropertyProvider provider;

    @Mock
    private FileStorage fileStorage;

    // --- [변경 1] 구체적인 DAO들을 Mock으로 선언 ---
    @Mock
    private ProfileImagePropertyDao profileDao; // 단건 처리용이라고 가정

    @Mock
    private FreeBoardImagePropertyDao freeBoardDao; // 배치 지원용이라고 가정
    @Mock
    private NoticeBoardImagePropertyDao noticeBoardDao; // 배치 지원용이라고 가정
    @Mock
    private ReviewImagePropertyDao reviewDao; // 배치 지원용이라고 가정

    private User testUser;
    private Account testUserDetails;

    @BeforeEach
    void setUp() {
        testUser = User.builder().id(1L).name("testuser").build();
        testUserDetails = new Account();
        testUserDetails.setUserId(testUser.getId());
        testUserDetails.setEmail("test@test.com");

        // --- [변경 2] 각 DAO가 지원하는 ImageType 설정 (Map Key 생성용) ---
        // lenient()를 사용하는 이유는 테스트 메서드마다 사용하지 않는 DAO의 설정에 대해 경고를 무시하기 위함입니다.
        lenient().when(profileDao.supports()).thenReturn(ImageType.PROFILE);
        lenient().when(freeBoardDao.supports()).thenReturn(ImageType.FREE_BOARD);
        lenient().when(noticeBoardDao.supports()).thenReturn(ImageType.NOTICE);
        lenient().when(reviewDao.supports()).thenReturn(ImageType.REVIEW);
        // --- [변경 3] Service 생성 및 Mock 리스트 주입 ---
        // 단건 처리용 DAO 리스트와 배치 지원 DAO 리스트를 구분하여 주입합니다.
        // (가정: Profile은 단건, 나머지는 배치 지원으로 가정하고 분류했습니다. 실제 상속 구조에 맞게 조정하세요)
        fileService = new FileServiceImpl(
                provider,
                fileStorage,
                List.of(freeBoardDao, noticeBoardDao, reviewDao, profileDao),
                List.of(freeBoardDao, noticeBoardDao, reviewDao) // BatchSupportFilePropertyDao 리스트
        );
    }@Test
    @DisplayName("성공: 단일 파일 저장 (Profile - 단건 DAO 사용)")
    void saveFile_Success() {
        // Given
        MultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", "test data".getBytes());
        ImageType imageType = ImageType.PROFILE;
        ProfileImageProperty fileProperty = ProfileImageProperty.builder()
                .id(1L)
                .path("/path/to/test.jpg")
                .originFileName("test.jpg")
                .contentType("image/jpeg")
                .size(100L)
                .uploaderId(testUser.getId())
                .build();

        when(provider.create(anyString(), anyString(), anyLong(), anyLong(), eq(imageType))).thenReturn(fileProperty);
        when(profileDao.insert(any())).thenReturn(1); // ProfileDao가 호출되어야 함
        doNothing().when(fileStorage).store(any(), any());

        // When
        FileResponse response = fileService.saveFile(file, imageType, testUserDetails);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(fileProperty.getId());

        // [검증] 구체적으로 profileDao가 호출되었는지 확인
        verify(profileDao).insert(fileProperty);
        verify(freeBoardDao, never()).insert(any()); // 다른 DAO는 호출되지 않아야 함
    }

    @Test
    @DisplayName("실패: 지원하지 않는 이미지 타입으로 파일 저장")
    void saveFile_Fail_UnsupportedType() {
        // Given
        MultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", "test data".getBytes());

        // Mock에 등록되지 않은 타입을 사용 (예: null이거나 설정하지 않은 타입)
        // 여기서는 THUMBNAIL이라는 타입이 있고, 이에 대한 DAO는 주입하지 않았다고 가정

        when(provider.create(anyString(), anyString(), anyLong(), anyLong(), any()))
                .thenReturn(ProfileImageProperty.builder().path("path").build());

        // When & Then
        assertThrows(BusinessLogicException.class, () -> {
            fileService.saveFile(file, null, testUserDetails);
        });
    }

    @Test
    @DisplayName("성공: 파일 삭제 (Profile)")
    void remove_Success() {
        // Given
        Long fileId = 1L;
        ImageType imageType = ImageType.PROFILE;
        ProfileImageProperty fileProperty = ProfileImageProperty.builder()
                .id(fileId)
                .uploaderId(testUser.getId())
                .path("/path/to/file")
                .build();

        // Service 생성자에서 이미 supports() 설정을 마쳤으므로 여기선 생략 가능
        when(profileDao.findById(fileId)).thenReturn(Optional.ofNullable(fileProperty));
        when(profileDao.delete(fileId)).thenReturn(1);
        doNothing().when(fileStorage).delete(any());

        // When
        FileResponse response = fileService.remove(fileId, imageType, testUserDetails);

        // Then
        assertThat(response).isNotNull();
        verify(fileStorage).delete(fileProperty.getPath());
        verify(profileDao).delete(fileId);
    }

    @Test
    @DisplayName("실패: 다른 사용자의 파일 삭제 시도")
    void remove_Fail_InvalidUser() {
        // Given
        Long fileId = 1L;
        ImageType imageType = ImageType.PROFILE;
        ProfileImageProperty fileProperty = ProfileImageProperty.builder().id(fileId).uploaderId(999L).build(); // 다른 ID

        when(profileDao.findById(fileId)).thenReturn(Optional.of(fileProperty));

        // When & Then
        assertThrows(BusinessLogicException.class, () -> {
            fileService.remove(fileId, imageType, testUserDetails);
        });
    }

    @Test
    @DisplayName("성공: 다중 파일 저장 (Review - 배치 DAO 사용)")
    void saveFiles_Success() {
        // Given
        List<MultipartFile> files = List.of(
                new MockMultipartFile("file1", "test1.jpg", "image/jpeg", "test data 1".getBytes()),
                new MockMultipartFile("file2", "test2.jpg", "image/jpeg", "test data 2".getBytes())
        );
        ImageType imageType = ImageType.REVIEW;

        FileProperty fp1 = ReviewImageProperty.builder().id(1L).path("p1").build();
        FileProperty fp2 = ReviewImageProperty.builder().id(2L).path("p2").build();

        when(provider.create(eq("test1.jpg"), anyString(), anyLong(), anyLong(), eq(imageType))).thenReturn(fp1);
        when(provider.create(eq("test2.jpg"), anyString(), anyLong(), anyLong(), eq(imageType))).thenReturn(fp2);

        // reviewDao가 호출될 것을 설정 (Map에 이미 등록됨)
        // insertAll은 void 혹은 int 리턴일 수 있음. 여기선 void라고 가정하거나 int면 returns 설정 필요
        // when(reviewDao.insertAll(anyList())).thenReturn(2);

        // When
        List<FileResponse> responses = fileService.saveFiles(files, imageType, testUserDetails);

        // Then
        assertThat(responses).hasSize(2);
        verify(fileStorage, times(2)).store(any(), any());

        // [검증] ReviewDao의 insertAll이 호출되었는지 확인
        verify(reviewDao).insertAll(anyList());
    }

    @Test
    @DisplayName("성공: 파일 정보 일괄 업데이트 (FreeBoard)")
    void bulkUpdateFiles_Success() {
        // Given
        List<Long> fileIds = List.of(1L, 2L);
        Long refId = 100L;
        ImageType imageType = ImageType.FREE_BOARD; // FreeBoard 타입 테스트

        when(freeBoardDao.bulkUpdate(fileIds, refId)).thenReturn(2);

        // When
        int updatedCount = fileService.bulkUpdateFiles(fileIds, imageType, refId);

        // Then
        assertThat(updatedCount).isEqualTo(2);
        verify(freeBoardDao).bulkUpdate(fileIds, refId);
        verify(reviewDao, never()).bulkUpdate(anyList(), anyLong()); // 다른 DAO 호출 X 확인
    }
}