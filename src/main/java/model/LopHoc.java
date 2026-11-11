package model;

public class LopHoc {
    private String maLop;
    private String tenLop;
    private String maKhoa;
    private int siSo;

    public LopHoc() {}

    public LopHoc(String maLop, String tenLop, String maKhoa, int siSo) {
        this.maLop = maLop;
        this.tenLop = tenLop;
        this.maKhoa = maKhoa;
        this.siSo = siSo;
    }

    public String getMaLop() { return maLop; }
    public void setMaLop(String maLop) { this.maLop = maLop; }
    public String getTenLop() { return tenLop; }
    public void setTenLop(String tenLop) { this.tenLop = tenLop; }
    public String getMaKhoa() { return maKhoa; }
    public void setMaKhoa(String maKhoa) { this.maKhoa = maKhoa; }
    public int getSiSo() { return siSo; }
    public void setSiSo(int siSo) { this.siSo = siSo; }

    @Override
    public String toString() { return tenLop; }
}
