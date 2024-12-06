package com.example.javaproject.service;

import com.example.javaproject.entity.User;

public interface IUserService {
    User findByLoginAndPassword(String login, String password);
    String registerUser(User user);
}
