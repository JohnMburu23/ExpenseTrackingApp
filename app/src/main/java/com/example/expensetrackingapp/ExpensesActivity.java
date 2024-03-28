package com.example.expensetrackingapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;

import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ExpensesActivity extends AppCompatActivity implements ExpensesAdapter.OnExpenseLongClickListener{
    private DBHelper dbHelper;
    private RecyclerView recyclerView;
    private ExpensesAdapter adapter;
    private List<ExpenseModel> expenses;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenses);
        dbHelper = new DBHelper(this);





        // Initialize RecyclerView and adapter
        recyclerView = findViewById(R.id.recyclerViewExpenses);
        adapter = new ExpensesAdapter(expenses, dbHelper);
        adapter.setOnExpenseLongClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


        loadExpenses();


        Button btnAddExpense = findViewById(R.id.btnAddExpense);
        btnAddExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddExpenseDialog();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_expenses,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.sort_date:
                sortItemsByDate();
                return true;
            case R.id.sort_amount:
                sortItemsByAmount();
                return true;
            case R.id.sort_alphabetical:
                sortItemsAlphabetically();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    // Implement long click listener method
    @Override
    public void onLongClick(ExpenseModel expense) {
        // Show dialog for confirmation before deletion
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Expense");
        builder.setIcon(R.drawable.ic_delete);
        builder.setMessage("Are you sure you want to delete this expense?");
        builder.setPositiveButton("Delete", (dialogInterface, i) -> {
            // Perform deletion
            if (dbHelper.deleteExpense(expense.getExpenseId())) {
                // Reload expenses after deletion
                loadExpenses();
                Toast.makeText(ExpensesActivity.this, "Expense deleted!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ExpensesActivity.this, "Failed to delete expense!", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.create().show();
    }
    public void loadExpenses(){
        SharedPreferencesManager sharedPreferencesManager = new SharedPreferencesManager(this);
        int userId = sharedPreferencesManager.getUserId();

        expenses = dbHelper.getUserExpenses(userId);
        adapter.setExpenses(expenses);
        adapter.notifyDataSetChanged();

        double totalExpenses = calculateTotalExpenses(expenses);
        sharedPreferencesManager.saveTotalExpenses(userId,(float) totalExpenses);


    }
    public void sortItemsByDate(){
        loadExpenses();
        adapter.notifyDataSetChanged();
    }
    public void sortItemsByAmount(){
        Collections.sort(expenses,new Comparator<ExpenseModel>(){
            @Override
            public int compare(ExpenseModel o1,ExpenseModel o2){
                return Double.compare(o1.getAmount(),o2.getAmount());
            }
        });
        adapter.notifyDataSetChanged();
    }
    public void sortItemsAlphabetically(){
        Collections.sort(expenses,new Comparator<ExpenseModel>(){
            @Override
            public int compare(ExpenseModel o1,ExpenseModel o2){
                return o1.getCategory().compareTo(o2.getCategory());
            }
        });
        adapter.notifyDataSetChanged();
    }

    private double calculateTotalExpenses(List<ExpenseModel> expenses) {
        double totalExpenses = 0;
        for (ExpenseModel expense : expenses) {
            totalExpenses += expense.getAmount();
        }
        return totalExpenses;
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
        dialogBuilder.setIcon(R.drawable.ic_plus);
        dialogBuilder.setTitle("Add Expense");
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
            SharedPreferencesManager sharedPreferencesManager = new SharedPreferencesManager(this);
            int userId = sharedPreferencesManager.getUserId();

            // Add the expense to the database
            expenses = dbHelper.getUserExpenses(userId);
            if (dbHelper.addExpense(userId, expenseType, expenseDate, expenseAmount, expenseDescription)) {
                expenses = dbHelper.getUserExpenses(userId);
                adapter.setExpenses(expenses);
                adapter.notifyDataSetChanged();
                updateTotalExpensesDisplay();

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
    private void updateTotalExpensesDisplay() {
        SharedPreferencesManager sharedPreferencesManager = new SharedPreferencesManager(this);
        int userId = sharedPreferencesManager.getUserId();
        double totalExpenses = calculateTotalExpenses(expenses);
        sharedPreferencesManager.saveTotalExpenses(userId,(float) totalExpenses);
    }

}
