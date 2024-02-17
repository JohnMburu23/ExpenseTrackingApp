package com.example.expensetrackingapp;

public class IncomeModel {
    private int incomeId;
    private int userId;
    private String category;
    private String date;
    private double amount;
    private String description;

    // Constructor
    public IncomeModel() {
    }

    // Getters
    public int getIncomeId() {
        return incomeId;
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
    public void setId(int incomeId) {
        this.incomeId = incomeId;
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

