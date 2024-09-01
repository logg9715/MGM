package com.example.Flower.service;

import com.example.Flower.entity.Users;

public interface UsersService {
    Users findByUserId(String userId);
    boolean validateUser(String userId, String password);
}
