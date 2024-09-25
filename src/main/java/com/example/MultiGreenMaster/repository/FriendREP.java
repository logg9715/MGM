package com.example.MultiGreenMaster.repository;

import com.example.MultiGreenMaster.dto.UserDTO_id_nickname;
import com.example.MultiGreenMaster.entity.FriendENT;
import com.example.MultiGreenMaster.entity.UserENT;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FriendREP extends JpaRepository<FriendENT, Long> {
    boolean existsByUserAndFriend(UserENT user, UserENT friend);
    FriendENT findByUser(UserENT user);
    @Query("SELECT DISTINCT new com.example.MultiGreenMaster.dto.UserDTO_id_nickname(u.id, u.nickname) " +
            "FROM FriendENT f JOIN f.friend u " +
            "WHERE f.user.id = :userId")
    List<UserDTO_id_nickname> findAllFriendByUserId(@Param("userId") Long userId);


}