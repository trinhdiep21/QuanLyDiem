package model;

public class CotDiem {
    private int id;
    private String maLHP;
    private String tenCot;
    private double trongSo;
    private String loai; // ThanhPhan, CuoiKy

    public CotDiem() {}

    public CotDiem(int id, String maLHP, String tenCot, double trongSo, String loai) {
        this.id = id;
        this.maLHP = maLHP;
        this.tenCot = tenCot;
        this.trongSo = trongSo;
        this.loai = loai;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getMaLHP() { return maLHP; }
    public void setMaLHP(String maLHP) { this.maLHP = maLHP; }
    public String getTenCot() { return tenCot; }
    public void setTenCot(String tenCot) { this.tenCot = tenCot; }
    public double getTrongSo() { return trongSo; }
    public void setTrongSo(double trongSo) { this.trongSo = trongSo; }
    public String getLoai() { return loai; }
    public void setLoai(String loai) { this.loai = loai; }

    @Override
    public String toString() { return tenCot + " (" + trongSo + "%)"; }
}
