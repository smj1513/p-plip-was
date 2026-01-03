package com.pplip.domain.file.usecase;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.pplip.domain.file.utils.FileStorage;
import com.pplip.global.api.code.ErrorCode;
import com.pplip.global.exception.BusinessLogicException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AwsS3FileStorage implements FileStorage {

	private final AmazonS3 s3;
	@Value("${aws.s3.bucket}")
	private String bucketName;

	@Override
	public void store(MultipartFile file, String path) {
		ObjectMetadata objectMetadata = new ObjectMetadata();
		objectMetadata.setContentLength(file.getSize());
		objectMetadata.setContentType(file.getContentType());
		try {
			s3.putObject(bucketName, path, file.getInputStream(), objectMetadata);

		} catch (IOException e) {
			throw new BusinessLogicException(ErrorCode.FILE_PROCESS_FAILURE, "파일 저장에 실패했습니다.");
		}
	}

	@Override
	public void delete(String path) {
		s3.deleteObject(bucketName, path);
	}
}
