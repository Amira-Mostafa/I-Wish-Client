module com.example.client {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.json;

    exports com.example.client;

    // FXML controllers
    opens com.example.client.controllers to javafx.fxml;

    // If you reference these packages from other modules (usually not required unless shared):
    exports com.example.client.services;
    exports com.example.client.network;
    exports com.example.client.utils;

    // If models are inside Client project:
    exports com.example.models;
}
