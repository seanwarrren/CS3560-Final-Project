package com.budgetapp;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

// Manages a list of transactions
public class TransactionManager {
    
    // List to store all transactions
    private static final List<Transaction> transactions = new ArrayList<>();

    // Counter to generate IDs for each transaction
    private static final AtomicLong idCounter = new AtomicLong();
    
    // Adds a new transaction to the list and assigns a unique ID
    public static synchronized void addTransaction(Transaction transaction) {
        transaction.setId(idCounter.incrementAndGet());
        transactions.add(transaction);
    }

    // Retrieves a copy of all transactions
    public static synchronized List<Transaction> getAllTransactions() {
        return new ArrayList<>(transactions);
    }

    // Removes a transaction by its ID
    public static synchronized void deleteTransaction(long id) {
        transactions.removeIf(tx -> tx.getId() == id);
    }

    // Clears all transactions from the list
    public static synchronized void clearTransactions() {
        transactions.clear();
    }
}
