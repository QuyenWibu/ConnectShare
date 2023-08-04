package com.example.save_food.models;

public class HinhAnh_Upload {
    public String linkHinh;

    public HinhAnh_Upload(String linkHinh) {
        this.linkHinh = linkHinh;
    }

    public HinhAnh_Upload() {
    }

    public String getLinkHinh() {
        return linkHinh;
    }

    public void setLinkHinh(String linkHinh) {
        this.linkHinh = linkHinh;
    }

    @Override
    public String toString() {
        return "HinhAnh_Upload{" +
                "linkHinh='" + linkHinh + '\'' +
                '}';
    }
}
