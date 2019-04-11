package com.example.cholghuri;

public class Tour {

    private String tourID;
    private String tourTitle;
    private String tourDetails;
    private int tourAmount;
    private double tourStratDate;
    private double tourEndDate;



    public Tour() {
    }

    public Tour(String tourTitle, String tourDetails, int tourAmount, double tourStratDate, double tourEndDate) {
        this.tourTitle = tourTitle;
        this.tourDetails = tourDetails;
        this.tourAmount = tourAmount;
        this.tourStratDate = tourStratDate;
        this.tourEndDate = tourEndDate;
    }

    public Tour(String tourID, String tourTitle, String tourDetails, int tourAmount, double tourStratDate, double tourEndDate) {
        this.tourID = tourID;
        this.tourTitle = tourTitle;
        this.tourDetails = tourDetails;
        this.tourAmount = tourAmount;
        this.tourStratDate = tourStratDate;
        this.tourEndDate = tourEndDate;
    }

    public String getTourID() {
        return tourID;
    }

    public String getTourTitle() {
        return tourTitle;
    }

    public String getTourDetails() {
        return tourDetails;
    }

    public int getTourAmount() {
        return tourAmount;
    }

    public double getTourStratDate() {
        return tourStratDate;
    }

    public double getTourEndDate() {
        return tourEndDate;
    }

    public void setTourID(String tourID) {
        this.tourID = tourID;
    }
}


