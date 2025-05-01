package com.budgetapp;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class TransactionManager {
    private static final List<Transaction> transactions = new ArrayList<>();
    private static final AtomicLong idCounter = new AtomicLong();
    
    public static synchronized void addTransaction(Transaction transaction) {
        transaction.setId(idCounter.incrementAndGet());
        transactions.add(transaction);
    }

    public static synchronized List<Transaction> getAllTransactions() {
        return new ArrayList<>(transactions);
    }

    public static void deleteTransaction(long id) {
        transactions.removeIf(tx -> tx.getId() == id);
    }

    public static synchronized void clearTransactions() {
        transactions.clear();
    }
}
