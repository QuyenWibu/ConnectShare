package com.example.save_food.models;

public class ThongTin_UpLoadClass {
    private String tenDonHang;
    private String diaChi;
    private String nganhHang;
    private String thoiGianHetHan;
    private String donViHetHan;


    public ThongTin_UpLoadClass() {
    }

    public ThongTin_UpLoadClass(String tenDonHang, String diaChi, String nganhHang, String thoiGianHetHan, String donViHetHan) {
        this.tenDonHang = tenDonHang;
        this.diaChi = diaChi;
        this.nganhHang = nganhHang;
        this.thoiGianHetHan = thoiGianHetHan;
        this.donViHetHan = donViHetHan;
    }

    public String getDonViHetHan() {
        return donViHetHan;
    }

    public void setDonViHetHan(String donViHetHan) {
        this.donViHetHan = donViHetHan;
    }

    public String getTenDonHang() {
        return tenDonHang;
    }

    public void setTenDonHang(String tenDonHang) {
        this.tenDonHang = tenDonHang;
    }




    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getNganhHang() {
        return nganhHang;
    }

    public void setNganhHang(String nganhHang) {
        this.nganhHang = nganhHang;
    }

    public String getThoiGianHetHan() {
        return thoiGianHetHan;
    }

    public void setThoiGianHetHan(String thoiGianHetHan) {
        this.thoiGianHetHan = thoiGianHetHan;
    }

    public String toStringg() {
        return "ThongTin_UpLoadClass{" +
                "tenDonHang='" + tenDonHang + '\'' +
                ", diaChi='" + diaChi + '\'' +
                ", nganhHang='" + nganhHang + '\'' +
                ", thoiGianHetHan='" + thoiGianHetHan + '\'' +
                ", donViHetHan='" + donViHetHan + '\'' +
                '}';
    }
}
