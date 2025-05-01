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
        if (CorsUtils.handleOptions(exchange)) return;

        if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            CorsUtils.setCorsHeaders(exchange); // apply CORS headers

            InputStream is = exchange.getRequestBody();
            String name = new String(is.readAllBytes(), StandardCharsets.UTF_8).trim();
            UserManager.createUser(name);

            String response = "User created";
            exchange.sendResponseHeaders(200, response.length());
            exchange.getResponseBody().write(response.getBytes());
            exchange.close();
        } else {
            exchange.sendResponseHeaders(405, -1); // Method Not Allowed
            exchange.close();
        }
    }
}
