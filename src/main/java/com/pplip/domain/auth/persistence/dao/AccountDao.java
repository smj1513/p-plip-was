package com.pplip.domain.auth.persistence.dao;


import com.pplip.domain.auth.persistence.entity.Account;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

/**
 * Account 데이터에 접근하기 위한 MyBatis 매퍼 인터페이스
 */
@Mapper
public interface AccountDao {

    /**
     * 이메일 주소를 기준으로 계정 정보를 조회합니다.
     *
     * @param username 조회할 사용자의 이메일 주소
     * @return Optional<Account> 객체. 계정이 존재하지 않는 경우 Optional.empty()
     */
    Optional<Account> findByEmail(String username);

    int insert(Account account);

    int updatePassword(Account account);

    int count();
}
