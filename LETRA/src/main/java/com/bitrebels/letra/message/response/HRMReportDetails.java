package com.bitrebels.letra.message.response;

public class HRMReportDetails {

    private int annual;

    private int casual;

    private int nopay;

    private int sick;

    public HRMReportDetails() {
    }

    public HRMReportDetails(int annual, int casual, int nopay, int sick) {
        this.annual = annual;
        this.casual = casual;
        this.nopay = nopay;
        this.sick = sick;
    }

    public int getAnnual() {
        return annual;
    }

    public void setAnnual(int annual) {
        this.annual = annual;
    }

    public int getCasual() {
        return casual;
    }

    public void setCasual(int casual) {
        this.casual = casual;
    }

    public int getNopay() {
        return nopay;
    }

    public void setNopay(int nopay) {
        this.nopay = nopay;
    }

    public int getSick() {
        return sick;
    }

    public void setSick(int sick) {
        this.sick = sick;
    }
}
