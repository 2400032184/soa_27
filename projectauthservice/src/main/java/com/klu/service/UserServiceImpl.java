package com.klu.service;

import org.springframework.stereotype.Service;

import com.klu.entity.User;
import com.klu.repository.UserRepository;
import com.klu.util.JwtUtil;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final JwtUtil jwtUtil;

    public UserServiceImpl(
            UserRepository userRepository,
            JwtUtil jwtUtil) {

        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public String signup(User user) {

        User existingUser =
                userRepository.findByUsername(user.getUsername());

        if (existingUser != null) {
            return "user already exist";
        }

        if (user.getRole() == null || user.getRole().isBlank()) {
            user.setRole("USER");
        }

        userRepository.save(user);

        return "User registered successfully";
    }

    @Override
    public String login(User user) {

        User existingUser =
                userRepository.findByUsername(user.getUsername());

        if (existingUser == null) {
            return "User not found";
        }

        if (!existingUser.getPassword().equals(user.getPassword())) {
            return "Invalid password";
        }

        return jwtUtil.generateToken(
                existingUser.getId(),
                existingUser.getUsername(),
                existingUser.getRole()
        );
    }
}