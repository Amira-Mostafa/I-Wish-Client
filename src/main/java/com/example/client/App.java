package com.example.client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

public class App extends Application {

    private static Stage stage;
    private static final double WIDTH = 1200;
    private static final double HEIGHT = 700;
    private static boolean lastStatus = false;

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;

        // Show Not Available first
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/views/notAvailableServer.fxml"));
        Scene scene = new Scene(root, WIDTH, HEIGHT);
        stage.setScene(scene);

        stage.setWidth(WIDTH);
        stage.setHeight(HEIGHT);
        stage.setMinWidth(WIDTH);
        stage.setMinHeight(HEIGHT);
        stage.setResizable(true);

        stage.show();
        stage.centerOnScreen();

        startServerChecker(); // AUTO SWITCH
    }

    // Change screen
    public static void loadScene(String fxml) {
        try {
            Parent root = FXMLLoader.load(App.class.getResource(fxml));
            stage.getScene().setRoot(root);

            if (root instanceof Region) {
                Region r = (Region) root;
                r.setPrefSize(WIDTH, HEIGHT);
                r.setMinSize(WIDTH, HEIGHT);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Check server every 2 seconds
    private void startServerChecker() {
        Timer timer = new Timer(true);

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                boolean serverUp = isServerUp();

                if (serverUp != lastStatus) {
                    lastStatus = serverUp;

                    Platform.runLater(() -> {
                        if (serverUp) {
                            loadScene("/com/example/views/login.fxml");
                        } else {
                            loadScene("/com/example/views/notAvailableServer.fxml");
                        }
                    });
                }
            }
        }, 0, 2000);
    }

    private boolean isServerUp() {
        try (Socket socket = new Socket("localhost", 8888)) {
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
