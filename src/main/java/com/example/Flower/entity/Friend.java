package com.example.Flower.entity;

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
public class Friend {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties("friends")
    private User user;

    @ManyToOne
    @JoinColumn(name = "friend_id")
    @JsonIgnoreProperties("friends")
    private User friend;

    // Getter for friend
    public User getFriend() {
        return friend;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Friend that = (Friend) o;
        return user.equals(that.user) && friend.equals(that.friend);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, friend);
    }
}