package com.example.expensetrackingapp;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.app.AlertDialog;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ExpensesAdapter extends RecyclerView.Adapter<ExpensesAdapter.ExpenseViewHolder> {
    private OnExpenseLongClickListener longClickListener;

    private List<ExpenseModel> expenses;
    private DBHelper dbHelper;


    public ExpensesAdapter(List<ExpenseModel> expenses,DBHelper dbHelper) {
        this.expenses = expenses;
        this.dbHelper = dbHelper;

    }
    // Define interface for long click listener
    public interface OnExpenseLongClickListener {
        boolean onOptionsItemSelected(MenuItem item);

        void onLongClick(ExpenseModel expense);
    }
    // Method to set long click listener
    public void setOnExpenseLongClickListener(OnExpenseLongClickListener listener) {
        this.longClickListener = listener;
    }

    public void setExpenses(List<ExpenseModel> expenses) {
        this.expenses = expenses;
    }

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_expenses, parent, false);
        ExpenseViewHolder viewHolder = new ExpenseViewHolder(view);
        view.setOnLongClickListener(v -> {
            int position = viewHolder.getAdapterPosition();
            if (position != RecyclerView.NO_POSITION && longClickListener != null) {
                longClickListener.onLongClick(expenses.get(position));
                return true;
            }
            return false;
        });
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position) {
        ExpenseModel expense = expenses.get(position);
        holder.itemView.setOnLongClickListener(view -> {
            if (longClickListener != null) {
                longClickListener.onLongClick(expense);
            }
            return true;
        });
        holder.bind(expense);

    }

    @Override
    public int getItemCount() {
        if (expenses == null) {
            return 0;
        }
        return expenses.size();
    }

    public class ExpenseViewHolder extends RecyclerView.ViewHolder {

        private TextView categoryTextView;
        private TextView dateTextView;
        private TextView amountTextView;
        private TextView descriptionTextView;

        public ExpenseViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setClickable(true);
            categoryTextView = itemView.findViewById(R.id.categoryTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            amountTextView = itemView.findViewById(R.id.amountTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);

        }

        public void bind(ExpenseModel expense) {
            categoryTextView.setText(expense.getCategory());
            dateTextView.setText(expense.getDate());
            amountTextView.setText(String.valueOf(expense.getAmount()));
            descriptionTextView.setText(expense.getDescription());
        }

    }
}