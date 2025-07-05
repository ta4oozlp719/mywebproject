package com.example.demo2.service;

import com.example.demo2.domain.User;
import com.example.demo2.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 회원가입
    public boolean register(User user) {
        // 아이디 중복 체크
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return false;  // 이미 존재
        }
        userRepository.save(user);
        return true;
    }

    // 로그인
    public Optional<User> login(String username, String password) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent() && user.get().getPassword().equals(password)) {
            return user;
        }
        return Optional.empty();
    }
}
