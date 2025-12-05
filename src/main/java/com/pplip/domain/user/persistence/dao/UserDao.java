package com.pplip.domain.user.persistence.dao;

import com.pplip.domain.user.persistence.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface UserDao {
    Optional<User> findById(Long id);

    int update(User user);

    int insert(User user);

    int delete(Long id);
}
