package com.example.save_food.models;

public class UserLocation {
    private String uid;
    private double latitude;
    private double longitude;

    public UserLocation(String uid, double latitude, double longitude) {
        this.uid = uid;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getUid() {
        return uid;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }


}