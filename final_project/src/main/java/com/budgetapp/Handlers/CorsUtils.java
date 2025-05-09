package com.budgetapp.handlers;

import com.sun.net.httpserver.HttpExchange;

public class CorsUtils {

    // Sets required CORS headers to allow frontend to communicate with this server
    public static void setCorsHeaders(HttpExchange exchange) {
        String origin = exchange.getRequestHeaders().getFirst("Origin");
        if (origin != null) {
          // echo back the incoming Origin
          exchange.getResponseHeaders().set("Access-Control-Allow-Origin", origin);
        }
        exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type");
        exchange.getResponseHeaders().set("Vary", "Origin");
      }

    // Handles OPTIONS requests sent by the browser to check CORS permissions
    public static boolean handleOptions(HttpExchange exchange) {
        if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
            setCorsHeaders(exchange);
            try {
                exchange.sendResponseHeaders(204, -1); // No Content
                exchange.close();
            } catch (Exception e) {
                e.printStackTrace(); // Log error if response fails
            }
            return true;
        }
        return false; // Not an OPTIONS request
    }
}
