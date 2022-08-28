package com.example.websqlnotebook.service.impl;

import com.example.websqlnotebook.domain.Role;
import com.example.websqlnotebook.domain.User;
import com.example.websqlnotebook.repository.RoleRepository;
import com.example.websqlnotebook.repository.UserRepository;
import com.example.websqlnotebook.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void save(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        //Role all user ROLE_USER
        Optional<Role> ro = roleRepository.findById(1l);
        user.setRoles(Collections.singleton(ro.get()));
        userRepository.save(user);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

}
