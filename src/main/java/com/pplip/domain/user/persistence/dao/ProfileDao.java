package com.pplip.domain.user.persistence.dao;

import com.pplip.domain.file.api.response.FileResponse;
import com.pplip.domain.user.api.response.ProfileResponse;
import com.pplip.domain.user.persistence.entity.Profile;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

/**
 * 프로필 데이터에 접근하는 DAO 인터페이스
 */
@Mapper
public interface ProfileDao {
    int insert(Profile profile);

    Optional<Profile> findById(Long id);

    Optional<Profile> findByUserId(Long userId);

    boolean existsNickname(String nickname);

    int update(Profile profile);

    int delete(Long id);

    ProfileResponse.Info findByIdToDto(Long userId);
}
