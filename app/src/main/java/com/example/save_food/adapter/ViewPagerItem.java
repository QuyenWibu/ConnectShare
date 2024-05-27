package com.example.save_food.adapter;

import java.util.Objects;

public class ViewPagerItem {
    String ImgaeId, uid, pLikes;
    String Heding, Heding2;

    public ViewPagerItem(String imgaeId, String heding, String heding2, String uid) {
        ImgaeId = imgaeId;
        Heding = heding;
        Heding2 = heding2;
        this.uid = uid;
    }

    public String getHeding() {
        return Heding;
    }

    public String getHeding2() {
        return Heding2;
    }

    public String getImgaeId() {
        return ImgaeId;
    }

    public String getUid() {
        return uid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ViewPagerItem that = (ViewPagerItem) o;
        return Objects.equals(uid, that.uid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uid);
    }
}
