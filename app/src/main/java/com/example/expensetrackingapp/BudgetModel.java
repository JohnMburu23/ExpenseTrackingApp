package com.example.expensetrackingapp;

public class BudgetModel {
    private int budgetId;
    private int userId;
    private String category;
    private String date;
    private double amount;
    private String description;

    // Constructor
    public BudgetModel() {
    }

    // Getters
    public int getBudgetId() {
        return budgetId;
    }

    public int getUserId() {
        return userId;
    }

    public String getCategory() {
        return category;
    }

    public String getDate() {
        return date;
    }

    public double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    // Setters
    public void setId(int budgetId) {
        this.budgetId = budgetId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

