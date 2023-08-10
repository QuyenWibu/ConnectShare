package com.example.save_food.adapter;

public class ViewPagerItem {
    String ImgaeId, uid, pLike;
    String Heding, Heding2;

    public ViewPagerItem(String imgaeId, String heding, String heding2,String uid,String pLike) {
        ImgaeId = imgaeId;
        Heding = heding;
        Heding2 = heding2;
        this.uid = uid;
        this.pLike = pLike;
    }

    public String getHeding() {
        return Heding;
    }


    public String getHeding2() {
        return Heding2;
    }
    public String getpLike() {
        return pLike;
    }

    public String getImgaeId() {
        return ImgaeId;
    }
    public String getUid(){return uid;}
    public void setpLike(String pLike){this.pLike = pLike; }
}
