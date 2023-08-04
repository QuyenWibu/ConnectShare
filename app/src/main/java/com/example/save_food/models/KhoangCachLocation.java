package com.example.save_food.models;

import android.os.Parcel;
import android.os.Parcelable;

public class KhoangCachLocation implements Parcelable {
    private double distance;
    private String uid;

    public KhoangCachLocation(double distance, String uid) {
        this.distance = distance;
        this.uid = uid;
    }

    protected KhoangCachLocation(Parcel in) {
        distance = in.readDouble();
        uid = in.readString();
    }

    public static final Creator<KhoangCachLocation> CREATOR = new Creator<KhoangCachLocation>() {
        @Override
        public KhoangCachLocation createFromParcel(Parcel in) {
            return new KhoangCachLocation(in);
        }

        @Override
        public KhoangCachLocation[] newArray(int size) {
            return new KhoangCachLocation[size];
        }
    };

    public double getDistance() {
        return distance;
    }

    public String getUid() {
        return uid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(distance);
        dest.writeString(uid);
    }
}
