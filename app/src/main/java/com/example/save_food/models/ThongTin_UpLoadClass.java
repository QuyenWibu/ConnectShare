package com.example.save_food.models;

public class ThongTin_UpLoadClass{
    private String tenDonHang;
    private int donGia;
    private String diaChi;
    private String nganhHang;
    private String thoiGianHetHan;

    public ThongTin_UpLoadClass() {
    }

    public ThongTin_UpLoadClass(String tenDonHang, int donGia, String diaChi, String nganhHang, String thoiGianHetHan) {
        this.tenDonHang = tenDonHang;
        this.donGia = donGia;
        this.diaChi = diaChi;
        this.nganhHang = nganhHang;
        this.thoiGianHetHan = thoiGianHetHan;
    }

    public String getTenDonHang() {
        return tenDonHang;
    }

    public void setTenDonHang(String tenDonHang) {
        this.tenDonHang = tenDonHang;
    }

    public int getDonGia() {
        return donGia;
    }

    public void setDonGia(int donGia) {
        this.donGia = donGia;
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
                ", donGia=" + donGia +
                ", diaChi='" + diaChi + '\'' +
                ", nganhHang='" + nganhHang + '\'' +
                ", thoiGianHetHan='" + thoiGianHetHan + '\'' +
                '}';
    }
}
