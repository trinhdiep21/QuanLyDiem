package model;

public class KhungCTDT {
    private int id;
    private String maKhoa;
    private String tenKhoa;
    private String maMH;
    private String tenMH;
    private int soTinChi;
    private String loai; // BatBuoc, TuChon

    public KhungCTDT() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getMaKhoa() { return maKhoa; }
    public void setMaKhoa(String maKhoa) { this.maKhoa = maKhoa; }
    public String getTenKhoa() { return tenKhoa; }
    public void setTenKhoa(String tenKhoa) { this.tenKhoa = tenKhoa; }
    public String getMaMH() { return maMH; }
    public void setMaMH(String maMH) { this.maMH = maMH; }
    public String getTenMH() { return tenMH; }
    public void setTenMH(String tenMH) { this.tenMH = tenMH; }
    public int getSoTinChi() { return soTinChi; }
    public void setSoTinChi(int soTinChi) { this.soTinChi = soTinChi; }
    public String getLoai() { return loai; }
    public void setLoai(String loai) { this.loai = loai; }
}
