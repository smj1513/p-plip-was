package com.pplip.domain.trip.ai.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

public class AiRequest {

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	@Builder
	@ToString
	public static class SuggestAttractions{
		private String query;
		@JsonProperty("content_types")
		private List<String> contentTypes;
		private BigDecimal lat;
		private BigDecimal lng;
		@Builder.Default
		private int k = 5;
		@Builder.Default
		private int m = 1000;
	}
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder

	@ToString
	public static class SuggestPlan{
		@JsonProperty("user_theme")
		private String query;
		@JsonProperty("attraction_id")
		private Long attractionId;
	}

}
