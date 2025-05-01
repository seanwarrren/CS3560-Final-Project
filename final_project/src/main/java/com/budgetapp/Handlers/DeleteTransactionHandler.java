package com.budgetapp.handlers;

import java.io.IOException;
import java.io.OutputStream;

import com.budgetapp.TransactionManager;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class DeleteTransactionHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (CorsUtils.handleOptions(exchange)) return;
        CorsUtils.setCorsHeaders(exchange);

        if ("DELETE".equalsIgnoreCase(exchange.getRequestMethod())) {
            String query = exchange.getRequestURI().getQuery(); // id=123
            if (query != null && query.startsWith("id=")) {
                try {
                    long id = Long.parseLong(query.split("=")[1]);
                    TransactionManager.deleteTransaction(id);
                    String response = "Transaction deleted";
                    exchange.sendResponseHeaders(200, response.length());
                    OutputStream os = exchange.getResponseBody();
                    os.write(response.getBytes());
                    os.close();
                    return;
                } catch (NumberFormatException e) {
                    exchange.sendResponseHeaders(400, -1); // Bad Request
                }
            } else {
                exchange.sendResponseHeaders(400, -1); // Bad Request if no id
            }
        } else {
            exchange.sendResponseHeaders(405, -1); // Method Not Allowed
        }
        exchange.close();
    }
}