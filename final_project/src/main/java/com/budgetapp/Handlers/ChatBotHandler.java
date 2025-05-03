package com.budgetapp.handlers;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import org.json.JSONObject;

import com.budgetapp.CohereClient;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class ChatBotHandler implements HttpHandler {

    // Instance of CohereClient to interact with the Cohere API
    private final CohereClient cohereClient = new CohereClient();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // Handle CORS and set headers
        if (CorsUtils.handleOptions(exchange)) {
            return;
        }
            CorsUtils.setCorsHeaders(exchange);

        // Ensure HTTP method is POST
        if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            // Read request body
            InputStream is = exchange.getRequestBody();
            String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);

            // Log raw request
            System.out.println("Raw request body: " + body);

            try {
                // Parse JSON paylod from the request
                JSONObject json = new JSONObject(body);
                String prompt = json.getString("prompt");

                // Validate the prompt
                if (prompt.isEmpty()) {
                    exchange.sendResponseHeaders(400, -1);
                    return;
                }

                // Generate AI response using Cohere
                String reply = cohereClient.generateAdvice(prompt);

                // Set response headers and return reply text
                exchange.getResponseHeaders().set("Content-Type", "text/plain");
                exchange.sendResponseHeaders(200, reply.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(reply.getBytes());
                os.close();
            } catch (Exception e) {
                // Log error and return 400 with error message
                e.printStackTrace(); 
                String error = "Failed to parse JSON: " + e.getMessage();
                exchange.sendResponseHeaders(400, error.length());
                OutputStream os = exchange.getResponseBody();
                os.write(error.getBytes());
                os.close();
            }
        }   
    }
}