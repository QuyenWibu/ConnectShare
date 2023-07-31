package com.example.save_food.models;

public class UserLocation {
    private String uid;
    private double latitude;
    private double longitude;
    private String image;

    public UserLocation(String uid, double latitude, double longitude) {
        this.uid = uid;
        this.latitude = latitude;
        this.longitude = longitude;
//        this.image = image;
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

//public String getImage(){
//        return image;
//}
}