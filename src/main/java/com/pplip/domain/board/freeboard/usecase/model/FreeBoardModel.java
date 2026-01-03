package com.pplip.domain.board.freeboard.usecase.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FreeBoardModel {
	private Long id;
	private String title;
	private String content;

}
