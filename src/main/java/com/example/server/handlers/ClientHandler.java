package com.example.server.handlers;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import com.example.server.router.RequestRouter;
import com.example.server.socket.ClientSession;

public class ClientHandler implements Runnable {

    private ClientSession session;
    private RequestRouter router = new RequestRouter();

    public ClientHandler(ClientSession session) {
        this.session = session;
    }

    @Override
    public void run() {

        try (
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(session.getSocket().getInputStream()));
            PrintWriter out = new PrintWriter(
                    session.getSocket().getOutputStream(), true)
        ) {

            String request;
            while ((request = in.readLine()) != null) {

                System.out.println("Received: " + request);

                String response = router.route(request, session);

                out.println(response);
            }

        } catch (Exception e) {
            System.out.println("Client disconnected");
        }
    }
}