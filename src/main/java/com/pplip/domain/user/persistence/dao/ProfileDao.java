package com.pplip.domain.user.persistence.dao;

import com.pplip.domain.user.persistence.entity.Profile;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface ProfileDao {
    int insert(Profile profile);

    Optional<Profile> findById(Long id);

    Optional<Profile> findByUserId(Long userId);

    boolean existsNickname(String nickname);

    int update(Profile profile);

    int delete(Long id);
}
