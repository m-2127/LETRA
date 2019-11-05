package com.bitrebels.letra.message.response;

public class ReturnDetails {

    long rmId;

    String name;

    public ReturnDetails(long rmId, String name) {
        this.rmId = rmId;
        this.name = name;
    }

    public long getRmId() {
        return rmId;
    }

    public void setRmId(long rmId) {
        this.rmId = rmId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
