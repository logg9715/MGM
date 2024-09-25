package com.example.MultiGreenMaster.repository;

import com.example.MultiGreenMaster.entity.FriendENT;
import com.example.MultiGreenMaster.entity.UserENT;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FriendREP extends JpaRepository<FriendENT, Long> {
    boolean existsByUserAndFriend(UserENT user, UserENT friend);
    FriendENT findByUser(UserENT user);
    @Query(value = "SELECT friend_id FROM flower.friend WHERE user_id = :userId", nativeQuery = true)
    List<UserENT> findAllFriendByUserId(@Param("userId") Long userId);
}