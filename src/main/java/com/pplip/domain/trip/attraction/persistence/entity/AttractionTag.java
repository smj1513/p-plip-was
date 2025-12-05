package com.pplip.domain.trip.attraction.persistence.entity;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AttractionTag {
    private Long id;
    private Long tagId;
    private Long attractionId;
}
