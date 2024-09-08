package com.example.MultiGreenMaster.repository;

import com.example.MultiGreenMaster.entity.UserENT;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserREP extends JpaRepository<UserENT, Long> {

    Optional<UserENT> findByLoginId(String loginId);
    Optional<UserENT> findByNickname(String nickname);

    List<UserENT> findByDisableFalse();

    // 비활성화된 사용자 조회
}
