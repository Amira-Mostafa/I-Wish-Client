package com.example.client.controllers;

import java.io.File;

import com.example.client.services.DataManagerClient;
import com.example.utils.UIUtils;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.stage.*;

public class AddWishController {
    
    @FXML private TextField nameField;
    @FXML private TextArea descriptionField;
    @FXML private TextField priceField;
    @FXML private ImageView wishImageView;
    @FXML private Button selectImageButton;
    @FXML private Label imagePathLabel;
    @FXML private Label errorLabel;
    @FXML private Button saveButton;
    @FXML private Button cancelButton;
    
    private String selectedImagePath = "";
    
    
    @FXML
    public void initialize() {
        saveButton.setOnAction(e -> handleSave());
        cancelButton.setOnAction(e -> handleCancel());
        
        try {
            wishImageView.setImage(new Image(UIUtils.DEFAULT_WISH_IMAGE, 100, 100, true, true));
        } catch (Exception e) {
            // Ignore
        }
        
        selectImageButton.setOnAction(e -> selectImage());
    }
    
    private void selectImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Wish Image");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp")
        );
        
        File selectedFile = fileChooser.showOpenDialog(selectImageButton.getScene().getWindow());
        if (selectedFile != null) {
            selectedImagePath = selectedFile.toURI().toString();
            try {
                Image image = new Image(selectedImagePath, 100, 100, true, true);
                wishImageView.setImage(image);
                imagePathLabel.setText(selectedFile.getName());
            } catch (Exception e) {
                showError("Failed to load image: " + e.getMessage());
                selectedImagePath = "";
            }
        }
    }
    
    private void handleSave() {
        String name = nameField.getText().trim();
        String description = descriptionField.getText().trim();
        String priceText = priceField.getText().trim();
    
        if (name.isEmpty() || priceText.isEmpty()) {
            showError("Please fill in all required fields (*)");
            return;
        }
    
        try {
            double price = Double.parseDouble(priceText);
            if (price <= 0) {
                showError("Price must be greater than 0");
                return;
            }
    
            String imageUrl = (selectedImagePath != null && !selectedImagePath.isEmpty()) ? selectedImagePath : "";
            boolean success = DataManagerClient.addWish(name, description, price, imageUrl);
    
            if (success) {
                closeWindow();
            } else {
                showError("Failed to add wish. Please try again.");
            }
    
        } catch (NumberFormatException ex) {
            showError("Please enter a valid price");
        }
    }
    
    
    private void handleCancel() {
        closeWindow();
    }
    
    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setStyle("-fx-text-fill: #f44336; -fx-font-size: 12; -fx-font-weight: bold;");
        errorLabel.setVisible(true);
    }
    
    private void closeWindow() {
        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();
    }
}