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
    private IncomeAdapter.OnIncomeLongClickListener longClickListener;

    private List<IncomeModel> incomes;
    private DBHelper dbHelper;


    public IncomeAdapter(List<IncomeModel> incomes,DBHelper dbHelper) {
        this.incomes = incomes;
        this.dbHelper = dbHelper;
    }
    // Define interface for long click listener
    public interface OnIncomeLongClickListener {
        void onLongClick(IncomeModel income);
    }
    // Method to set long click listener
    public void setOnIncomeLongClickListener(IncomeAdapter.OnIncomeLongClickListener listener) {
        this.longClickListener = listener;
    }

    public void setIncomes(List<IncomeModel> incomes) {
        this.incomes = incomes;
    }

    @NonNull
    @Override
    public IncomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_income, parent, false);
        IncomeAdapter.IncomeViewHolder viewHolder = new IncomeAdapter.IncomeViewHolder(view);
        view.setOnLongClickListener(v -> {
            int position = viewHolder.getAdapterPosition();
            if (position != RecyclerView.NO_POSITION && longClickListener != null) {
                longClickListener.onLongClick(incomes.get(position));
                return true;
            }
            return false;
        });
        return viewHolder;
    }




    @Override
    public void onBindViewHolder(@NonNull IncomeAdapter.IncomeViewHolder holder, int position) {
        IncomeModel income = incomes.get(position);
        holder.itemView.setOnLongClickListener(view -> {
            if (longClickListener != null) {
                longClickListener.onLongClick(income);
            }
            return true;
        });
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

        }

        public void bind(IncomeModel income) {
            incomeSourceTextView.setText(income.getCategory());
            incomeDateTextView.setText(income.getDate());
            incomeAmountTextView.setText(String.valueOf(income.getAmount()));
            incomeDescriptionTextView.setText(income.getDescription());
        }

    }
}
