package com.example.expensetrackingapp;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.app.AlertDialog;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ExpensesAdapter extends RecyclerView.Adapter<ExpensesAdapter.ExpenseViewHolder> {

    private List<ExpenseModel> expenses;
    private DBHelper dbHelper;


    public ExpensesAdapter(List<ExpenseModel> expenses,DBHelper dbHelper) {
        this.expenses = expenses;
        this.dbHelper = dbHelper;

    }

    public void setExpenses(List<ExpenseModel> expenses) {
        this.expenses = expenses;
    }

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_expenses, parent, false);

        return new ExpenseViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position) {
        ExpenseModel expense = expenses.get(position);
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
            categoryTextView = itemView.findViewById(R.id.categoryTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            amountTextView = itemView.findViewById(R.id.amountTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            // Set OnClickListener on the entire item
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        showDeleteDialog(position);
                        return true;
                    }
                    return false;
                }
            });
        }

        public void bind(ExpenseModel expense) {
            categoryTextView.setText(expense.getCategory());
            dateTextView.setText(expense.getDate());
            amountTextView.setText(String.valueOf(expense.getAmount()));
            descriptionTextView.setText(expense.getDescription());
        }
        private void showDeleteDialog(final int position) {
            AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
            builder.setTitle("Delete Expense");
            builder.setIcon(R.drawable.ic_delete);
            builder.setMessage("Do you want to remove this Expense?");

            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dbHelper.deleteExpense(expenses.get(position).getExpenseId());

                    // Remove item from list
                    expenses.remove(position);
                    notifyItemRemoved(position);
                }
            });
            builder.setNegativeButton("No", null);
            builder.show();
        }
    }
}