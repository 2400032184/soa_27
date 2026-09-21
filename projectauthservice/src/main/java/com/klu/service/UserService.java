package com.klu.service;

import com.klu.entity.User;

public interface UserService {

    String signup(User user);

    String login(User user);
}