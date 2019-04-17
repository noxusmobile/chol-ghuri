package com.example.cholghuri;

public class Expense {

    private String tourID;
    private String expenseID;
    private String expenseTitle;
    private String expenseDetails;
    private int expenseAmount;

    public Expense() {
    }

    public Expense(String expenseTitle, String expenseDetails, int expenseAmount) {
        this.expenseTitle = expenseTitle;
        this.expenseDetails = expenseDetails;
        this.expenseAmount = expenseAmount;
    }

    public Expense(String expenseID, String expenseTitle, String expenseDetails, int expenseAmount) {
        this.expenseID = expenseID;
        this.expenseTitle = expenseTitle;
        this.expenseDetails = expenseDetails;
        this.expenseAmount = expenseAmount;
    }

    public String getTourID() {
        return tourID;
    }

    public String getExpenseID() {
        return expenseID;
    }

    public String getExpenseTitle() {
        return expenseTitle;
    }

    public String getExpenseDetails() {
        return expenseDetails;
    }

    public int getExpenseAmount() {
        return expenseAmount;
    }

    public void setExpenseID(String expenseID) {
        this.expenseID = expenseID;
    }

    public void setTourID(String tourID) {
        this.tourID = tourID;
    }
}
