package com.example.server.router;

import com.example.models.User;
import com.example.server.services.AuthService;
import com.example.server.socket.ClientSession;

public class RequestRouter {

    private AuthService authService = new AuthService();

    public String route(String request, ClientSession session) {

        try {
            String[] parts = request.split("\\|");
            String command = parts[0];

            switch (command) {

                case "REGISTER": {
                    User user = authService.register(
                            parts[1], parts[2], parts[3], parts.length > 4 ? parts[4] : "");
                
                    if (user != null) {
                        session.authenticate(user);
                        return "SUCCESS|" + user.getUserId() + "|" +
                               user.getName() + "|" + user.getEmail() + "|" +
                               (user.getImagePath() != null ? user.getImagePath() : "");
                    }
                    return "ERROR|Registration failed";
                }
                

                case "LOGIN": {
                    User user = authService.login(parts[1], parts[2]);

                    if (user != null) {
                        session.authenticate(user);
                        return "SUCCESS|" + user.getUserId() + "|" +
                                user.getName() + "|" + user.getEmail();
                    }
                    return "ERROR|Invalid credentials";
                }

                default:
                    return "ERROR|Unknown command";
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR|Bad request";
        }
    }
}