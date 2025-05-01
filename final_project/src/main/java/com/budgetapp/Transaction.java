package com.budgetapp;

import java.util.concurrent.atomic.AtomicLong;

public class Transaction {
    private static final AtomicLong counter = new AtomicLong();
    
    private long id;
    private String name;
    private String category;
    private double amount;
    private TransactionType type;

    public Transaction(long id, String name, String category, double amount, TransactionType type) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.amount = amount; 
        this.type = type;
    }

    public Transaction(String name, String category, double amount, TransactionType type) {
        this.name = name;
        this.id = counter.incrementAndGet();
        this.category = category;
        this.amount = amount;
        this.type = type;
    }

    // getters and setters
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

