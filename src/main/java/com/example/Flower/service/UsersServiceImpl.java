package com.example.Flower.service;

import com.example.Flower.entity.Users;
import com.example.Flower.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsersServiceImpl implements UsersService {

    @Autowired
    private UsersRepository usersRepository;

    @Override
    public Users findByUserId(String userId) {
        return usersRepository.findByUserId(userId);
    }

    @Override
    public boolean validateUser(String userId, String password) {
        Users user = usersRepository.findByUserId(userId);
        return user != null && user.getPassword().equals(password);
    }
}
