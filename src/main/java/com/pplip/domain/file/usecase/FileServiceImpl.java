package com.pplip.domain.file.usecase;

import com.pplip.domain.auth.persistence.entity.Account;
import com.pplip.domain.auth.utils.SecurityUtils;
import com.pplip.domain.file.api.response.FileResponse;
import com.pplip.domain.file.persistence.dao.BatchSupportFilePropertyDao;
import com.pplip.domain.file.persistence.dao.FilePropertyDao;
import com.pplip.domain.file.persistence.entity.FileProperty;
import com.pplip.domain.file.persistence.entity.ImageType;
import com.pplip.domain.file.utils.FilePropertyProvider;
import com.pplip.domain.file.utils.FileStorage;
import com.pplip.global.api.code.ErrorCode;
import com.pplip.global.exception.BusinessLogicException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
@Transactional
@Log4j2
public class FileServiceImpl implements FileService {
	private final FilePropertyProvider provider;
	private final FileStorage fileStorage;
	private final Map<ImageType, FilePropertyDao<? extends FileProperty>> filePropertyMap;
	private final Map<ImageType, BatchSupportFilePropertyDao<? extends FileProperty>> batchSupportFilePropertyMap;

	@Autowired
	public FileServiceImpl(FilePropertyProvider provider,
	                       FileStorage fileStorage,
	                       List<FilePropertyDao<? extends FileProperty>> filePropertyDaos,
	                       List<BatchSupportFilePropertyDao<? extends FileProperty>> batchSupportFilePropertyDaos) {
		this.fileStorage = fileStorage;
		this.provider = provider;
		this.filePropertyMap = new HashMap<>();
		for (FilePropertyDao<? extends FileProperty> filePropertyDao : filePropertyDaos) {
			filePropertyMap.put(filePropertyDao.supports(), filePropertyDao);
		}
		this.batchSupportFilePropertyMap = new HashMap<>();
		for (BatchSupportFilePropertyDao<? extends FileProperty> filePropertyDao : batchSupportFilePropertyDaos) {
			batchSupportFilePropertyMap.put(filePropertyDao.supports(), filePropertyDao);
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public FileResponse saveFile(MultipartFile file, ImageType imageType, UserDetails userDetails) {
		log.info("map:{}, {}",batchSupportFilePropertyMap, filePropertyMap);

		long userId = SecurityUtils.resolveUserId(userDetails);
		FileProperty fileProperty = provider.create(file.getOriginalFilename(), file.getContentType(), file.getSize(), userId, imageType);
		fileStorage.store(file, fileProperty.getPath());
		FilePropertyDao<FileProperty> fdao = (FilePropertyDao<FileProperty>) Optional.ofNullable(filePropertyMap.get(imageType)).orElseThrow(() -> new BusinessLogicException(ErrorCode.FILE_TYPE_NOT_SUPPORT));
		fdao.insert(fileProperty);
		return FileResponse.builder()
				.id(fileProperty.getId())
				.path(fileProperty.getPath())
				.contentType(fileProperty.getContentType())
				.name(fileProperty.getOriginFileName())
				.imageType(imageType)
				.size(fileProperty.getSize())
				.build();
	}

	public void deleteOriginFiles(List<String> paths) {
		if (paths.isEmpty()) return;

		try {
			for (String path : paths) {
				fileStorage.delete(path);
			}
		} catch (Exception e) {
			// S3 삭제 실패는 비즈니스 로직을 실패시킬 것인가?
			// 나중에 배치로 정리
			log.error("S3 파일 삭제 중 오류 발생: ", e);
		}
	}

	@Override
	public void deleteSavedFiles(List<Long> ids, ImageType imageType) {
		if (ids.isEmpty()) return;
		BatchSupportFilePropertyDao<? extends FileProperty> fdao = Optional.ofNullable(batchSupportFilePropertyMap.get(imageType)).orElseThrow(() -> new BusinessLogicException(ErrorCode.FILE_TYPE_NOT_SUPPORT));
		fdao.deleteAllById(ids);
	}


	@Override
	public FileResponse remove(Long id, ImageType imageType, UserDetails userDetails) {
		long userId = SecurityUtils.resolveUserId(userDetails);
		FilePropertyDao<? extends FileProperty> fdao = Optional.ofNullable(filePropertyMap.get(imageType)).orElseThrow(() -> new BusinessLogicException(ErrorCode.FILE_NOT_FOUND));
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
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<FileResponse> saveFiles(List<? extends MultipartFile> files, ImageType imageType, UserDetails userDetails) {
		long userId = SecurityUtils.resolveUserId(userDetails);

		List<FileProperty> fileProperties = new ArrayList<>();
		for (MultipartFile file : files) {
			FileProperty fileProperty = provider.create(file.getOriginalFilename(), file.getContentType(), file.getSize(), userId, imageType);
			fileStorage.store(file, fileProperty.getPath());
			fileProperties.add(fileProperty);
		}
		BatchSupportFilePropertyDao<FileProperty> fdao = (BatchSupportFilePropertyDao<FileProperty>) Optional.ofNullable(batchSupportFilePropertyMap.get(imageType)).orElseThrow(() -> new BusinessLogicException(ErrorCode.FILE_TYPE_NOT_SUPPORT));
		fdao.insertAll(fileProperties);
		return fileProperties.stream().map(fileProperty -> FileResponse.builder()
				.id(fileProperty.getId())
				.path(fileProperty.getPath())
				.contentType(fileProperty.getContentType())
				.name(fileProperty.getOriginFileName())
				.imageType(imageType)
				.size(fileProperty.getSize())
				.build()).toList();
	}

	@SuppressWarnings("unchecked")
	public int bulkUpdateFiles(List<Long> fileIds, ImageType imageType, Long refId) {
		BatchSupportFilePropertyDao<FileProperty> fdao = (BatchSupportFilePropertyDao<FileProperty>) Optional.ofNullable(batchSupportFilePropertyMap.get(imageType)).orElseThrow(() -> new BusinessLogicException(ErrorCode.FILE_TYPE_NOT_SUPPORT));
		return fdao.bulkUpdate(fileIds, refId);
	}
}
