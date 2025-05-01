package com.budgetapp;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.budgetapp.handlers.AddTransactionHandler;
import com.budgetapp.handlers.CreateUserHandler;
import com.budgetapp.handlers.DateHandler;
import com.budgetapp.handlers.DeleteTransactionHandler;
import com.budgetapp.handlers.DeleteUserHandler;
import com.budgetapp.handlers.GetTransactionsHandler;
import com.sun.net.httpserver.HttpServer;

public class WebServer {
    public static void main(String[] args) throws IOException {
        // Create HTTP server on port 8080
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        // (You will add contexts/handlers here later)
        server.createContext("/api/date", new DateHandler());
        server.createContext("/api/create-user", new CreateUserHandler());
        server.createContext("/api/delete-user", new DeleteUserHandler());
        server.createContext("/api/transactions", new GetTransactionsHandler());
        server.createContext("/api/add-transaction", new AddTransactionHandler());
        server.createContext("/api/get-transactions", new GetTransactionsHandler());
        server.createContext("/api/transactions/delete", new DeleteTransactionHandler());


        server.setExecutor(null); // creates a default executor
        server.start();
        System.out.println("Server started at http://localhost:8080");
    }
}
