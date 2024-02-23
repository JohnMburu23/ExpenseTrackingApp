package com.example.expensetrackingapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

public class BudgetingActivity extends AppCompatActivity implements BudgetsAdapter.OnBudgetLongClickListener{
    private DBHelper dbHelper;
    private RecyclerView recyclerView;
    private BudgetsAdapter adapter;
    private List<BudgetModel> budgets;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budgeting);
        dbHelper = new DBHelper(this);

        // Initialize RecyclerView and adapter
        recyclerView = findViewById(R.id.recyclerViewBudgeting);
        adapter = new BudgetsAdapter(budgets,dbHelper);
        adapter.setOnBudgetLongClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


        loadBudgets();


        Button btnAddBudget = findViewById(R.id.btnAddBudget);
        btnAddBudget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddBudgetDialog();
            }
        });
    }
    // Implement long click listener method
    @Override
    public void onLongClick(BudgetModel budget) {
        // Show dialog for confirmation before deletion
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Budget");
        builder.setIcon(R.drawable.ic_delete);
        builder.setMessage("Are you sure you want to delete this budget?");
        builder.setPositiveButton("Delete", (dialogInterface, i) -> {
            // Perform deletion
            if (dbHelper.deleteBudget(budget.getBudgetId())) {
                // Reload budgets after deletion
                loadBudgets();
                Toast.makeText(BudgetingActivity.this, "Budget deleted!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(BudgetingActivity.this, "Failed to delete budget!", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.create().show();
    }
    public void loadBudgets(){
        SharedPreferencesManager sharedPreferencesManager = new SharedPreferencesManager(this);
        int userId = sharedPreferencesManager.getUserId();
        budgets = dbHelper.getUserBudgets(userId);
        adapter.setBudgets(budgets);
        adapter.notifyDataSetChanged();

        double totalBudget = calculateTotalBudget(budgets);
        sharedPreferencesManager.saveTotalBudget(userId,(float) totalBudget);


    }
    private double calculateTotalBudget(List<BudgetModel> budgets) {
        double totalBudget = 0;
        for (BudgetModel budget : budgets) {
            totalBudget += budget.getAmount();
        }
        return totalBudget;
    }


    private void showAddBudgetDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.budget_form, null);
        dialogBuilder.setView(dialogView);

        Spinner spinnerBudgetType = dialogView.findViewById(R.id.spinnerExpenseToBudget);
        EditText editTextAmount = dialogView.findViewById(R.id.editTextBudgetAmount);
        EditText editTextDate = dialogView.findViewById(R.id.editTextBudgetDate);
        EditText editTextDescription = dialogView.findViewById(R.id.editTextBudgetDescription);

        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(editTextDate);
            }
        });

        dialogBuilder.setTitle("Add Budget");
        dialogBuilder.setIcon(R.drawable.ic_plus);
        dialogBuilder.setPositiveButton("Set Budget", (dialog, which) -> {
            String budgetType = spinnerBudgetType.getSelectedItem().toString();
            String budgetAmountStr = editTextAmount.getText().toString();
            String budgetDate = editTextDate.getText().toString();
            String budgetDescription = editTextDescription.getText().toString();

            if (budgetType.isEmpty() || budgetAmountStr.isEmpty() || budgetDate.isEmpty()) {
                Toast.makeText(BudgetingActivity.this, "Please fill in the required fields", Toast.LENGTH_SHORT).show();
                return;
            }

            double budgetAmount = Double.parseDouble(budgetAmountStr);

            // Get user ID from shared preferences
            SharedPreferencesManager sharedPreferencesManager = new SharedPreferencesManager(this);
            int userId = sharedPreferencesManager.getUserId();


            // Add the budget to the database
            if (dbHelper.addBudget(userId, budgetType, budgetDate, budgetAmount, budgetDescription)) {
                budgets = dbHelper.getUserBudgets(userId);
                adapter.setBudgets(budgets);
                adapter.notifyDataSetChanged();
                updateTotalBudgetDisplay();
                Toast.makeText(BudgetingActivity.this, "Budget set successfully!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(BudgetingActivity.this, "Failed to set budget", Toast.LENGTH_SHORT).show();
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
    private void updateTotalBudgetDisplay() {
        SharedPreferencesManager sharedPreferencesManager = new SharedPreferencesManager(this);
        double totalBudget = calculateTotalBudget(budgets);
        sharedPreferencesManager.saveTotalBudget(sharedPreferencesManager.getUserId(),(float) totalBudget);
    }
}
