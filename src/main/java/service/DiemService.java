package service;

import dao.DiemDAO;
import dao.DiemDanhDAO;
import dao.LopHocPhanDAO;
import model.CotDiem;
import model.DiemMonHoc;
import model.DiemThanhPhan;

import java.util.List;

/**
 * Service xử lý tính điểm, quy đổi hệ 10 -> hệ 4 -> hệ chữ
 */
public class DiemService {

    private DiemDAO diemDAO;
    private DiemDanhDAO diemDanhDAO;
    private LopHocPhanDAO lhpDAO;

    public DiemService() {
        diemDAO = new DiemDAO();
        diemDanhDAO = new DiemDanhDAO();
        lhpDAO = new LopHocPhanDAO();
    }

    /**
     * Tính điểm trung bình môn học theo trọng số phần trăm
     * Trả về điểm hệ 10
     */
    public double tinhDiemTrungBinh(String maSV, String maLHP) {
        List<CotDiem> cotDiems = diemDAO.getCotDiemByLHP(maLHP);
        double tongDiem = 0;
        double tongTrongSo = 0;

        for (CotDiem cd : cotDiems) {
            DiemThanhPhan dtp = diemDAO.getDiemThanhPhan(maSV, cd.getId());
            if (dtp != null && dtp.getDiem() >= 0) {
                tongDiem += dtp.getDiem() * cd.getTrongSo();
                tongTrongSo += cd.getTrongSo();
            }
        }

        if (tongTrongSo == 0) return 0;
        double diemTB = tongDiem / tongTrongSo;
        // Làm tròn 2 chữ số thập phân
        return Math.round(diemTB * 100.0) / 100.0;
    }

    /**
     * Quy đổi điểm hệ 10 sang hệ 4
     */
    public double quyDoiHe4(double diem10) {
        if (diem10 >= 9.0) return 4.0;
        if (diem10 >= 8.5) return 3.7;
        if (diem10 >= 8.0) return 3.5;
        if (diem10 >= 7.5) return 3.0;  // B+
        if (diem10 >= 7.0) return 2.5;  // B
        if (diem10 >= 6.5) return 2.0;  // C+
        if (diem10 >= 5.5) return 1.5;  // C (Đạt)
        if (diem10 >= 5.0) return 1.0;  // D+
        if (diem10 >= 4.0) return 0.5;  // D (Đạt yếu)
        return 0.0;                      // F
    }

    /**
     * Quy đổi điểm hệ 10 sang điểm chữ
     */
    public String quyDoiDiemChu(double diem10) {
        if (diem10 >= 9.0) return "A+";
        if (diem10 >= 8.5) return "A";
        if (diem10 >= 8.0) return "B+";
        if (diem10 >= 7.5) return "B";
        if (diem10 >= 7.0) return "C+";
        if (diem10 >= 6.5) return "C";
        if (diem10 >= 5.5) return "D+";
        if (diem10 >= 5.0) return "D";
        if (diem10 >= 4.0) return "D-";
        return "F";
    }

    /**
     * Xác định kết quả (Đạt/Không đạt) theo điểm hệ 10
     */
    public String xacDinhKetQua(double diem10, boolean camThi) {
        if (camThi) return "CamThi";
        return diem10 >= 4.0 ? "Dat" : "KhongDat";
    }

    /**
     * Tính và lưu điểm môn học cho một sinh viên trong lớp học phần
     */
    public boolean tinhVaLuuDiemMonHoc(String maSV, String maLHP) {
        // Kiểm tra cấm thi
        int soVang = diemDanhDAO.countVang(maSV, maLHP);
        var lhp = lhpDAO.getById(maLHP);
        boolean camThi = false;
        if (lhp != null && lhp.getTongSoBuoi() > 0) {
            int maxVangChoPhep = lhp.getTongSoBuoi() / 5; // 20% số buổi
            camThi = soVang > maxVangChoPhep;
        }

        double diemTB = camThi ? 0 : tinhDiemTrungBinh(maSV, maLHP);
        double diemHe4 = quyDoiHe4(diemTB);
        String diemChu = camThi ? "F" : quyDoiDiemChu(diemTB);
        String ketQua = xacDinhKetQua(diemTB, camThi);

        DiemMonHoc dmh = new DiemMonHoc();
        dmh.setMaSV(maSV);
        dmh.setMaLHP(maLHP);
        dmh.setDiemTrungBinh(diemTB);
        dmh.setDiemHe4(diemHe4);
        dmh.setDiemChu(diemChu);
        dmh.setKetQua(ketQua);

        return diemDAO.upsertDiemMonHoc(dmh);
    }

    /**
     * Tính điểm cho tất cả SV trong lớp học phần
     */
    public int tinhDiemToLHP(String maLHP, List<String> danhSachMaSV) {
        int count = 0;
        for (String maSV : danhSachMaSV) {
            if (tinhVaLuuDiemMonHoc(maSV, maLHP)) count++;
        }
        return count;
    }
}
