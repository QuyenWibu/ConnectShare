package com.example.save_food.adapter;

public class ViewPagerItem {
    String ImgaeId;
    String Heding, Heding2;

    public ViewPagerItem(String imgaeId, String heding, String heding2) {
        ImgaeId = imgaeId;
        Heding = heding;
        Heding2 = heding2;
    }

    public String getImgaeId() {
        return ImgaeId;
    }
}
