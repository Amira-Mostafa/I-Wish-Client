package com.example.client.controllers;

import com.example.client.App;
import com.example.client.services.DataManagerClient;
import com.example.utils.UIUtils;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class LoginController {

    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label errorLabel;
    @FXML
    private Button signInButton;
    @FXML
    private Hyperlink createAccountLink;

    @FXML
    public void initialize() {
        UIUtils.styleHyperlink(createAccountLink);

        signInButton.setOnAction(e -> handleLogin());
        createAccountLink.setOnAction(e -> showRegisterScreen());
    }

    private void handleLogin() {
        System.out.println("=== LoginController.handleLogin() ===");

        String email = emailField.getText().trim();
        String password = passwordField.getText();

        System.out.println("Email: " + email);
        System.out.println("Password length: " + password.length());

        errorLabel.setVisible(false);

        if (email.isEmpty() || password.isEmpty()) {
            showError("Please fill in all fields");
            return;
        }

        System.out.println("Calling dataManager.login()...");
        if (DataManagerClient.login(email, password)) {
            System.out.println("Login successful! Loading dashboard...");
            try {
                App.loadScene("/com/example/views/dashboard.fxml");
            } catch (Exception ex) {
                ex.printStackTrace();
                showError("Error loading dashboard: " + ex.getMessage());
            }
        } else {
            System.out.println("Login failed in DataManager");
            showError("Invalid email or password");
        }
    }

    private void showRegisterScreen() {
        try {
            App.loadScene("/com/example/views/register.fxml");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
        System.err.println("Login error: " + message);
    }
}
