package com.example.websqlnotebook.controller;

import com.example.websqlnotebook.domain.User;
import com.example.websqlnotebook.service.SecurityService;
import com.example.websqlnotebook.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
public class RegistrationController {

    @Autowired
    private final UserService userService;
    @Autowired
    private final SecurityService securityService;


    public RegistrationController(UserService userService, SecurityService securityService) {
        this.userService = userService;
        this.securityService = securityService;
    }

    @GetMapping("/registration")
    public String registrati_on(Model model) {
        log.info("Register for user");
        if(securityService.isAuthenticated()) {
            //return "redirect:/";
            log.info("Register for user isAuthenticated");
        }
        //
        model.addAttribute("userForm", new User());
        return "registration";
    }

    @PostMapping("/registration")
    public String registrationUserNew(@ModelAttribute("userForm") User userForm, BindingResult bindingResult, Model model) {
        log.info("==save user=========");
        log.info("User:=" + userForm.getUsername());

        if(bindingResult.hasErrors()) {
            return "registration";
        }
        if(!userForm.getPassword().equals(userForm.getPasswordConfirm())) {
            model.addAttribute("passwordError", "Error password confirm");
            return "registration";
        }
        userService.save(userForm);
        securityService.autoLogin(userForm.getUsername(), userForm.getPasswordConfirm());
        return "redirect:/";
    }

}
