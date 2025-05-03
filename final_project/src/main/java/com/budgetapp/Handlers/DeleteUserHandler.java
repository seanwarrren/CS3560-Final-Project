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

            // Delete current user from memory
            UserManager.deleteUser();

            // Clear all associated transactions from memory
            TransactionManager.clearTransactions();

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

