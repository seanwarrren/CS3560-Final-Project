package com.budgetapp;

enum TransactionType {INCOME, EXPENSE}

public class Transaction {
    private long id;
    private String category;
    private double amount;
    private TransactionType type;

    public Transaction(long id, String category, double amount, TransactionType type) {
        this.id = id;
        this.category = category;
        this.amount = amount; 
        this.type = type;
    }

    // getters and setters
    
}

