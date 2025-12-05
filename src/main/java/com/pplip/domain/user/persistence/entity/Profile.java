package com.pplip.domain.user.persistence.entity;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Profile {
    private Long id;
    private Long userId;
    private String description;
    private String nickname;
}
