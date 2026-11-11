package model;

import java.util.Date;

public class HocKy {
    private String maHK;
    private String tenHK;
    private String namHoc;
    private int kyHoc;
    private Date ngayBatDau;
    private Date ngayKetThuc;
    private String trangThai; // DangHoc, DaDong

    public HocKy() {}

    public HocKy(String maHK, String tenHK, String namHoc, int kyHoc,
                 Date ngayBatDau, Date ngayKetThuc, String trangThai) {
        this.maHK = maHK;
        this.tenHK = tenHK;
        this.namHoc = namHoc;
        this.kyHoc = kyHoc;
        this.ngayBatDau = ngayBatDau;
        this.ngayKetThuc = ngayKetThuc;
        this.trangThai = trangThai;
    }

    public String getMaHK() { return maHK; }
    public void setMaHK(String maHK) { this.maHK = maHK; }
    public String getTenHK() { return tenHK; }
    public void setTenHK(String tenHK) { this.tenHK = tenHK; }
    public String getNamHoc() { return namHoc; }
    public void setNamHoc(String namHoc) { this.namHoc = namHoc; }
    public int getKyHoc() { return kyHoc; }
    public void setKyHoc(int kyHoc) { this.kyHoc = kyHoc; }
    public Date getNgayBatDau() { return ngayBatDau; }
    public void setNgayBatDau(Date ngayBatDau) { this.ngayBatDau = ngayBatDau; }
    public Date getNgayKetThuc() { return ngayKetThuc; }
    public void setNgayKetThuc(Date ngayKetThuc) { this.ngayKetThuc = ngayKetThuc; }
    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    @Override
    public String toString() { return tenHK; }
}
