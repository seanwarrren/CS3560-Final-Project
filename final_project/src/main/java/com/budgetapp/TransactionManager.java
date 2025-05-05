package com.budgetapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

// Manages a list of transactions
public class TransactionManager {
    
    // Map username -> their transactions
    private static final Map<String, List<Transaction>> transactionsByUser = new HashMap<>();

    // Counter to generate IDs for each transaction
    private static final AtomicLong idCounter = new AtomicLong();
    
    // Adds a new transaction for the given user
    public static synchronized void addTransaction(String user, Transaction t) {
        t.setId(idCounter.incrementAndGet());
        transactionsByUser
          .computeIfAbsent(user, u -> new ArrayList<>())
          .add(t);
      }

    // Retrieves a copy of all transactions for the given user
    public static synchronized List<Transaction> getAllTransactions(String user) {
        return new ArrayList<>(transactionsByUser.getOrDefault(user, List.of()));
      }
    

    // Removes a transaction by its ID for the given user
    public static synchronized void deleteTransaction(String user, long id) {
        List<Transaction> list = transactionsByUser.get(user);
        if (list != null) {
          list.removeIf(t -> t.getId() == id);
        }
      }

    // Clears all transactions for the given user
    public static synchronized void clearTransactions(String user) {
        transactionsByUser.remove(user);
      }
}
