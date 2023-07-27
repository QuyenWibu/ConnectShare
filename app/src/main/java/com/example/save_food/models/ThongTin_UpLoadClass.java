package com.example.save_food.models;

public class ThongTin_UpLoadClass{
    private String TenDonHang;
    private int Don_Gia;
    private String dia_chi;
    private String NganhHang;
    private String ThoiGianHetHan;

    // Constructor không đối số
    public ThongTin_UpLoadClass() {
        // Khởi tạo giá trị mặc định cho các thuộc tính nếu cần
    }

    // Constructor
    public ThongTin_UpLoadClass(String tenDonHang, Integer donGia, String diaChi, String nganhHang, String thoiGianHetHan) {
        this.TenDonHang = tenDonHang;
        this.Don_Gia = donGia;
        this.dia_chi = diaChi;
        this.NganhHang = nganhHang;
        this.ThoiGianHetHan = thoiGianHetHan;
    }

    // Getter và Setter cho TenDonHang
    public  String getTenDonHang() {
        return TenDonHang;
    }
    public void setTenDonHang(String tenDonHang) {
        this.TenDonHang = tenDonHang;
    }

    // Getter và Setter cho Don_Gia
    public Integer getDonGia() {
        return Don_Gia;
    }
    public void setDonGia(Integer donGia) {
        this.Don_Gia = donGia;
    }

    // Getter và Setter cho dia_chi
    public String getDiaChi() {
        return dia_chi;
    }
    public void setDiaChi(String diaChi) {
        this.dia_chi = diaChi;
    }

    // Getter và Setter cho NganhHang
    public String getNganhHang() {
        return NganhHang;
    }
    public void setNganhHang(String nganhHang) {
        this.NganhHang = nganhHang;
    }

    // Getter và Setter cho ThoiGianHetHan
    public String getThoiGianHetHan() {
        return ThoiGianHetHan;
    }
    public void setThoiGianHetHan(String thoiGianHetHan) {
        this.ThoiGianHetHan = thoiGianHetHan;
    }

    @Override
    public String toString() {
        return "ThongTin_UpLoadClass{" +
                "TenDonHang='" + TenDonHang + '\'' +
                ", Don_Gia=" + Don_Gia +
                ", dia_chi='" + dia_chi + '\'' +
                ", NganhHang='" + NganhHang + '\'' +
                ", ThoiGianHetHan='" + ThoiGianHetHan + '\'' +
                '}';
    }
}
