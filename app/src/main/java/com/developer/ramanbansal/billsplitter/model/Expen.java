package com.developer.ramanbansal.billsplitter.model;

public class Expen {
    public static final String TAG = "Expen";

    private long expenId;
    private String expenName;

    public Expen() {
    }

    public Expen(String expenName) {
        this.expenName = expenName;
    }

    public long getId() {
        return expenId;
    }

    public void setId(long mId) {
        this.expenId = mId;
    }

    public String getName() {
        return expenName;
    }

    public void setName(String mFirstName) {
        this.expenName = mFirstName;
    }

}
