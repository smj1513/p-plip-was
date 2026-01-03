package com.pplip.domain.trip.attraction.persistence.entity;

import lombok.Getter;

@Getter
public enum SpecialSidos {
    SEOUL(1),
    INCHEON(2),
    DAEJEON(3),
    DAEGU(4),
    GWANGJU(5),
    BUSAN(6),
    ULSAN(7),
    SEJONG(8),
    JEJU(39);

    private int code;

    SpecialSidos(int code) {
        this.code = code;
    }
}
