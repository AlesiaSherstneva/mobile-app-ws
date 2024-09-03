package com.develop.app.ws.repository;

import com.develop.app.ws.io.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findUserEntityByEmail(String email);

    UserEntity findByUserId(String userId);
}