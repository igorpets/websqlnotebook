package com.example.websqlnotebook.controller;

import com.example.websqlnotebook.domain.User;
import com.example.websqlnotebook.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Slf4j
@Controller
public class AdminController {

    @Autowired
    private final UserRepository userRepository;

    public AdminController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/admin")
    public String userList(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "admin/listUser";
    }

    @PostMapping(value = "/delUser", params = "id")
    public String delete(Long id, Model model) {
        log.info("====del user id:=" + id);
        Optional<User> user = userRepository.findById(id);
        log.info("====del user name:=" + user.get().getUsername());
        if(user.isPresent()) {
            userRepository.delete(user.get());
        }
        model.addAttribute("users", userRepository.findAll());
        return "admin/listUser";
    }


}
