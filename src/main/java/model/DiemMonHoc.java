package model;

public class DiemMonHoc {
    private int id;
    private String maSV;
    private String hoTen;
    private String maLHP;
    private String tenMH;
    private double diemTrungBinh;
    private double diemHe4;
    private String diemChu;
    private String ketQua; // Dat, KhongDat, CamThi

    public DiemMonHoc() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getMaSV() { return maSV; }
    public void setMaSV(String maSV) { this.maSV = maSV; }
    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }
    public String getMaLHP() { return maLHP; }
    public void setMaLHP(String maLHP) { this.maLHP = maLHP; }
    public String getTenMH() { return tenMH; }
    public void setTenMH(String tenMH) { this.tenMH = tenMH; }
    public double getDiemTrungBinh() { return diemTrungBinh; }
    public void setDiemTrungBinh(double diemTrungBinh) { this.diemTrungBinh = diemTrungBinh; }
    public double getDiemHe4() { return diemHe4; }
    public void setDiemHe4(double diemHe4) { this.diemHe4 = diemHe4; }
    public String getDiemChu() { return diemChu; }
    public void setDiemChu(String diemChu) { this.diemChu = diemChu; }
    public String getKetQua() { return ketQua; }
    public void setKetQua(String ketQua) { this.ketQua = ketQua; }
}
