package com.pplip.domain.file.persistence.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ReviewImageProperty extends FileProperty {
    private Long reviewId;


}
