package com.example.MultiGreenMaster.repository;

import com.example.MultiGreenMaster.entity.Friend;
import com.example.MultiGreenMaster.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendRepository extends JpaRepository<Friend, Long> {
    Friend findByUserAndFriend(User user, User friend);
    boolean existsByUserAndFriend(User user, User friend);
}