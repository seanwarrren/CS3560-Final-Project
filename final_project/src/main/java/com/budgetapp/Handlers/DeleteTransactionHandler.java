package com.budgetapp.handlers;

import java.io.IOException;
import java.io.OutputStream;

import com.budgetapp.TransactionManager;
import com.budgetapp.UserManager;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class DeleteTransactionHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // Handle CORS and add headers to response
        if (CorsUtils.handleOptions(exchange)) return;
        CorsUtils.setCorsHeaders(exchange);

        // Only accept DELETE requests
        if ("DELETE".equalsIgnoreCase(exchange.getRequestMethod())) {
            // Ensure a user is logged in
            String user = UserManager.getCurrentUser();
            if (user == null) {
                // Unauthorized if no user in session
                exchange.sendResponseHeaders(401, -1);
                return;
            }

            // Extract query string
            String query = exchange.getRequestURI().getQuery();

            // Check if query is present and starts with "id="
            if (query != null && query.startsWith("id=")) {
                try {
                    // Parse ID from query string
                    long id = Long.parseLong(query.split("=")[1]);

                    // Delete transaction with the given ID
                    TransactionManager.deleteTransaction(user, id);

                    // Send success response
                    String response = "Transaction deleted";
                    exchange.sendResponseHeaders(200, response.length());
                    OutputStream os = exchange.getResponseBody();
                    os.write(response.getBytes());
                    os.close();
                    return;
                } catch (NumberFormatException e) {
                    // Invalid ID format
                    exchange.sendResponseHeaders(400, -1); 
                }
            } else {
                // Missing or malformed query string
                exchange.sendResponseHeaders(400, -1); 
            }
        } else {
            // Method Not Allowed
            exchange.sendResponseHeaders(405, -1); 
        }
        // Close response stream
        exchange.close();
    }
}