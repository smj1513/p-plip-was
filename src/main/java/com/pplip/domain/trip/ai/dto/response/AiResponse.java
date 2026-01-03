package com.pplip.domain.trip.ai.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.pplip.domain.trip.attraction.persistence.entity.ContentType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AiResponse {

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	@ToString
	public static class SuggestAttraction {
		private Long no;
		private String title;
		private BigDecimal latitude;
		private BigDecimal longitude;

		@JsonProperty("content_type")
		private ContentType contentType;
		private String address;

		@JsonProperty("big_image")
		private String bigImage;
		private String thumbnail;
		private String homepage;

		@Builder.Default
		private List<String> tags = new ArrayList<>();
	}

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	@ToString
	public static class SuggestPlan {
		@JsonProperty("plan_title")
		private String title;
		@JsonProperty("start_date")
		private LocalDate startDate;
		@JsonProperty("end_date")
		private LocalDate endDate;
		@JsonProperty("to_dos")
		private List<ToDoItem> toDos;


	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class ToDoItem {
		private String name;
		@JsonProperty("detail_plan_desc")
		private String detailPlanDesc;
		@JsonProperty("start_at")
		private LocalDateTime startAt;
		@JsonProperty("end_at")
		private LocalDateTime endAt;
		private PlanAttraction attraction;
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class PlanAttraction {
		@JsonProperty("no")
		private Long id;

		private String title;
		private BigDecimal latitude;
		private BigDecimal longitude;
		private String addr1;
		private String overview;
		@JsonProperty("first_image1")
		private String firstImage1;

		@JsonProperty("first_image2")
		private String firstImage2;
		@JsonProperty("content_type")
		private String contentType;
	}
}
