package com.pplip.domain.trip.plan.api.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class ToDoResponse {

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class ToDoSummary {
		private Long id;
		private Long planId;
		private Long attractionId;
		private String description;
		private String title;
		private String attractionImage;

		private BigDecimal latitude;
		private BigDecimal longitude;

		private LocalDateTime willStartAt;
		private LocalDateTime willEndAt;
		private LocalDateTime createdAt;
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class ToDoDetail {
		private Long id;
		private Long planId;
		private Long attractionId;

		private String title;
		private String attractionImage;
		private String description;

		private BigDecimal latitude;
		private BigDecimal longitude;

		private LocalDateTime willStartAt;
		private LocalDateTime willEndAt;
		private LocalDateTime createdAt;
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class ToDoUpdated {
		private List<ToDoSummary> toDoItems;
	}
}
