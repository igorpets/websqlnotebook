package com.example.websqlnotebook.service;

import com.example.websqlnotebook.domain.User;

public interface UserService {
    void save(User user);
    User findByUsername(String username);
}
