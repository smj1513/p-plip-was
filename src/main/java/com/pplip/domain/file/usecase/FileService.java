package com.pplip.domain.file.usecase;

import com.pplip.domain.file.api.response.FileResponse;
import com.pplip.domain.file.persistence.entity.ImageType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 파일 관련 비즈니스 로직을 정의하는 인터페이스
 */
public interface FileService {
	/**
	 * 단일 파일을 저장합니다.
	 *
	 * @param file 저장할 파일
	 * @param imageType 이미지 유형
	 * @param userDetails 사용자 정보
	 * @return 저장된 파일 정보
	 */
	FileResponse saveFile(MultipartFile file, ImageType imageType, UserDetails userDetails);

	/**
	 * 파일을 삭제합니다.
	 *
	 * @param id 파일 ID
	 * @param imageType 이미지 유형
	 * @param principal 사용자 정보
	 * @return 삭제된 파일 정보
	 */
	FileResponse remove(Long id, ImageType imageType, UserDetails principal);

	/**
	 * 여러 파일을 저장합니다.
	 *
	 * @param files 저장할 파일 목록
	 * @param imageType 이미지 유형
	 * @param userDetails 사용자 정보
	 * @return 저장된 파일 정보 목록
	 */
	List<FileResponse> saveFiles(List<? extends MultipartFile> files, ImageType imageType, UserDetails userDetails);
}
