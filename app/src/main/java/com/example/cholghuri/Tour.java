package com.example.cholghuri;

public class Tour {

    private String tourID;
    private String tourTitle;
    private String tourDetails;
    private int tourAmount;
    private long tourStratDate;
    private long tourEndDate;



    public Tour() {
    }

    public Tour(String tourTitle, String tourDetails, int tourAmount, long tourStratDate, long tourEndDate) {
        this.tourTitle = tourTitle;
        this.tourDetails = tourDetails;
        this.tourAmount = tourAmount;
        this.tourStratDate = tourStratDate;
        this.tourEndDate = tourEndDate;
    }

    public Tour(String tourID, String tourTitle, String tourDetails, int tourAmount, long tourStratDate, long tourEndDate) {
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

    public long getTourStratDate() {
        return tourStratDate;
    }

    public long getTourEndDate() {
        return tourEndDate;
    }

    public void setTourID(String tourID) {
        this.tourID = tourID;
    }
}


