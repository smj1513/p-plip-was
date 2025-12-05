package com.pplip.domain.trip.attraction.persistence.entity;

import lombok.Getter;

@Getter
public enum ContentType {
    ATTRACTION(12),
    CULTURAL_FACILITIES(14),
    FESTIVAL_PERFORMANCE_EVENT(15),
    TRAVEL_COURSE(25),
    LEPORTS(28),
    ACCOMMODATION(32),
    SHOPPING(38),
    RESTAURANT(39);

    private final int id;

    ContentType(int id) {
        this.id = id;
    }
}
