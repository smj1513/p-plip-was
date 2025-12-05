package com.pplip.domain.file.persistence.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class NoticeBoardImageProperty extends FileProperty {
    private Long boardId;


}
