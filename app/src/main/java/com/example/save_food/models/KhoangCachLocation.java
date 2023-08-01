package com.example.save_food.models;

public class KhoangCachLocation {
    private double distance;
    private String uid;

    public KhoangCachLocation(double distance, String uid) {
        this.distance = distance;
        this.uid = uid;
    }

    public double getDistance() {
        return distance;
    }

    public String getUid() {
        return uid;
    }
}
