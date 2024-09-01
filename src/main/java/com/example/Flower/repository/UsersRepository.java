package com.example.Flower.repository;

import com.example.Flower.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<Users, Long> {
    Users findByUserId(String userId);
}
