package model;

import java.util.Date;

public class DiemDanh {
    private int id;
    private String maSV;
    private String hoTen;
    private String maLHP;
    private Date ngayHoc;
    private int buoiHoc;
    private String trangThai; // CoMat, Vang, VangPhep

    public DiemDanh() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getMaSV() { return maSV; }
    public void setMaSV(String maSV) { this.maSV = maSV; }
    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }
    public String getMaLHP() { return maLHP; }
    public void setMaLHP(String maLHP) { this.maLHP = maLHP; }
    public Date getNgayHoc() { return ngayHoc; }
    public void setNgayHoc(Date ngayHoc) { this.ngayHoc = ngayHoc; }
    public int getBuoiHoc() { return buoiHoc; }
    public void setBuoiHoc(int buoiHoc) { this.buoiHoc = buoiHoc; }
    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
}
