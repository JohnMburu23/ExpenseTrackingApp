package com.example.expensetrackingapp;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesManager {
    private static final String PREF_NAME = "my_prefs";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_TOTAL_EXPENSES = "total_expenses";
    private static final String KEY_TOTAL_BUDGET = "total_budget";
    private static final String KEY_TOTAL_INCOME = "total_income";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;

    public SharedPreferencesManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveUserId(int userId) {
        editor.putInt(KEY_USER_ID, userId);
        editor.apply();
    }

    public int getUserId() {
        return sharedPreferences.getInt(KEY_USER_ID, -1); // Return -1 if user ID is not found
    }

    public void saveTotalExpenses(int userId,float totalExpenses) {
        editor.putFloat(KEY_TOTAL_EXPENSES + userId, totalExpenses);
        editor.apply();
    }

    public float getTotalExpenses(int userId) {
        return sharedPreferences.getFloat(KEY_TOTAL_EXPENSES + userId, 0.0f);
    }

    public void saveTotalIncome(int userId,float totalIncome) {
        editor.putFloat(KEY_TOTAL_INCOME + userId, totalIncome);
        editor.apply();
    }

    public float getTotalIncome(int userId) {
        return sharedPreferences.getFloat(KEY_TOTAL_INCOME + userId, 0.0f);
    }

    public void saveTotalBudget(int userId,float totalBudget) {
        editor.putFloat(KEY_TOTAL_BUDGET + userId, totalBudget);
        editor.apply();
    }

    public float getTotalBudget(int userId) {
        return sharedPreferences.getFloat(KEY_TOTAL_BUDGET + userId, 0.0f);
    }
}
