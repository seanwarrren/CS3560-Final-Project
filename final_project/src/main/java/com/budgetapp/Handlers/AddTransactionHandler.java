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
        if (CorsUtils.handleOptions(exchange)) return;
        CorsUtils.setCorsHeaders(exchange);

        if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            InputStream is = exchange.getRequestBody();
            String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);

            try {
                JSONObject json = new JSONObject(body);
                String name = json.getString("transactionName");
                String category = json.getString("category");
                double amount = json.getDouble("amount");
                String typeStr = json.getString("type").toUpperCase();

                TransactionType type = TransactionType.valueOf(typeStr);
                Transaction transaction = new Transaction(name, category, amount, type);
                TransactionManager.addTransaction(transaction);

                String response = "Transaction added successfully";
                exchange.sendResponseHeaders(200, response.length());
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            } catch (Exception e) {
                String error = "Invalid transaction format: " + e.getMessage();
                exchange.sendResponseHeaders(400, error.length());
                OutputStream os = exchange.getResponseBody();
                os.write(error.getBytes());
                os.close();
            }
        } else {
            exchange.sendResponseHeaders(405, -1); // Method Not Allowed
        }
    }
}
