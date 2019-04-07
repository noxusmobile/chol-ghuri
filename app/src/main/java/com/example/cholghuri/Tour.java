package com.example.cholghuri;

public class Tour {

    private String TourID;
    private String TourTitle;
    private String TourDetails;
    private int TourAmount;
    private double TourStratDate;
    private double TourEndDate;


    public Tour() {
    }


    public Tour(String tourTitle, String tourDetails, int tourAmount, double tourStratDate, double tourEndDate) {
        TourTitle = tourTitle;
        TourDetails = tourDetails;
        TourAmount = tourAmount;
        TourStratDate = tourStratDate;
        TourEndDate = tourEndDate;
    }

    public String getTourTitle() {
        return TourTitle;
    }

    public String getTourDetails() {
        return TourDetails;
    }

    public int getTourAmount() {
        return TourAmount;
    }

    public double getTourStratDate() {
        return TourStratDate;
    }

    public double getTourEndDate() {
        return TourEndDate;
    }

    public String getTourID() {
        return TourID;
    }


    public void setTourID(String tourID) {
        TourID = tourID;
    }
}


