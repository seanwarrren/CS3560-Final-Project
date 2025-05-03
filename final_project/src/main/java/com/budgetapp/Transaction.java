package com.budgetapp;

import java.util.concurrent.atomic.AtomicLong;

// Represents a transaction, such as income, expense, or bill
// Each transaction has a unique ID, name, category, amount, and type
public class Transaction {

    // Atomic counter to ensure unique transaction IDs 
    private static final AtomicLong counter = new AtomicLong();
    
    private long id;
    private String name;
    private String category;
    private double amount;
    private TransactionType type;

    // Constructor for manually assigning an ID
    public Transaction(long id, String name, String category, double amount, TransactionType type) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.amount = amount; 
        this.type = type;
    }

    // Constructor that auto-generates an ID using atomic counter
    public Transaction(String name, String category, double amount, TransactionType type) {
        this.name = name;
        this.id = counter.incrementAndGet();
        this.category = category;
        this.amount = amount;
        this.type = type;
    }

    // Getters and setters
    public long getId() { 
        return id; 
    }

    public void setId(long id) 
    { 
        this.id = id; 
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() { 
        return category; 
    }

    public void setCategory(String category) {
         this.category = category;
    }

    public double getAmount() { 
        return amount;
    }

    public void setAmount(double amount) { 
        this.amount = amount; 
    }

    public TransactionType getType() { 
        return type; 
    }

    public void setType(TransactionType type) { 
        this.type = type; 
    }

}

