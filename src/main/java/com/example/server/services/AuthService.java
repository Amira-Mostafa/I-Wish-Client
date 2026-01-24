package com.example.server.services;

import com.example.models.User;

public class AuthService {

    private DataManager dataManager = DataManager.getInstance();

    public User register(String name, String email, String password, String imagePath) {
        boolean success = dataManager.register(name, email, password, imagePath);
        if (success) {
            return dataManager.getCurrentUser();
        }
        return null;
    }

    public User login(String email, String password) {
        boolean success = dataManager.login(email, password);
        if (success) {
            return dataManager.getCurrentUser();
        }
        return null;
    }
}
