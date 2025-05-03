package com.budgetapp.handlers;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.budgetapp.Transaction;
import com.budgetapp.TransactionManager;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class GetTransactionsHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {

        // Set CORS headers
        if (CorsUtils.handleOptions(exchange)) return;
        CorsUtils.setCorsHeaders(exchange);
        
        // Only allow GET requests
        if ("GET".equalsIgnoreCase(exchange.getRequestMethod())) {

            // Retrieve all transactions from the TransactionManager
            List<Transaction> transactions = TransactionManager.getAllTransactions();
            JSONArray jsonArray = new JSONArray();

            // Convert each transaction into a JSON object and add to array
            for (Transaction t : transactions) {
                JSONObject obj = new JSONObject();
                obj.put("id", t.getId());
                obj.put("name", t.getName());
                obj.put("category", t.getCategory());
                obj.put("amount", t.getAmount());
                obj.put("type", t.getType().toString());
                jsonArray.put(obj);
            }

            // Send the transaction list as the response
            String response = jsonArray.toString();
            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        } else {
            // Respond with 405 if method is not GET
            exchange.sendResponseHeaders(405, -1); 
        }
    }
}
