package com.example.interfolio.service;

import com.example.interfolio.entity.User;
import com.example.interfolio.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User createUser(User user) {
        return userRepository.save(user); // 자동으로 예외 처리
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("유저가 존재하지 않습니다."));
    }
}
