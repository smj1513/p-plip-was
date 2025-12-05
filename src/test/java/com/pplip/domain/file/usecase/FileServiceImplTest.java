package com.pplip.domain.file.usecase;

import com.pplip.domain.auth.persistence.entity.Account;
import com.pplip.domain.file.api.response.FileResponse;
import com.pplip.domain.file.persistence.dao.BatchSupportFilePropertyDao;
import com.pplip.domain.file.persistence.dao.FilePropertyDao;
import com.pplip.domain.file.persistence.entity.FileProperty;
import com.pplip.domain.file.persistence.entity.ImageType;
import com.pplip.domain.file.persistence.entity.ProfileImageProperty;
import com.pplip.domain.file.utils.FilePropertyProvider;
import com.pplip.domain.file.utils.FileStorage;
import com.pplip.domain.user.persistence.entity.User;
import com.pplip.global.exception.BusinessLogicException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("FileServiceImpl 테스트")
class FileServiceImplTest {

    @InjectMocks
    private FileServiceImpl fileService;

    @Mock
    private FilePropertyProvider provider;

    @Mock
    private FileStorage fileStorage;
    
    @Mock
    private FilePropertyDao<FileProperty> filePropertyDao;

    @Mock
    private BatchSupportFilePropertyDao<FileProperty> batchSupportFilePropertyDao;

    private User testUser;
    private Account testUserDetails;

    @BeforeEach
    void setUp() {
        testUser = User.builder().id(1L).name("testuser").build();
        testUserDetails = new Account();
        testUserDetails.setUserId(testUser.getId());
        testUserDetails.setUserId(testUser.getId());
        testUserDetails.setEmail("test@test.com");
        
        // Since the service uses lists of DAOs, we need to re-inject them before each test.
        fileService = new FileServiceImpl(provider, fileStorage, List.of(filePropertyDao), List.of(batchSupportFilePropertyDao));
    }

    @Test
    @DisplayName("성공: 단일 파일 저장")
    void saveFile_Success() {
        // Given
        MultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", "test data".getBytes());
        ImageType imageType = ImageType.PROFILE;
        FileProperty fileProperty = ProfileImageProperty.builder().id(1L).path("/path/to/test.jpg").originFileName("test.jpg").contentType("image/jpeg").size(100L).uploaderId(testUser.getId()).build();

        when(provider.create(anyString(), anyString(), anyLong(), anyLong(), any(ImageType.class))).thenReturn(fileProperty);
        when(filePropertyDao.supports(imageType)).thenReturn(true);
        when(filePropertyDao.insert(any())).thenReturn(1);
        doNothing().when(fileStorage).store(any(), any());

        // When
        FileResponse response = fileService.saveFile(file, imageType, testUserDetails);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(fileProperty.getId());
        verify(fileStorage).store(file, fileProperty.getPath());
        verify(filePropertyDao).insert(fileProperty);
    }
    
    @Test
    @DisplayName("실패: 지원하지 않는 이미지 타입으로 파일 저장")
    void saveFile_Fail_UnsupportedType() {
        // Given
        MultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", "test data".getBytes());
        ImageType imageType = ImageType.PROFILE;


        when(provider.create(anyString(), anyString(), anyLong(), anyLong(), any(ImageType.class))).thenReturn(ProfileImageProperty.builder().build());
        when(filePropertyDao.supports(imageType)).thenReturn(false);

        // When & Then
        assertThrows(BusinessLogicException.class, () -> {
            fileService.saveFile(file, imageType, testUserDetails);
        });
    }

    @Test
    @DisplayName("성공: 파일 삭제")
    void remove_Success() {
        // Given
        Long fileId = 1L;
        ImageType imageType = ImageType.PROFILE;
        FileProperty fileProperty = ProfileImageProperty.builder().id(fileId).uploaderId(testUser.getId()).path("/path/to/file").build();

        when(filePropertyDao.supports(imageType)).thenReturn(true);
        when(filePropertyDao.findById(fileId)).thenReturn(Optional.of(fileProperty));
        when(filePropertyDao.delete(fileId)).thenReturn(1);
        doNothing().when(fileStorage).delete(any());

        // When
        FileResponse response = fileService.remove(fileId, imageType, testUserDetails);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(fileId);
        verify(fileStorage).delete(fileProperty.getPath());
    }

    @Test
    @DisplayName("실패: 다른 사용자의 파일 삭제 시도")
    void remove_Fail_InvalidUser() {
        // Given
        Long fileId = 1L;
        ImageType imageType = ImageType.PROFILE;
        FileProperty fileProperty = ProfileImageProperty.builder().id(fileId).uploaderId(2L).build(); // Different user ID

        when(filePropertyDao.supports(imageType)).thenReturn(true);
        when(filePropertyDao.findById(fileId)).thenReturn(Optional.of(fileProperty));

        // When & Then
        assertThrows(BusinessLogicException.class, () -> {
            fileService.remove(fileId, imageType, testUserDetails);
        });
    }
    
    @Test
    @DisplayName("성공: 다중 파일 저장")
    void saveFiles_Success() {
        // Given
        List<MultipartFile> files = List.of(
            new MockMultipartFile("file1", "test1.jpg", "image/jpeg", "test data 1".getBytes()),
            new MockMultipartFile("file2", "test2.jpg", "image/jpeg", "test data 2".getBytes())
        );
        ImageType imageType = ImageType.REVIEW;
        FileProperty fp1 = ProfileImageProperty.builder().id(1L).build();
        FileProperty fp2 = ProfileImageProperty.builder().id(2L).build();

        when(provider.create(eq("test1.jpg"), anyString(), anyLong(), anyLong(), any(ImageType.class))).thenReturn(fp1);
        when(provider.create(eq("test2.jpg"), anyString(), anyLong(), anyLong(), any(ImageType.class))).thenReturn(fp2);
        when(batchSupportFilePropertyDao.supports(imageType)).thenReturn(true);
        
        // When
        List<FileResponse> responses = fileService.saveFiles(files, imageType, testUserDetails);

        // Then
        assertThat(responses).hasSize(2);
        verify(fileStorage, times(2)).store(any(), any());
        verify(batchSupportFilePropertyDao).insertAll(List.of(fp1, fp2));
    }
    
    @Test
    @DisplayName("성공: 파일 정보 일괄 업데이트")
    void bulkUpdateFiles_Success() {
        // Given
        List<Long> fileIds = List.of(1L, 2L);
        Long refId = 100L;
        ImageType imageType = ImageType.REVIEW;
        
        when(batchSupportFilePropertyDao.supports(imageType)).thenReturn(true);
        when(batchSupportFilePropertyDao.bulkUpdate(fileIds, refId)).thenReturn(2);

        // When
        int updatedCount = fileService.bulkUpdateFiles(fileIds, imageType, refId);

        // Then
        assertThat(updatedCount).isEqualTo(2);
        verify(batchSupportFilePropertyDao).bulkUpdate(fileIds, refId);
    }
}