package com.example.expensetrackingapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "expenseTrackerAppDb";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_USERS = "users";
    private static final String COLUMN_ID = "user_id";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PHONE_NUMBER = "phone_number";
    private static final String COLUMN_PASSWORD = "password";


    private static final String TABLE_EXPENSE = "expense";
    private static final String COLUMN_EXPENSE_ID = "expense_id";
    private static final String COLUMN_EXPENSE_USER_ID = "user_id"; // Foreign key
    private static final String COLUMN_EXPENSE_CATEGORY = "category";
    private static final String COLUMN_EXPENSE_DATE = "date";
    private static final String COLUMN_EXPENSE_AMOUNT = "amount";
    private static final String COLUMN_EXPENSE_DESCRIPTION = "description";



    private static final String TABLE_BUDGET = "budget";
    private static final String COLUMN_BUDGET_ID = "budget_id";
    private static final String COLUMN_BUDGET_USER_ID = "user_id";
    private static final String COLUMN_BUDGET_CATEGORY = "category";
    private static final String COLUMN_BUDGET_DATE = "date";
    private static final String COLUMN_BUDGET_AMOUNT = "amount";
    private static final String COLUMN_BUDGET_DESCRIPTION = "description";



    private static final String TABLE_INCOME = "income";
    private static final String COLUMN_INCOME_ID = "income_id";
    private static final String COLUMN_INCOME_USER_ID = "user_id";
    private static final String COLUMN_INCOME_DATE = "date";
    private static final String COLUMN_INCOME_SOURCE = "source";
    private static final String COLUMN_INCOME_AMOUNT = "amount";
    private static final String COLUMN_INCOME_DESCRIPTION = "description";






    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_USERS + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_USERNAME + " TEXT UNIQUE, "
                + COLUMN_PHONE_NUMBER + " TEXT, "
                + COLUMN_PASSWORD + " TEXT)";
        db.execSQL(createTableQuery);

        // Create expense table
        String createExpenseTableQuery = "CREATE TABLE " + TABLE_EXPENSE + " ("
                + COLUMN_EXPENSE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_EXPENSE_USER_ID + " INTEGER, "
                + COLUMN_EXPENSE_CATEGORY + " TEXT, "
                + COLUMN_EXPENSE_DATE + " TEXT, "
                + COLUMN_EXPENSE_AMOUNT + " REAL, "
                + COLUMN_EXPENSE_DESCRIPTION + " TEXT, "
                + "FOREIGN KEY(" + COLUMN_EXPENSE_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_ID + "))";
        db.execSQL(createExpenseTableQuery);

        String createBudgetTableQuery = "CREATE TABLE " + TABLE_BUDGET + " ("
                + COLUMN_BUDGET_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_BUDGET_USER_ID + " INTEGER, "
                + COLUMN_BUDGET_CATEGORY + " TEXT, "
                + COLUMN_BUDGET_DATE + " TEXT, "
                + COLUMN_BUDGET_AMOUNT + " REAL, "
                + COLUMN_BUDGET_DESCRIPTION + " TEXT, "
                + "FOREIGN KEY(" + COLUMN_BUDGET_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_ID + "))";
        db.execSQL(createBudgetTableQuery);

        String createIncomeTableQuery = "CREATE TABLE " + TABLE_INCOME + " ("
                + COLUMN_INCOME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_INCOME_USER_ID + " INTEGER, "
                + COLUMN_INCOME_SOURCE + " TEXT, "
                + COLUMN_INCOME_DATE + " TEXT, "
                + COLUMN_INCOME_AMOUNT + " REAL, "
                + COLUMN_INCOME_DESCRIPTION + " TEXT, "
                + "FOREIGN KEY(" + COLUMN_INCOME_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_ID + "))";
        db.execSQL(createIncomeTableQuery);


    }
    public boolean addExpense(int userId, String category, String date, double amount, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_EXPENSE_USER_ID, userId);
        values.put(COLUMN_EXPENSE_CATEGORY, category);
        values.put(COLUMN_EXPENSE_DATE, date);
        values.put(COLUMN_EXPENSE_AMOUNT, amount);
        values.put(COLUMN_EXPENSE_DESCRIPTION, description);
        try {
            long result = db.insert(TABLE_EXPENSE, null, values);
            return result != -1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            db.close();
        }
    }

    // Method to add an income
    public boolean addIncome(int userId, String source, String date, double amount, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_INCOME_USER_ID, userId);
        values.put(COLUMN_INCOME_SOURCE, source);
        values.put(COLUMN_INCOME_DATE, date);
        values.put(COLUMN_INCOME_AMOUNT, amount);
        values.put(COLUMN_INCOME_DESCRIPTION, description);
        long result = db.insert(TABLE_INCOME, null, values);
        db.close();
        return result != -1;

    }

    // Method to add a budget
    public boolean addBudget(int userId, String category, String date, double amount, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_BUDGET_USER_ID, userId);
        values.put(COLUMN_BUDGET_CATEGORY, category);
        values.put(COLUMN_BUDGET_DATE, date);
        values.put(COLUMN_BUDGET_AMOUNT, amount);
        values.put(COLUMN_BUDGET_DESCRIPTION, description);
        long result = db.insert(TABLE_BUDGET, null, values);
        db.close();
        return result != -1;

    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INCOME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BUDGET);
        onCreate(db);
    }

    // Method to add a new user to the database
    public boolean addUser(String username, String password,String phoneNumber) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PHONE_NUMBER, phoneNumber);
        values.put(COLUMN_PASSWORD, password);
        long result = db.insert(TABLE_USERS, null, values);
        return result != -1;
    }

    // Method to check if a user with given credentials exists in the database
    public boolean checkUser(String username, String phoneNumber, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_ID};
        String selection = COLUMN_USERNAME + "=? AND " + COLUMN_PHONE_NUMBER + "=? AND " + COLUMN_PASSWORD + "=?";
        String[] selectionArgs = {username,phoneNumber,password};
        Cursor cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        return count > 0;
    }
    public boolean checkUserExists(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_ID};
        String selection = COLUMN_USERNAME + "=?";
        String[] selectionArgs = {username};
        Cursor cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        return count > 0;
    }
    public List<ExpenseModel> getUserExpenses(int userId) {
        List<ExpenseModel> expenses = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {
                COLUMN_EXPENSE_ID,
                COLUMN_EXPENSE_CATEGORY,
                COLUMN_EXPENSE_DATE,
                COLUMN_EXPENSE_AMOUNT,
                COLUMN_EXPENSE_DESCRIPTION
        };
        String selection = COLUMN_EXPENSE_USER_ID + "=?";
        String[] selectionArgs = {String.valueOf(userId)};
        Cursor cursor = db.query(
                TABLE_EXPENSE,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {
                ExpenseModel expense = new ExpenseModel();
                expense.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_EXPENSE_ID)));
                expense.setUserId(userId); // Assuming you also want to set user ID for each expense
                expense.setCategory(cursor.getString(cursor.getColumnIndex(COLUMN_EXPENSE_CATEGORY)));
                expense.setDate(cursor.getString(cursor.getColumnIndex(COLUMN_EXPENSE_DATE)));
                expense.setAmount(cursor.getDouble(cursor.getColumnIndex(COLUMN_EXPENSE_AMOUNT)));
                expense.setDescription(cursor.getString(cursor.getColumnIndex(COLUMN_EXPENSE_DESCRIPTION)));
                expenses.add(expense);
            } while (cursor.moveToNext());
            cursor.close();
        }

        return expenses;
    }

    public List<BudgetModel> getUserBudgets(int userId) {
        List<BudgetModel> budgets = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {
                COLUMN_BUDGET_ID,
                COLUMN_BUDGET_CATEGORY,
                COLUMN_BUDGET_DATE,
                COLUMN_BUDGET_AMOUNT,
                COLUMN_BUDGET_DESCRIPTION
        };
        String selection = COLUMN_BUDGET_USER_ID + "=?";
        String[] selectionArgs = {String.valueOf(userId)};
        Cursor cursor = db.query(
                TABLE_BUDGET,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {
                BudgetModel budget = new BudgetModel();
                budget.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_BUDGET_ID)));
                budget.setUserId(userId); // Assuming you also want to set user ID for each budget
                budget.setCategory(cursor.getString(cursor.getColumnIndex(COLUMN_BUDGET_CATEGORY)));
                budget.setDate(cursor.getString(cursor.getColumnIndex(COLUMN_BUDGET_DATE)));
                budget.setAmount(cursor.getDouble(cursor.getColumnIndex(COLUMN_BUDGET_AMOUNT)));
                budget.setDescription(cursor.getString(cursor.getColumnIndex(COLUMN_BUDGET_DESCRIPTION)));
                budgets.add(budget);
            } while (cursor.moveToNext());
            cursor.close();
        }

        return budgets;
    }

    public List<IncomeModel> getUserIncomes(int userId) {
        List<IncomeModel> incomes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {
                COLUMN_INCOME_ID,
                COLUMN_INCOME_SOURCE,
                COLUMN_INCOME_DATE,
                COLUMN_INCOME_AMOUNT,
                COLUMN_INCOME_DESCRIPTION
        };
        String selection = COLUMN_INCOME_USER_ID + "=?";
        String[] selectionArgs = {String.valueOf(userId)};
        Cursor cursor = db.query(
                TABLE_INCOME,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {
                IncomeModel income = new IncomeModel();
                income.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_INCOME_ID)));
                income.setUserId(userId); // Assuming you also want to set user ID for each income
                income.setCategory(cursor.getString(cursor.getColumnIndex(COLUMN_INCOME_SOURCE)));
                income.setDate(cursor.getString(cursor.getColumnIndex(COLUMN_INCOME_DATE)));
                income.setAmount(cursor.getDouble(cursor.getColumnIndex(COLUMN_INCOME_AMOUNT)));
                income.setDescription(cursor.getString(cursor.getColumnIndex(COLUMN_INCOME_DESCRIPTION)));
                incomes.add(income);
            } while (cursor.moveToNext());
            cursor.close();
        }

        return incomes;
    }
    public boolean deleteExpense(int expenseId) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            int rowsAffected = db.delete(TABLE_EXPENSE, COLUMN_EXPENSE_ID + "=?",
                    new String[]{String.valueOf(expenseId)});
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            db.close();
        }
    }
    public boolean deleteIncome(int incomeId) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            int rowsAffected = db.delete(TABLE_INCOME, COLUMN_INCOME_ID + "=?",
                    new String[]{String.valueOf(incomeId)});
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            db.close();
        }
    }
    public boolean deleteBudget(int budgetId) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            int rowsAffected = db.delete(TABLE_BUDGET, COLUMN_BUDGET_ID + "=?",
                    new String[]{String.valueOf(budgetId)});
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            db.close();
        }
    }
    public int getUserIdByUsername(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        int userId = -1;
        String[] columns = {COLUMN_ID};
        String selection = COLUMN_USERNAME + "=?";
        String[] selectionArgs = {username};
        Cursor cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null);
        if (cursor.moveToFirst()) {
            userId = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
        }
        cursor.close();
        db.close();
        return userId;
    }
}
