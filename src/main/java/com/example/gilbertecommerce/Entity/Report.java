package com.example.gilbertecommerce.Entity;

public class Report {

    private int reportedUserID;
    private int reportingUserID;
    private int reportedListingID;
    private String complaintMSG;
    private int reportID;

    public Report(){}
    public Report(int reportedUserID, int reportingUserID, int reportedListingID, String complaintMSG, int reportID) {
        this.reportedUserID = reportedUserID;
        this.reportingUserID = reportingUserID;
        this.reportedListingID = reportedListingID;
        this.complaintMSG = complaintMSG;
        this.reportID = reportID;
    }



}
