package com.example.MultiGreenMaster.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Entity
@Table(name = "Friend")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class FriendENT {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties("friends")
    private UserENT user;

    @ManyToOne
    @JoinColumn(name = "friend_id")
    @JsonIgnoreProperties("friends")
    private UserENT friend;

    // Getter for friend
    public UserENT getFriend() {
        return friend;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FriendENT that = (FriendENT) o;
        return user.equals(that.user) && friend.equals(that.friend);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, friend);
    }
}