package com.example.client.controllers;

import java.io.File;
import java.io.IOException;

import com.example.client.services.*;
import com.example.utils.*;

import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.shape.Circle;
import javafx.stage.*;

public class RegisterController {
    
    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmField;
    @FXML private Label errorLabel;
    @FXML private Button createAccountButton;
    @FXML private Hyperlink signInLink;
    @FXML private ImageView profileImageView;
    @FXML private Button selectImageButton;
    @FXML private Label imagePathLabel;
    
    private String selectedImagePath = "";
    
    
    @FXML
    public void initialize() {
        UIUtils.styleHyperlink(signInLink);
        createAccountButton.setOnAction(e -> handleRegister());
        signInLink.setOnAction(e -> showLoginScreen());
        
        try {
            profileImageView.setImage(new Image(UIUtils.DEFAULT_USER_AVATAR, 80, 80, true, true));
            Circle clip = new Circle(40, 40, 40);
            profileImageView.setClip(clip);
        } catch (Exception e) {
        }
        
        selectImageButton.setOnAction(e -> selectImage());
    }
    
    private void selectImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Profile Picture");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp")
        );
        
        File selectedFile = fileChooser.showOpenDialog(selectImageButton.getScene().getWindow());
        if (selectedFile != null) {
            selectedImagePath = selectedFile.toURI().toString();
            try {
                Image image = new Image(selectedImagePath, 80, 80, true, true);
                profileImageView.setImage(image);
                imagePathLabel.setText(selectedFile.getName());
            } catch (Exception e) {
                showError("Failed to load image: " + e.getMessage());
                selectedImagePath = "";
            }
        }
    }
    
    private void handleRegister() {
        System.out.println("=== RegisterController.handleRegister() ===");
        
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = confirmField.getText();
        
        System.out.println("Name: " + name);
        System.out.println("Email: " + email);
        System.out.println("Password length: " + password.length());
        
        errorLabel.setVisible(false);
        
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showError("Please fill in all fields");
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            showError("Passwords do not match");
            return;
        }
        
        if (password.length() < 6) {
            showError("Password must be at least 6 characters");
            return;
        }
        
        String emailRegex = "^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$";
        if (!email.matches(emailRegex)) {
            showError("Please enter a valid email address");
            return;
        }
        
        System.out.println("Calling dataManager.register()...");
        boolean success = DataManagerClient.register(name, email, password, selectedImagePath);
        
        if (success) {
            System.out.println("Registration successful! Loading dashboard...");
            DialogUtils.showSuccess("Registration Successful", 
                "Welcome to i-Wish, " + name + "!\n\nYour account has been created successfully.");
            
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/views/dashboard.fxml"));
                Parent root = loader.load();

                DashboardController dashboardController = loader.getController();
                System.out.println("Dashboard controller obtained: " + (dashboardController != null ? "YES" : "NO"));
        
                if (dashboardController != null) {
                    dashboardController.setCurrentUser(UserSession.getUser());
                    System.out.println("User passed to dashboard controller");
                } else {
                    System.err.println("ERROR: Could not get dashboard controller!");
                }

                Stage stage = (Stage) createAccountButton.getScene().getWindow();
                Scene scene = new Scene(root, 1200, 700);
                scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
                stage.setScene(scene);
                stage.setMinWidth(1200);
                stage.setMinHeight(700);
                stage.setResizable(true);
                
            } catch (IOException ex) {
                ex.printStackTrace();
                DialogUtils.showError("Error", "Error loading dashboard: " + ex.getMessage());
            }
        } else {
            DialogUtils.showError("Registration Failed", 
                "Unable to create account.\n\n" +
                "This email address may already be registered.\n" +
                "Please try a different email or sign in if you already have an account.");
        }
    }
    
    private void showLoginScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/views/login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) signInLink.getScene().getWindow();
            Scene scene = new Scene(root, 1200, 700);
            scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
            stage.setScene(scene);
            stage.setMinWidth(1200);
            stage.setMinHeight(700);
            stage.setResizable(true);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setStyle("-fx-text-fill: #f44336; -fx-font-size: 12; -fx-font-weight: bold;");
        errorLabel.setVisible(true);
        System.err.println("Registration error: " + message);
    }
}
