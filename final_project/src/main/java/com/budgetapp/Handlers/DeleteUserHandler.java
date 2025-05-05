package com.budgetapp.handlers;

import java.io.IOException;

import com.budgetapp.TransactionManager;
import com.budgetapp.UserManager;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class DeleteUserHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // Handle CORS requests
        if (CorsUtils.handleOptions(exchange)) return;

        // Add CORS headers to response
        CorsUtils.setCorsHeaders(exchange);

        // Check if request method is DELETE
        if ("DELETE".equals(exchange.getRequestMethod())) {

            // Fetch the username of the currently logged-in user
            String user = UserManager.getCurrentUser();
            if (user == null) {
                // No user logged in → unauthorized
                exchange.sendResponseHeaders(401, -1);
                return;
            }

            // Delete current user from memory
            UserManager.deleteUser();

            // Clear all associated transactions from memory
            TransactionManager.clearTransactions(user);

            // Send confirmation message 
            String response = "User deleted";
            System.out.println(response); // log deletion
            
            // Return HTTP 200 OK with response message
            exchange.sendResponseHeaders(200, response.length());
            exchange.getResponseBody().write(response.getBytes());
            exchange.close();
        } else {
            // If method is not DELETE, respond with 405 Method Not Allowed
            exchange.sendResponseHeaders(405, -1); 
            exchange.close();
        }
    }
 }

