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

public class IncomeAdapter extends RecyclerView.Adapter<IncomeAdapter.IncomeViewHolder> {

    private List<IncomeModel> incomes;
    private DBHelper dbHelper;


    public IncomeAdapter(List<IncomeModel> incomes,DBHelper dbHelper) {
        this.incomes = incomes;
        this.dbHelper = dbHelper;
    }

    public void setIncomes(List<IncomeModel> incomes) {
        this.incomes = incomes;
    }

    @NonNull
    @Override
    public IncomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_income, parent, false);
        return new IncomeViewHolder(view);
    }




    @Override
    public void onBindViewHolder(@NonNull IncomeAdapter.IncomeViewHolder holder, int position) {
        IncomeModel income = incomes.get(position);
        holder.bind(income);
    }

    @Override
    public int getItemCount() {
        if (incomes == null) {
            return 0;
        }
        return incomes.size();
    }

    public class IncomeViewHolder extends RecyclerView.ViewHolder {

        private TextView incomeSourceTextView;
        private TextView incomeDateTextView;
        private TextView incomeAmountTextView;
        private TextView incomeDescriptionTextView;

        public IncomeViewHolder(@NonNull View itemView) {
            super(itemView);
            incomeSourceTextView = itemView.findViewById(R.id.incomeSourceTextView);
            incomeDateTextView = itemView.findViewById(R.id.incomeDateTextView);
            incomeAmountTextView = itemView.findViewById(R.id.incomeAmountTextView);
            incomeDescriptionTextView = itemView.findViewById(R.id.incomeDescriptionTextView);

<<<<<<< HEAD
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        showDeleteDialog(position);
                        return true;
                    }
                    return false;
=======
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        showDeleteDialog(position);
                    }
>>>>>>> origin/expense-tracker
                }
            });
        }

        public void bind(IncomeModel income) {
            incomeSourceTextView.setText(income.getCategory());
            incomeDateTextView.setText(income.getDate());
            incomeAmountTextView.setText(String.valueOf(income.getAmount()));
            incomeDescriptionTextView.setText(income.getDescription());
        }

        private void showDeleteDialog(final int position) {
            AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
            builder.setTitle("Delete Income Source");
            builder.setMessage("Do you want to remove this Income source?");
            builder.setIcon(R.drawable.ic_delete);

            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dbHelper.deleteIncome(incomes.get(position).getUserId(),incomes.get(position).getIncomeId());

                    // Remove item from list
                    incomes.remove(position);
                    notifyItemRemoved(position);
                }
            });
            builder.setNegativeButton("No", null);
            builder.show();
        }
    }
}
