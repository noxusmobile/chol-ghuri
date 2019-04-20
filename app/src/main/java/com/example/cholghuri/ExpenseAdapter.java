package com.example.cholghuri;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ViewHolder> {
    @NonNull


    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private String userID;


    private List<Expense> expenseList;
    private Context context;

    public ExpenseAdapter(Context context,List<Expense> expenseList, String tourID) {
        this.expenseList = expenseList;
        this.context = context;
    }

    public ExpenseAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        userID = firebaseAuth.getCurrentUser().getUid();


        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.expense_list_layout, viewGroup, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseAdapter.ViewHolder viewHolder, int i) {


        final Expense currentExpense = expenseList.get(i);
        viewHolder.expenseNameTV.setText(currentExpense.getExpenseTitle());
        String temp = String.valueOf(currentExpense.getExpenseAmount());

         viewHolder.expenseAmountTV.setText(temp);
        viewHolder.expenseDescriptionTV.setText(currentExpense.getExpenseDetails());


    }

    @Override
    public int getItemCount() {
        return expenseList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {


        private TextView expenseNameTV,expenseAmountTV,expenseDescriptionTV;
        private Button expenseDeleteBTN,expenseUpdateBTN;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            expenseNameTV = itemView.findViewById(R.id.expenseNameTV);
            expenseAmountTV = itemView.findViewById(R.id.expenseAmountTV);
            expenseDescriptionTV = itemView.findViewById(R.id.expenseDescriptionTV);
            expenseDeleteBTN = itemView.findViewById(R.id.expenseDeleteBTN);
            expenseUpdateBTN = itemView.findViewById(R.id.expenseUpdateBTN);

        }
    }
}