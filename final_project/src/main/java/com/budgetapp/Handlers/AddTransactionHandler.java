package com.budgetapp.handlers;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import org.json.JSONObject;

import com.budgetapp.Transaction;
import com.budgetapp.TransactionManager;
import com.budgetapp.TransactionType;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class AddTransactionHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {

        // Handle CORS and set necessary headers
        if (CorsUtils.handleOptions(exchange)) return;
        CorsUtils.setCorsHeaders(exchange);

        // Only accept POST requests
        if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            InputStream is = exchange.getRequestBody();
            String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);

            try {
                // Parse request body into a JSON object
                JSONObject json = new JSONObject(body);
                String name = json.getString("transactionName");
                String category = json.getString("category");
                double amount = json.getDouble("amount");
                String typeStr = json.getString("type").toUpperCase(); // Ensure enum matching

                // Convert string to enum and construct transaction
                TransactionType type = TransactionType.valueOf(typeStr);
                Transaction transaction = new Transaction(name, category, amount, type);

                // Add transaction to manager
                TransactionManager.addTransaction(transaction);

                // Send success response
                String response = "Transaction added successfully";
                exchange.sendResponseHeaders(200, response.length());
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            } catch (Exception e) {
                // Send error if JSON parsing or field extraction fails
                String error = "Invalid transaction format: " + e.getMessage();
                exchange.sendResponseHeaders(400, error.length());
                OutputStream os = exchange.getResponseBody();
                os.write(error.getBytes());
                os.close();
            }
        } else {
            // Method not allowed for other request types
            exchange.sendResponseHeaders(405, -1); 
        }
    }
}
