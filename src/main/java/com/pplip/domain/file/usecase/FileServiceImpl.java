package com.pplip.domain.file.usecase;

import com.pplip.domain.auth.persistence.entity.Account;
import com.pplip.domain.file.api.response.FileResponse;
import com.pplip.domain.file.persistence.dao.BatchSupportFilePropertyDao;
import com.pplip.domain.file.persistence.dao.FilePropertyDao;
import com.pplip.domain.file.persistence.entity.FileProperty;
import com.pplip.domain.file.persistence.entity.ImageType;
import com.pplip.domain.file.utils.FilePropertyProvider;
import com.pplip.domain.file.utils.FileStorage;
import com.pplip.global.api.code.ErrorCode;
import com.pplip.global.exception.BusinessLogicException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class FileServiceImpl implements FileService {
	private final FilePropertyProvider provider;
	private final FileStorage fileStorage;
	private final List<FilePropertyDao<FileProperty>> filePropertyDaos;
	private final List<BatchSupportFilePropertyDao<FileProperty>> batchSupportFilePropertyDaos;

	@Override
	public FileResponse saveFile(MultipartFile file, ImageType imageType, UserDetails userDetails) {
		long userId = ((Account) userDetails).getUserId();
		FileProperty fileProperty = provider.create(file.getOriginalFilename(), file.getContentType(), file.getSize(), userId, imageType);
		fileStorage.store(file, fileProperty.getPath());

		return filePropertyDaos.stream().filter(fdao -> fdao.supports(imageType)).map(fdao -> {
			fdao.insert(fileProperty);
			return FileResponse.builder()
					.id(fileProperty.getId())
					.path(fileProperty.getPath())
					.contentType(fileProperty.getContentType())
					.name(fileProperty.getOriginFileName())
					.imageType(imageType)
					.size(fileProperty.getSize())
					.build();
		}).findFirst().orElseThrow(() -> new BusinessLogicException(ErrorCode.FILE_PROCESS_FAILURE, "파일 저장에 실패하였습니다."));
	}


	@Override
	public FileResponse remove(Long id, ImageType imageType, UserDetails userDetails) {
		long userId= ((Account) userDetails).getUserId();
		return filePropertyDaos.stream().filter(fdao -> fdao.supports(imageType)).map(fdao -> {
			FileProperty fileProperty = fdao.findById(id).orElseThrow(() -> new BusinessLogicException(ErrorCode.FILE_TYPE_NOT_SUPPORT, "파일을 찾을 수 없습니다."));
			if (!fileProperty.getUploaderId().equals(userId)) {
				throw new BusinessLogicException(ErrorCode.INVALIDATED_USER_ERROR, "업로드한 사용자만 삭제할 수 있습니다.");
			}
			int delete = fdao.delete(fileProperty.getId());
			if (delete == 0) {
				throw new BusinessLogicException(ErrorCode.FILE_PROCESS_FAILURE, "삭제된 파일 프로퍼티가 존재하지 않습니다.");
			}
			fileStorage.delete(fileProperty.getPath());
			return FileResponse.builder()
					.id(fileProperty.getId())
					.path(fileProperty.getPath())
					.contentType(fileProperty.getContentType())
					.name(fileProperty.getOriginFileName())
					.size(fileProperty.getSize())
					.imageType(imageType).build();
		}).findFirst().orElseThrow(() -> new BusinessLogicException(ErrorCode.FILE_PROCESS_FAILURE, "파일 삭제에 실패했습니다."));
	}

	@Override
	public List<FileResponse> saveFiles(List<? extends MultipartFile> files, ImageType imageType, UserDetails userDetails) {
		long userId = ((Account) userDetails).getUserId();

		List<FileProperty> fileProperties = new ArrayList<>();
		for (MultipartFile file : files) {
			FileProperty fileProperty = provider.create(file.getOriginalFilename(), file.getContentType(), file.getSize(), userId, imageType);
			fileStorage.store(file, fileProperty.getPath());
			fileProperties.add(fileProperty);
		}
		batchSupportFilePropertyDaos.stream().filter(fdao -> fdao.supports(imageType)).forEach(fdao ->
				fdao.insertAll(fileProperties)
		);
		return fileProperties.stream().map(fileProperty -> FileResponse.builder()
				.id(fileProperty.getId())
				.path(fileProperty.getPath())
				.contentType(fileProperty.getContentType())
				.name(fileProperty.getOriginFileName())
				.imageType(imageType)
				.size(fileProperty.getSize())
				.build()).toList();
	}

	public int bulkUpdateFiles(List<Long> fileIds, ImageType imageType, Long refId) {
		return batchSupportFilePropertyDaos.stream().filter(fdao -> fdao.supports(imageType))
				.mapToInt(fdao ->
						fdao.bulkUpdate(fileIds, refId)
				).sum();
	}
}
