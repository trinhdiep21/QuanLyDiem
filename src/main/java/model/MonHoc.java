package model;

public class MonHoc {
    private String maMH;
    private String tenMH;
    private int soTinChi;
    private String loai; // BatBuoc, TuChon
    private String moTa;

    public MonHoc() {}

    public MonHoc(String maMH, String tenMH, int soTinChi, String loai, String moTa) {
        this.maMH = maMH;
        this.tenMH = tenMH;
        this.soTinChi = soTinChi;
        this.loai = loai;
        this.moTa = moTa;
    }

    public String getMaMH() { return maMH; }
    public void setMaMH(String maMH) { this.maMH = maMH; }
    public String getTenMH() { return tenMH; }
    public void setTenMH(String tenMH) { this.tenMH = tenMH; }
    public int getSoTinChi() { return soTinChi; }
    public void setSoTinChi(int soTinChi) { this.soTinChi = soTinChi; }
    public String getLoai() { return loai; }
    public void setLoai(String loai) { this.loai = loai; }
    public String getMoTa() { return moTa; }
    public void setMoTa(String moTa) { this.moTa = moTa; }

    @Override
    public String toString() { return maMH + " - " + tenMH; }
}
