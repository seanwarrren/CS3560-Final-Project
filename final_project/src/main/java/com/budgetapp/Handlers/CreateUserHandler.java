package com.budgetapp.handlers;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import com.budgetapp.UserManager;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class CreateUserHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // Handle CORS requests
        if (CorsUtils.handleOptions(exchange)) return;

        // Apply CORS headers
        CorsUtils.setCorsHeaders(exchange); 

        // Only allow POST requests to create a user
        if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {

            // Read request body and extract the username
            InputStream is = exchange.getRequestBody();
            String name = new String(is.readAllBytes(), StandardCharsets.UTF_8).trim();

            // Set the current user in memory
            UserManager.createUser(name);

            // Send back a success response
            String response = "User created";
            exchange.sendResponseHeaders(200, response.length());
            exchange.getResponseBody().write(response.getBytes());
            exchange.close();
        } else {
            // Return 405 if method is not POST
            exchange.sendResponseHeaders(405, -1); 
            exchange.close();
        }
    }
}
