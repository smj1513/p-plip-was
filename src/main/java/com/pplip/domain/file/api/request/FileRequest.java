package com.pplip.domain.file.api.request;

import com.pplip.domain.file.persistence.entity.ModifyStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileRequest {
	private Long id;
	private ModifyStatus status;
}
