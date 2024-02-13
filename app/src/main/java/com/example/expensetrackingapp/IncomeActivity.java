package com.example.expensetrackingapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;


public class IncomeActivity extends AppCompatActivity {
    private DBHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income);
        dbHelper = new DBHelper(this);


        Button btnAddBudget = findViewById(R.id.btnAddIncome);
        btnAddBudget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddIncomeDialog();
            }
        });
    }

    private void showAddIncomeDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.income_form, null);
        dialogBuilder.setView(dialogView);

        Spinner spinnerIncomeType = dialogView.findViewById(R.id.spinnerIncomeType);
        EditText editTextAmount = dialogView.findViewById(R.id.editTextIncomeAmount);
        EditText editTextDate = dialogView.findViewById(R.id.editTextIncomeDate);
        EditText editTextDescription = dialogView.findViewById(R.id.editTextIncomeDescription);

        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(editTextDate);
            }
        });

        dialogBuilder.setPositiveButton("Add Income", (dialog, which) -> {
            String incomeType = spinnerIncomeType.getSelectedItem().toString();
            String incomeAmountStr = editTextAmount.getText().toString();
            String incomeDate = editTextDate.getText().toString();
            String incomeDescription = editTextDescription.getText().toString();

            if (incomeType.isEmpty() || incomeAmountStr.isEmpty() || incomeDate.isEmpty()) {
                Toast.makeText(IncomeActivity.this, "Please fill in the required fields", Toast.LENGTH_SHORT).show();
                return;
            }

            double incomeAmount = Double.parseDouble(incomeAmountStr);

            // Get user ID from session or wherever it's stored
            int userId = 1; // Example value, you should replace it with the actual user ID

            // Add the income to the database
            if (dbHelper.addIncome(userId, incomeType, incomeDate, incomeAmount, incomeDescription)) {
                Toast.makeText(IncomeActivity.this, "Income added!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(IncomeActivity.this, "Failed to add income", Toast.LENGTH_SHORT).show();
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
