package com.budgetapp;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.budgetapp.Handlers.DateHandler;
import com.sun.net.httpserver.HttpServer;

public class WebServer {
    public static void main(String[] args) throws IOException {
        // Create HTTP server on port 8080
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        // (You will add contexts/handlers here later)
        server.createContext("/api/date", new DateHandler());

        server.setExecutor(null); // creates a default executor
        server.start();
        System.out.println("Server started at http://localhost:8080");
    }
}
