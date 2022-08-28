package com.example.websqlnotebook.service;

public interface SecurityService {
    boolean isAuthenticated();
    void autoLogin(String username, String password);
}
