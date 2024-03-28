package com.example.expensetrackingapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class IncomeActivity extends AppCompatActivity implements IncomeAdapter.OnIncomeLongClickListener{
    private DBHelper dbHelper;
    private RecyclerView recyclerView;
    private IncomeAdapter adapter;
    private List<IncomeModel> incomes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income);
        dbHelper = new DBHelper(this);

        // Initialize RecyclerView and adapter
        recyclerView = findViewById(R.id.recyclerViewIncome);
        adapter = new IncomeAdapter(incomes,dbHelper);
        adapter.setOnIncomeLongClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


        loadIncomes();


        Button btnAddBudget = findViewById(R.id.btnAddIncome);
        btnAddBudget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddIncomeDialog();
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
    public void onLongClick(IncomeModel income) {
        // Show dialog for confirmation before deletion
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Income");
        builder.setIcon(R.drawable.ic_delete);
        builder.setMessage("Are you sure you want to delete this income?");
        builder.setPositiveButton("Delete", (dialogInterface, i) -> {
            // Perform deletion
            if (dbHelper.deleteIncome(income.getIncomeId())) {
                // Reload incomes after deletion
                loadIncomes();
                Toast.makeText(IncomeActivity.this, "Income deleted!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(IncomeActivity.this, "Failed to delete income!", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.create().show();
    }
    public void loadIncomes(){
        SharedPreferencesManager sharedPreferencesManager = new SharedPreferencesManager(this);
        int userId = sharedPreferencesManager.getUserId();
        incomes = dbHelper.getUserIncomes(userId);
        adapter.setIncomes(incomes);
        adapter.notifyDataSetChanged();

        double totalIncome = calculateTotalIncome(incomes);
        sharedPreferencesManager.saveTotalIncome(userId,(float) totalIncome);
    }
    public void sortItemsByDate(){
        loadIncomes();
        adapter.notifyDataSetChanged();
    }
    public void sortItemsByAmount(){
        Collections.sort(incomes,new Comparator<IncomeModel>(){
            @Override
            public int compare(IncomeModel o1,IncomeModel o2){
                return Double.compare(o1.getAmount(),o2.getAmount());
            }
        });
        adapter.notifyDataSetChanged();
    }
    public void sortItemsAlphabetically(){
        Collections.sort(incomes,new Comparator<IncomeModel>(){
            @Override
            public int compare(IncomeModel o1,IncomeModel o2){
                return o1.getCategory().compareTo(o2.getCategory());
            }
        });
        adapter.notifyDataSetChanged();
    }


    private double calculateTotalIncome(List<IncomeModel> incomes) {
        double totalIncome = 0;
        for (IncomeModel income : incomes) {
            totalIncome += income.getAmount();
        }
        return totalIncome;
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

        dialogBuilder.setIcon(R.drawable.ic_plus);
        dialogBuilder.setTitle("Add Income");
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

            SharedPreferencesManager sharedPreferencesManager = new SharedPreferencesManager(this);
            int userId = sharedPreferencesManager.getUserId();

            // Add income to the database
            if (dbHelper.addIncome(userId, incomeType, incomeDate, incomeAmount, incomeDescription)) {
                incomes = dbHelper.getUserIncomes(userId);
                adapter.setIncomes(incomes);
                adapter.notifyDataSetChanged();
                updateTotalIncomeDisplay();
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
    private void updateTotalIncomeDisplay() {
        SharedPreferencesManager sharedPreferencesManager = new SharedPreferencesManager(this);
        double totalIncome = calculateTotalIncome(incomes);
        sharedPreferencesManager.saveTotalIncome(sharedPreferencesManager.getUserId(),(float) totalIncome);
    }
}
