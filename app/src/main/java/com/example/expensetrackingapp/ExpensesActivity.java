package com.example.expensetrackingapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class ExpensesActivity extends AppCompatActivity {
    private DBHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenses);
        dbHelper = new DBHelper(this);


        Button btnAddExpense = findViewById(R.id.btnAddExpense);
        btnAddExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddExpenseDialog();
            }
        });
    }

    private void showAddExpenseDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.expense_form, null);
        dialogBuilder.setView(dialogView);

        Spinner spinnerExpenseType = dialogView.findViewById(R.id.spinnerExpenseType);
        EditText editTextAmount = dialogView.findViewById(R.id.editTextExpenseAmount);
        EditText editTextDate = dialogView.findViewById(R.id.editTextExpenseDate);
        EditText editTextDescription = dialogView.findViewById(R.id.editTextExpenseDescription);

        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(editTextDate);
            }
        });

        dialogBuilder.setPositiveButton("Add Expense", (dialog, which) -> {
            String expenseType = spinnerExpenseType.getSelectedItem().toString();
            String expenseAmountStr = editTextAmount.getText().toString();
            String expenseDate = editTextDate.getText().toString();
            String expenseDescription = editTextDescription.getText().toString();

            if (expenseType.isEmpty() || expenseAmountStr.isEmpty() || expenseDate.isEmpty()) {
                Toast.makeText(ExpensesActivity.this, "Please fill in the required fields", Toast.LENGTH_SHORT).show();
                return;
            }

            double expenseAmount = Double.parseDouble(expenseAmountStr);

            // Get user ID from session or wherever it's stored
            int userId = 1; // Example value, you should replace it with the actual user ID

            // Add the expense to the database
            if (dbHelper.addExpense(userId, expenseType, expenseDate, expenseAmount, expenseDescription)) {
                Toast.makeText(ExpensesActivity.this, "Expense added!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ExpensesActivity.this, "Failed to add expense", Toast.LENGTH_SHORT).show();
            }
        });

        dialogBuilder.setNegativeButton("Cancel", null);

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    private void showDatePicker(final EditText editTextDate) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        editTextDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                    }
                }, year, month, dayOfMonth);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis()-1000);
        datePickerDialog.show();
    }
}
