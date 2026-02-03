package com.example.client.controllers;

import java.io.File;

import com.example.client.services.DataManagerClient;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.stage.*;

public class EditWishController {

    @FXML
    private TextField nameField;
    @FXML
    private TextArea descriptionField;
    @FXML
    private TextField priceField;
    @FXML
    private ImageView wishImageView;
    @FXML
    private Button selectImageButton;
    @FXML
    private Label imagePathLabel;
    @FXML
    private Label errorLabel;
    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;

    private String selectedImagePath = "";

    private int wishId;

    public void setWishData(int wishId, String name, String description, double price, String imageUrl) {
        this.wishId = wishId;

        if (nameField != null) {
            nameField.setText(name != null ? name : "");
        }
        if (descriptionField != null) {
            descriptionField.setText(description != null ? description : "");
        }
        if (priceField != null) {
            priceField.setText(String.valueOf(price));
        }
        if (wishImageView != null && imageUrl != null && !imageUrl.isEmpty()) {
            try {
                Image image = new Image(imageUrl, 100, 100, true, true);
                wishImageView.setImage(image);
                selectedImagePath = imageUrl;
                if (imagePathLabel != null) {
                    String fileName = imageUrl;
                    if (imageUrl.contains("/")) {
                        fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
                    }
                    imagePathLabel.setText(fileName);
                }
            } catch (Exception e) {
                try {
                    wishImageView
                            .setImage(new Image(com.example.utils.UIUtils.DEFAULT_WISH_IMAGE, 100, 100, true, true));
                } catch (Exception ex) {
                }
            }
        } else if (wishImageView != null) {
            try {
                wishImageView.setImage(new Image(com.example.utils.UIUtils.DEFAULT_WISH_IMAGE, 100, 100, true, true));
            } catch (Exception e) {
            }
        }
    }

    @FXML
    public void initialize() {
        if (nameField == null || descriptionField == null || priceField == null || wishImageView == null) {
            System.err.println("Warning: Some FXML fields are not initialized!");
            return;
        }

        saveButton.setOnAction(e -> handleSave());
        cancelButton.setOnAction(e -> handleCancel());

        try {
            wishImageView.setImage(new Image(com.example.utils.UIUtils.DEFAULT_WISH_IMAGE, 100, 100, true, true));
        } catch (Exception e) {
        }

        if (selectImageButton != null) {
            selectImageButton.setOnAction(e -> selectImage());
        }
    }

    private void selectImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Wish Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp"));

        File selectedFile = fileChooser.showOpenDialog(selectImageButton.getScene().getWindow());
        if (selectedFile != null) {
            selectedImagePath = selectedFile.toURI().toString();
            try {
                Image image = new Image(selectedImagePath, 100, 100, true, true);
                wishImageView.setImage(image);
                if (imagePathLabel != null) {
                    imagePathLabel.setText(selectedFile.getName());
                }
            } catch (Exception e) {
                showError("Failed to load image: " + e.getMessage());
                selectedImagePath = "";
            }
        }
    }

    private void handleSave() {
        if (nameField == null || descriptionField == null || priceField == null) {
            showError("Form fields are not initialized. Please close and try again.");
            return;
        }

        String name = (nameField.getText() != null) ? nameField.getText().trim() : "";
        String description = (descriptionField.getText() != null) ? descriptionField.getText().trim() : "";
        String priceText = (priceField.getText() != null) ? priceField.getText().trim() : "";

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
            boolean success = DataManagerClient.updateWish(wishId, name, description, price, imageUrl);
            if (success) {
                closeWindow();
            } else {
                showError("Failed to update wish. Please try again.");
            }
        } catch (NumberFormatException ex) {
            showError("Please enter a valid price");
        } catch (Exception ex) {
            ex.printStackTrace();
            showError("An error occurred: " + ex.getMessage());
        }
    }

    private void handleCancel() {
        closeWindow();
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

    private void closeWindow() {
        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();
    }
}
