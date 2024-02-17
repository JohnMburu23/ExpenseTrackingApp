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

    private List<BudgetModel> budgets;
    private DBHelper dbHelper;


    public BudgetsAdapter(List<BudgetModel> budgets,DBHelper dbHelper) {
        this.budgets = budgets;
        this.dbHelper = dbHelper;
    }

    public void setBudgets(List<BudgetModel> budgets) {
        this.budgets = budgets;
    }

    @NonNull
    @Override
    public BudgetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_budget, parent, false);
        return new BudgetViewHolder(view);
    }




    @Override
    public void onBindViewHolder(@NonNull BudgetsAdapter.BudgetViewHolder holder, int position) {
        BudgetModel budget = budgets.get(position);
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

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        showDeleteDialog(position);
                    }
                }
            });
        }

        public void bind(BudgetModel budget) {
            budgetCategoryTextView.setText(budget.getCategory());
            budgetDateTextView.setText(budget.getDate());
            budgetAmountTextView.setText(String.valueOf(budget.getAmount()));
            budgetDescriptionTextView.setText(budget.getDescription());
        }

        private void showDeleteDialog(final int position) {
            AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
            builder.setTitle("Delete Budget");
            builder.setMessage("Do you want to remove this Budget?");

            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dbHelper.deleteBudget(budgets.get(position).getUserId(),budgets.get(position).getBudgetId());

                    // Remove item from list
                    budgets.remove(position);
                    notifyItemRemoved(position);
                }
            });
            builder.setNegativeButton("No", null);
            builder.show();
        }
    }
}
