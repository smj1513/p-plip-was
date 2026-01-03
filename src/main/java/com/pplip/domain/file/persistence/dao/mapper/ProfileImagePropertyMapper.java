package com.pplip.domain.file.persistence.dao.mapper;

import com.pplip.domain.file.persistence.entity.ProfileImageProperty;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

/**
 * 프로필 이미지 속성 관련 데이터베이스 작업을 위한 매퍼 인터페이스
 */
@Mapper
public interface ProfileImagePropertyMapper{
    /**
     * 새로운 프로필 이미지 속성을 삽입합니다.
     *
     * @param property 삽입할 이미지 속성 객체
     * @return 삽입된 행의 수
     */
    int insert(ProfileImageProperty property);

    /**
     * 지정된 ID를 가진 프로필 이미지 속성을 찾습니다.
     *
     * @param id 찾을 이미지 속성의 ID
     * @return 이미지 속성(존재하는 경우 Optional로 감싸짐)
     */
    Optional<ProfileImageProperty> findById(Long id);

    /**
     * 지정된 ID를 가진 프로필 이미지 속성을 삭제합니다.
     *
     * @param id 삭제할 이미지 속성의 ID
     * @return 삭제된 행의 수
     */
    int delete(Long id);

	Optional<ProfileImageProperty> findByProfileId(@Param("profileId") Long profileId);
}
