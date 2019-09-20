package com.bitrebels.letra.message.response;

public class LeaveValidation {
    private int annual;

    private int casual;

    private int sick;

    private String gender;

    public LeaveValidation(int annual, int casual, int sick, String gender) {
        this.annual = annual;
        this.casual = casual;
        this.sick = sick;
        this.gender = gender;
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

    public int getSick() {
        return sick;
    }

    public void setSick(int sick) {
        this.sick = sick;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
