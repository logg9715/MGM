package com.example.MultiGreenMaster.repository;

import com.example.MultiGreenMaster.entity.FriendENT;
import com.example.MultiGreenMaster.entity.UserENT;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendREP extends JpaRepository<FriendENT, Long> {
    FriendENT findByUserAndFriend(UserENT user, UserENT friend);
    boolean existsByUserAndFriend(UserENT user, UserENT friend);

    FriendENT findByUser(UserENT user);
}