package com.pplip.domain.trip.attraction.persistence.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.stream.Stream;

@Slf4j
@Getter
public enum ContentType {
	ATTRACTION(12, "관광지"),
	CULTURAL_FACILITIES(14, "문화시설"),
	FESTIVAL_PERFORMANCE_EVENT(15, "행사"),
	TRAVEL_COURSE(25, "관광코스"),
	LEPORTS(28, "레포츠"),
	ACCOMMODATION(32, "숙박"),
	SHOPPING(38, "쇼핑"),
	RESTAURANT(39, "음식점");

	@JsonValue
	private final int id;
	private final String description;

	ContentType(int id, String description) {
		this.id = id;
		this.description = description;
	}

	@JsonCreator
	public static ContentType from(Object value) {
		int targetId;

		// 1. 만약 숫자로 들어왔다면 그대로 사용
		if (value instanceof Integer) {
			targetId = (Integer) value;
		}
		// 2. 만약 문자열("12")로 들어왔다면 숫자로 변환
		else if (value instanceof String stValue) {
			try {
				Optional<ContentType> contentType = getContentType(stValue);
				if (contentType.isPresent()) {
					return contentType.get();
				} else {
					targetId = Integer.parseInt(stValue);
				}
			} catch (NumberFormatException e) {
				return null; // 숫자가 아닌 이상한 문자열이면 무시
			}
		} else {
			return null; // 그 외 타입이면 무시
		}

		// 3. ID로 Enum 찾기
		for (ContentType type : ContentType.values()) {
			if (type.getId() == targetId) {
				return type;
			}
		}
		return null;
	}

	public static ContentType getContentType(int id) {
		for (ContentType type : ContentType.values()) {
			if (type.getId() == id) {
				return type;
			}
		}
		ContentType.log.info("매칭되는 숫자가 없습니다: {}", id);
		throw new IllegalArgumentException("Unknown ContentType ID: " + id);
	}

	public static Optional<ContentType> getContentType(String desc) {
		return Stream.of(ContentType.values()).filter(v -> v.description.equals(desc)).findFirst();
	}

}
