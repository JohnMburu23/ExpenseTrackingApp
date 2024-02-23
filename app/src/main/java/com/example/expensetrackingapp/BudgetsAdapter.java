package com.example.expensetrackingapp;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BudgetsAdapter extends RecyclerView.Adapter<BudgetsAdapter.BudgetViewHolder> {
    private BudgetsAdapter.OnBudgetLongClickListener longClickListener;

    private List<BudgetModel> budgets;
    private DBHelper dbHelper;


    public BudgetsAdapter(List<BudgetModel> budgets,DBHelper dbHelper) {
        this.budgets = budgets;
        this.dbHelper = dbHelper;
    }
    // Define interface for long click listener
    public interface OnBudgetLongClickListener {
        void onLongClick(BudgetModel budget);
    }
    // Method to set long click listener
    public void setOnBudgetLongClickListener(BudgetsAdapter.OnBudgetLongClickListener listener) {
        this.longClickListener = listener;
    }

    public void setBudgets(List<BudgetModel> budgets) {
        this.budgets = budgets;
    }

    @NonNull
    @Override
    public BudgetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_budget, parent, false);
        BudgetsAdapter.BudgetViewHolder viewHolder = new BudgetsAdapter.BudgetViewHolder(view);
        view.setOnLongClickListener(v -> {
            int position = viewHolder.getAdapterPosition();
            if (position != RecyclerView.NO_POSITION && longClickListener != null) {
                longClickListener.onLongClick(budgets.get(position));
                return true;
            }
            return false;
        });
        return viewHolder;
    }




    @Override
    public void onBindViewHolder(@NonNull BudgetsAdapter.BudgetViewHolder holder, int position) {
        BudgetModel budget = budgets.get(position);
        holder.itemView.setOnLongClickListener(view -> {
            if (longClickListener != null) {
                longClickListener.onLongClick(budget);
            }
            return true;
        });
        holder.bind(budget);
    }

    @Override
    public int getItemCount() {
        if (budgets == null) {
            return 0;
        }
        return budgets.size();
    }

    public class BudgetViewHolder extends RecyclerView.ViewHolder {

        private TextView budgetCategoryTextView;
        private TextView budgetDateTextView;
        private TextView budgetAmountTextView;
        private TextView budgetDescriptionTextView;

        public BudgetViewHolder(@NonNull View itemView) {
            super(itemView);
            budgetCategoryTextView = itemView.findViewById(R.id.budgetCategoryTextView);
            budgetDateTextView = itemView.findViewById(R.id.budgetDateTextView);
            budgetAmountTextView = itemView.findViewById(R.id.budgetAmountTextView);
            budgetDescriptionTextView = itemView.findViewById(R.id.budgetDescriptionTextView);
        }

        public void bind(BudgetModel budget) {
            budgetCategoryTextView.setText(budget.getCategory());
            budgetDateTextView.setText(budget.getDate());
            budgetAmountTextView.setText(String.valueOf(budget.getAmount()));
            budgetDescriptionTextView.setText(budget.getDescription());
        }


    }
}
