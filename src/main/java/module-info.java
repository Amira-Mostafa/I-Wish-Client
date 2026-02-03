module com.example.client {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires org.json;

    exports com.example.client;

    opens com.example.client.controllers to javafx.fxml;

    exports com.example.client.services;
    exports com.example.client.network;
    exports com.example.client.utils;

    exports com.example.models;
}
