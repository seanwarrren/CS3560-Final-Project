package com.budgetapp.handlers;

import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class DateHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // Handle CORS requests 
        if (CorsUtils.handleOptions(exchange)) return;
        CorsUtils.setCorsHeaders(exchange);

        // Only handle GET requests
        if ("GET".equalsIgnoreCase(exchange.getRequestMethod())) {
            // Get today's date in PST and format it as "Month Day, Year"
            LocalDate today = ZonedDateTime.now(ZoneId.of("America/Los_Angeles")).toLocalDate();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy");
            String response = today.format(formatter);

            // Send formatted date as a plain test response
            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        } else {
            // Reject all non GET requests
            exchange.sendResponseHeaders(405, -1); 
            exchange.close();
        }
    }
}
