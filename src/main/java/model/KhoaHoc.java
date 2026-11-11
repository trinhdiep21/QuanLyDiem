package model;

public class KhoaHoc {
    private String maKhoa;
    private String tenKhoa;
    private int namBatDau;
    private int namKetThuc;
    private String moTa;

    public KhoaHoc() {}

    public KhoaHoc(String maKhoa, String tenKhoa, int namBatDau, int namKetThuc, String moTa) {
        this.maKhoa = maKhoa;
        this.tenKhoa = tenKhoa;
        this.namBatDau = namBatDau;
        this.namKetThuc = namKetThuc;
        this.moTa = moTa;
    }

    public String getMaKhoa() { return maKhoa; }
    public void setMaKhoa(String maKhoa) { this.maKhoa = maKhoa; }
    public String getTenKhoa() { return tenKhoa; }
    public void setTenKhoa(String tenKhoa) { this.tenKhoa = tenKhoa; }
    public int getNamBatDau() { return namBatDau; }
    public void setNamBatDau(int namBatDau) { this.namBatDau = namBatDau; }
    public int getNamKetThuc() { return namKetThuc; }
    public void setNamKetThuc(int namKetThuc) { this.namKetThuc = namKetThuc; }
    public String getMoTa() { return moTa; }
    public void setMoTa(String moTa) { this.moTa = moTa; }

    @Override
    public String toString() { return tenKhoa; }
}
