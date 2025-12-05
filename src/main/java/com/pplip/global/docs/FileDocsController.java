package com.pplip.global.docs;

import com.pplip.domain.file.api.response.FileResponse;
import com.pplip.domain.file.persistence.entity.ImageType;
import com.pplip.global.api.response.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;

/**
 * 파일 관련 API 명세를 정의하는 인터페이스
 */
@Tag(name = "파일 API", description = "파일 api")
public interface FileDocsController {

    /**
     * 이미지 파일을 업로드합니다.
     *
     * @param multipartFile 업로드할 이미지 파일
     * @param imageType     이미지 유형 (e.g., PROFILE, BOARD)
     * @param userDetails 로그인 중인 사용자
     * @return 업로드된 파일 정보와 함께 성공 응답을 반환
     */
    @Operation(summary = "이미지 파일 업로드")
    @ApiResponse(responseCode = "201", description = "생성")
    CommonResponse<FileResponse> upload(MultipartFile multipartFile, ImageType imageType, UserDetails userDetails);

    /**
     * 이미지 파일을 삭제합니다.
     *
     * @param id 삭제할 이미지 id
     * @return 삭제된 파일 정보와 함께 성공 응답을 반환
     */
    @Operation(summary = "이미지 파일 삭제")
    @ApiResponse(responseCode = "203", description = "성공")
    CommonResponse<FileResponse> deleteFile(Long id, UserDetails principal, ImageType imageType);
}
