package com.example.expensetrackingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

public class HomeActivity extends AppCompatActivity {
    private TextView userNameTextView;
    private TextView phoneNumberTextView;

    private DrawerLayout drawerLayout;

   private MaterialToolbar toolbar;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        SharedPreferencesManager sharedPreferencesManager = new SharedPreferencesManager(this);


        TextView totalExpensesTextView = findViewById(R.id.totalExpensesTextView);
        double totalExpenses = sharedPreferencesManager.getTotalExpenses();
        totalExpensesTextView.setText("Ksh " + totalExpenses);


        TextView totalBudgetTextView = findViewById(R.id.totalBudget);
        double totalBudgets = sharedPreferencesManager.getTotalBudget();
        totalBudgetTextView.setText("Ksh " + totalBudgets);


        TextView totalIncomeTextView = findViewById(R.id.totalIncome);
        double totalIncome = sharedPreferencesManager.getTotalIncome();
        totalIncomeTextView.setText("Ksh " + totalIncome);

        TextView totalBalanceTextView = findViewById(R.id.totalBalance);
        double totalBalance = totalIncome - totalExpenses;
        totalBalanceTextView.setText("Ksh "+ totalBalance);


        NavigationView navView = findViewById(R.id.nav_view);
        View headerView = navView.getHeaderView(0);
        userNameTextView = headerView.findViewById(R.id.navHeaderUsernameTextView);
        phoneNumberTextView = headerView.findViewById(R.id.navHeaderPhoneNumberTextView);

        String username = getIntent().getStringExtra("username");
        String phoneNumber = getIntent().getStringExtra("phoneNumber");

        userNameTextView.setText(username);
        phoneNumberTextView.setText(phoneNumber);


        drawerLayout = findViewById(R.id.drawer);
        toolbar = findViewById(R.id.hometoolbar);
        bottomNavigationView = findViewById(R.id.bottomnavigationview);
        toolbar.setTitle("Home");

        ActionBarDrawerToggle toggle= new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.drawer_close,R.string.drawer_open);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                if(id == R.id.nav_settings){
                    Intent intent = new Intent(HomeActivity.this,SettingsActivity.class);
                    startActivity(intent);
                }
                else if(id == R.id.nav_home){
                    //Intent intent = new Intent(HomeActivity.this,HomeActivity.class);
                    //startActivity(intent);
                    //finish();
                }
                else if(id == R.id.nav_logout){
                    onBackPressed();
                }
                drawerLayout.closeDrawers();
                return true;
            }
        });



        bottomNavigationView.setLabelVisibilityMode(NavigationBarView.LABEL_VISIBILITY_LABELED);//so that both the icon and the name are seen
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.expenses:
                    startActivity(new Intent(HomeActivity.this,ExpensesActivity.class));
                    return true;
                case R.id.budgeting:
                    startActivity(new Intent(HomeActivity.this,BudgetingActivity.class));
                    return true;
                case R.id.income:
                    startActivity(new Intent(HomeActivity.this,IncomeActivity.class));
                    return true;
            }
            return false;
        });


    }

    @Override
    public void  onBackPressed(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Exit");
        builder.setIcon(R.drawable.ic_exit);
        builder.setMessage("Do you want to exit the app?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //do nothing
                    }
                });
        builder.create().show();//to create and show the AlertDialog
    }
}