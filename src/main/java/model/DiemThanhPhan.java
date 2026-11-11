package model;

public class DiemThanhPhan {
    private int id;
    private String maSV;
    private int idCotDiem;
    private double diem;
    private String ghiChu;
    private boolean daKhoa;

    public DiemThanhPhan() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getMaSV() { return maSV; }
    public void setMaSV(String maSV) { this.maSV = maSV; }
    public int getIdCotDiem() { return idCotDiem; }
    public void setIdCotDiem(int idCotDiem) { this.idCotDiem = idCotDiem; }
    public double getDiem() { return diem; }
    public void setDiem(double diem) { this.diem = diem; }
    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }
    public boolean isDaKhoa() { return daKhoa; }
    public void setDaKhoa(boolean daKhoa) { this.daKhoa = daKhoa; }
}
