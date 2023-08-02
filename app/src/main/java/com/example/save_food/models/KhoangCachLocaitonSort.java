package com.example.save_food.models;

public class KhoangCachLocaitonSort {
    private double distance;
    private String uid;

    public KhoangCachLocaitonSort(double distance, String uid) {
        this.distance = distance;
        this.uid = uid;
    }

    public double getDistanceSort() {
        return distance;
    }

    public String getUidSort() {
        return uid;
    }
}
