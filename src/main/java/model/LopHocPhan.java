package model;

public class LopHocPhan {
    private String maLHP;
    private String maMH;
    private String tenMH;
    private String maHK;
    private String tenHK;
    private String giaoVien;
    private int soTietLyThuyet;
    private int soTietThucHanh;
    private int tongSoBuoi;
    private int soPhepNghi;

    public LopHocPhan() {}

    public String getMaLHP() { return maLHP; }
    public void setMaLHP(String maLHP) { this.maLHP = maLHP; }
    public String getMaMH() { return maMH; }
    public void setMaMH(String maMH) { this.maMH = maMH; }
    public String getTenMH() { return tenMH; }
    public void setTenMH(String tenMH) { this.tenMH = tenMH; }
    public String getMaHK() { return maHK; }
    public void setMaHK(String maHK) { this.maHK = maHK; }
    public String getTenHK() { return tenHK; }
    public void setTenHK(String tenHK) { this.tenHK = tenHK; }
    public String getGiaoVien() { return giaoVien; }
    public void setGiaoVien(String giaoVien) { this.giaoVien = giaoVien; }
    public int getSoTietLyThuyet() { return soTietLyThuyet; }
    public void setSoTietLyThuyet(int soTietLyThuyet) { this.soTietLyThuyet = soTietLyThuyet; }
    public int getSoTietThucHanh() { return soTietThucHanh; }
    public void setSoTietThucHanh(int soTietThucHanh) { this.soTietThucHanh = soTietThucHanh; }
    public int getTongSoBuoi() { return tongSoBuoi; }
    public void setTongSoBuoi(int tongSoBuoi) { this.tongSoBuoi = tongSoBuoi; }
    public int getSoPhepNghi() { return soPhepNghi; }
    public void setSoPhepNghi(int soPhepNghi) { this.soPhepNghi = soPhepNghi; }

    @Override
    public String toString() { return maLHP + " - " + tenMH; }
}
