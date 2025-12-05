package com.pplip.domain.file.persistence.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ProfileImageProperty extends FileProperty {

    private Long profileId;

}
