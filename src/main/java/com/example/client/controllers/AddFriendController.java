package com.example.client.controllers;

import java.util.List;

import com.example.models.User;
import com.example.client.services.DataManagerClient;
import com.example.utils.UIUtils;
import com.example.utils.DialogUtils;

import javafx.fxml.FXML;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.scene.text.*;
import javafx.stage.Stage;

public class AddFriendController {
    
    @FXML private TextField searchField;
    @FXML private VBox resultsContainer;
    @FXML private Button searchButton;
    @FXML private Button cancelButton;
    
    // Default avatar image loaded once
    private Image defaultAvatar;
        
    @FXML
    public void initialize() {
        // Pre-load default avatar
        loadDefaultAvatar();
        
        searchButton.setOnAction(e -> handleSearch());
        cancelButton.setOnAction(e -> handleCancel());
        
        // Allow Enter key to trigger search
        searchField.setOnAction(e -> handleSearch()); 
        loadAllUsers();
    }
    
    private void loadDefaultAvatar() {
        try {
            // Load the default avatar from resources
            defaultAvatar = new Image(getClass().getResourceAsStream(UIUtils.DEFAULT_USER_AVATAR));
            System.out.println("Default avatar loaded successfully from: " + UIUtils.DEFAULT_USER_AVATAR);
        } catch (Exception e) {
            System.err.println("ERROR: Could not load default avatar from " + UIUtils.DEFAULT_USER_AVATAR);
            e.printStackTrace();
            // Create a programmatic fallback avatar
            defaultAvatar = createPlaceholderAvatar("Default");
        }
    }
    
    private void loadAllUsers() {
        System.out.println("Loading all users...");
        List<User> allUsers = DataManagerClient.getAllUsers();
        System.out.println("Found " + allUsers.size() + " users");
        
        resultsContainer.getChildren().clear();
        
        if (allUsers.isEmpty()) {
            Label noUsers = new Label("No users found.\n\nThere are no other users in the system yet.");
            noUsers.setStyle("-fx-text-fill: #666; -fx-font-size: 14; -fx-padding: 20; -fx-alignment: center;");
            noUsers.setWrapText(true);
            resultsContainer.getChildren().add(noUsers);
        } else {
            for (User user : allUsers) {
                resultsContainer.getChildren().add(createUserItem(user));
            }
        }
    }
    
    private void handleSearch() {
        String query = searchField.getText().trim();
        
        System.out.println("Searching for: " + query);
        
        // If query is empty, show all users
        List<User> searchResults;
        if (query.isEmpty()) {
            searchResults = DataManagerClient.getAllUsers();
        } else {
            searchResults = DataManagerClient.searchUsers(query);
        }
        
        System.out.println("Search returned " + searchResults.size() + " results");
        
        resultsContainer.getChildren().clear();
        
        if (searchResults.isEmpty()) {
            Label noResults = new Label("No users found matching: \"" + query + "\"\n\nTry searching by name or email.");
            noResults.setStyle("-fx-text-fill: #666; -fx-font-size: 14; -fx-padding: 20; -fx-alignment: center;");
            noResults.setWrapText(true);
            resultsContainer.getChildren().add(noResults);
        } else {
            for (User user : searchResults) {
                resultsContainer.getChildren().add(createUserItem(user));
            }
        }
    }
    
    private HBox createUserItem(User user) {
        HBox item = new HBox(15);
        item.setAlignment(Pos.CENTER_LEFT);
        item.setPadding(new Insets(15));
        item.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 10; -fx-border-color: #e0e0e0; -fx-border-radius: 10; -fx-border-width: 1;");
        
        // Avatar
        ImageView avatar = new ImageView();
        avatar.setFitWidth(50);
        avatar.setFitHeight(50);
        avatar.setPreserveRatio(true);
        
        try {
            String imagePath = user.getImagePath();
            
            if (imagePath != null && !imagePath.trim().isEmpty()) {
                // Clean the path
                imagePath = imagePath.trim();
                
                // Try to load as a resource first (if it's a resource path)
                if (imagePath.startsWith("/")) {
                    // It looks like a resource path
                    try {
                        Image resourceImage = new Image(getClass().getResourceAsStream(imagePath));
                        avatar.setImage(resourceImage);
                        if (avatar.getImage() == null || avatar.getImage().isError()) {
                            throw new Exception("Resource image failed to load");
                        }
                        System.out.println("Loaded user image from resource: " + imagePath);
                    } catch (Exception e) {
                        // Try as a file path instead
                        try {
                            Image fileImage = new Image("file:" + imagePath);
                            avatar.setImage(fileImage);
                            System.out.println("Loaded user image from file: " + imagePath);
                        } catch (Exception ex) {
                            throw new Exception("Neither resource nor file path worked");
                        }
                    }
                } else if (imagePath.startsWith("file:")) {
                    // Already has file: prefix
                    Image fileImage = new Image(imagePath);
                    avatar.setImage(fileImage);
                    System.out.println("Loaded user image from file URL: " + imagePath);
                } else if (imagePath.startsWith("http")) {
                    // It's a web URL
                    Image webImage = new Image(imagePath);
                    avatar.setImage(webImage);
                    System.out.println("Loaded user image from web URL: " + imagePath);
                } else {
                    // Assume it's a local file path without prefix
                    try {
                        Image fileImage = new Image("file:" + imagePath);
                        avatar.setImage(fileImage);
                        System.out.println("Loaded user image from local file: " + imagePath);
                    } catch (Exception e) {
                        throw new Exception("Invalid image path format");
                    }
                }
                
                // Verify the image loaded
                if (avatar.getImage() == null || avatar.getImage().isError()) {
                    throw new Exception("Image failed to load");
                }
            } else {
                // No image path provided, use default
                throw new Exception("No image path provided for user: " + user.getName());
            }
        } catch (Exception e) {
            System.err.println("ERROR loading image for user " + user.getName() + ": " + e.getMessage());
            // Use the pre-loaded default avatar
            avatar.setImage(defaultAvatar);
        }
        
        // Make avatar circular
        Circle clip = new Circle(25, 25, 25);
        avatar.setClip(clip);
        
        // User info
        VBox info = new VBox(5);
        Label nameLabel = new Label(user.getName());
        nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        Label emailLabel = new Label(user.getEmail());
        emailLabel.setStyle("-fx-text-fill: #666; -fx-font-size: 12;");
        info.getChildren().addAll(nameLabel, emailLabel);
        
        // Add Friend button
        Button addFriendBtn = UIUtils.createGradientButton("Add Friend", 120, 40);
        addFriendBtn.setOnAction(e -> {
            System.out.println("Add Friend button clicked for user: " + user.getName() + " (ID: " + user.getUserId() + ")");
            boolean success = DataManagerClient.sendFriendRequest(user.getUserId());
            if (success) {
                DialogUtils.showSuccess("Friend Request Sent", 
                    "Your friend request has been sent to " + user.getName() + "!\n\n" +
                    "They will be notified and can accept your request.");
                // Remove this user from the results
                resultsContainer.getChildren().remove(item);
            } else {
                DialogUtils.showError("Request Failed", 
                    "Unable to send friend request.\n\n" +
                    "You may have already sent a request to this user, or they may have already sent one to you.\n\n" +
                    "Please check your pending friend requests.");
            }
        });
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        item.getChildren().addAll(avatar, info, spacer, addFriendBtn);
        return item;
    }
    
    private Image createPlaceholderAvatar(String name) {
        // Create a simple colored circle as placeholder
        int size = 50;
        WritableImage img = new WritableImage(size, size);
        PixelWriter pw = img.getPixelWriter();
        
        // Generate a color based on the name hash
        int color = name.hashCode() & 0xFFFFFF;
        
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                // Create a circle shape
                double dx = x - size/2.0;
                double dy = y - size/2.0;
                double distance = Math.sqrt(dx*dx + dy*dy);
                
                if (distance <= size/2.0) {
                    pw.setColor(x, y, javafx.scene.paint.Color.rgb(
                        (color >> 16) & 0xFF,
                        (color >> 8) & 0xFF,
                        color & 0xFF,
                        0.8  // Add some transparency
                    ));
                } else {
                    pw.setColor(x, y, javafx.scene.paint.Color.TRANSPARENT);
                }
            }
        }
        return img;
    }
    
    private void handleCancel() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
}