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

        if ("GET".equalsIgnoreCase(exchange.getRequestMethod())) {
            CorsUtils.setCorsHeaders(exchange);

            List<Transaction> transactions = TransactionManager.getAllTransactions();
            JSONArray jsonArray = new JSONArray();

            for (Transaction t : transactions) {
                JSONObject obj = new JSONObject();
                obj.put("id", t.getId());
                obj.put("name", t.getName());
                obj.put("category", t.getCategory());
                obj.put("amount", t.getAmount());
                obj.put("type", t.getType().toString());
                jsonArray.put(obj);
            }

            String response = jsonArray.toString();
            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        } else {
            exchange.sendResponseHeaders(405, -1); // Method Not Allowed
        }
    }
}
