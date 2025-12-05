package com.pplip.domain.file.api.controller;

import com.pplip.domain.file.api.response.FileResponse;
import com.pplip.domain.file.persistence.entity.ImageType;
import com.pplip.domain.file.usecase.FileService;
import com.pplip.global.api.code.SuccessCode;
import com.pplip.global.api.response.CommonResponse;
import com.pplip.global.docs.FileDocsController;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 파일 관련 API 요청을 처리하는 컨트롤러
 */
@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController implements FileDocsController {
	private final FileService fileService;

	/**
	 * 이미지 파일을 업로드합니다.
	 *
	 * @param multipartFile 업로드할 이미지 파일
	 * @param imageType     이미지 유형 (e.g., PROFILE, BOARD)
	 * @return 업로드된 파일 정보와 함께 성공 응답을 반환
	 */
	@PostMapping(path = "/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@Override
	public CommonResponse<FileResponse> upload(
			@RequestPart("file")
			MultipartFile multipartFile,
			@RequestParam
			ImageType imageType,
			@AuthenticationPrincipal
			UserDetails userDetails
	) {
		return CommonResponse.success(SuccessCode.CREATED, fileService.saveFile(multipartFile, imageType, userDetails));
	}

	@PostMapping(path = "/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public CommonResponse<List<FileResponse>> uploads(
			@RequestPart("files")
			List<MultipartFile> files,
			@RequestParam
			ImageType imageType,
			@AuthenticationPrincipal
			UserDetails userDetails) {
        return CommonResponse.success(SuccessCode.CREATED, fileService.saveFiles(files, imageType, userDetails));
	}


	/**
	 * 이미지 파일을 삭제합니다.
	 *
	 * @param id 삭제할 이미지 id
	 * @return 삭제된 파일 정보와 함께 성공 응답을 반환
	 */
	@DeleteMapping("/images/{id}")
	@Override
	public CommonResponse<FileResponse> deleteFile(@PathVariable Long id,
	                                               @AuthenticationPrincipal UserDetails principal,
	                                               @RequestParam ImageType imageType) {
		return CommonResponse.success(SuccessCode.REMOVED, fileService.remove(id, imageType, principal));
	}
}
