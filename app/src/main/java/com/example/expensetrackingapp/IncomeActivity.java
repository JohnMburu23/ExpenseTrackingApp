package com.example.expensetrackingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class IncomeActivity extends AppCompatActivity {

    private TextView salaryTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income);

        salaryTextView = findViewById(R.id.salaryTextView);

        salaryTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(IncomeActivity.this,SalaryActivity.class);
                startActivity(intent);
            }
        });

    }
}