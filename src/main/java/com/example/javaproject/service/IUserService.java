package com.example.javaproject.service;

import com.example.javaproject.entity.User;

import java.util.List;

public interface IUserService {
    User findByLoginAndPassword(String login, String password);
    String registerUser(User user);
    User getUserById(Long id);
}
