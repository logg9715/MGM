package com.example.Flower.repository;

import com.example.Flower.entity.Friend;
import com.example.Flower.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendRepository extends JpaRepository<Friend, Long> {
    Friend findByUserAndFriend(User user, User friend);
    boolean existsByUserAndFriend(User user, User friend);
}