package com.example.Flower.repository;

import com.example.Flower.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByLoginId(String loginId);
    Optional<User> findByNickname(String nickname);

    @Query("SELECT u FROM User u WHERE u.active = 1")
    List<User> findAllActiveUsers(); // 활성화된 사용자만 조회

    // 비활성화된 사용자 조회
    List<User> findByActive(int active);
}
