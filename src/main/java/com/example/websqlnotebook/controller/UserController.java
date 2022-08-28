package com.example.websqlnotebook.controller;

import com.example.websqlnotebook.service.SecurityService;
import com.example.websqlnotebook.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Controller
public class UserController {

    @Autowired
    private final SecurityService securityService;
    @Autowired
    private UserService userService;

    public UserController(SecurityService securityService) {
        this.securityService = securityService;
    }

    @GetMapping({"/"})
    public String welcome(Model model) {
        log.info("welcome=========");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            log.info("welcome=========" + auth.getPrincipal());
        }
        return "index";
    }

    @GetMapping("/login")
    public String login(Model model, String error, String logout) {
        log.info("====login report:=");
        if (securityService.isAuthenticated()) {
            log.info("====login user isAuthenticated!");
            return "redirect:/";
        }
        if (error != null) {
            log.info("====Err==:=" + error);
            model.addAttribute("error", "Your username and password is invalid.");
        }
        if (logout != null) {
            log.info("====Info==:=" + logout);
            model.addAttribute("message", "You have been logged out successfully.");
        }
        return "login";
    }


    @RequestMapping(value="/logout", method = RequestMethod.GET)
    public String logoutPage (HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login?logout";
    }


}
