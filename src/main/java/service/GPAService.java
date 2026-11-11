package service;

import dao.DiemDAO;
import dao.KhungCTDTDAO;
import model.DiemMonHoc;
import model.KhungCTDT;

import java.util.*;

/**
 * Service tính GPA tích lũy toàn khóa (hệ 4) và xếp loại học lực
 */
public class GPAService {

    private DiemDAO diemDAO;
    private KhungCTDTDAO khungDAO;

    public GPAService() {
        diemDAO = new DiemDAO();
        khungDAO = new KhungCTDTDAO();
    }

    /**
     * Tính GPA hệ 4 tích lũy cho một sinh viên
     * GPA = Σ(điểm hệ 4 × tín chỉ) / Σ(tín chỉ)
     */
    public double tinhGPA(String maSV) {
        List<DiemMonHoc> danhSachDiem = diemDAO.getDiemBangSinhVien(maSV);

        double tongTichLuy = 0;
        int tongTinChi = 0;

        for (DiemMonHoc dmh : danhSachDiem) {
            if (!"CamThi".equals(dmh.getKetQua())) {
                // Lấy số tín chỉ môn học qua tên LHP
                int tinChi = getTinChiByLHP(dmh.getMaLHP());
                tongTichLuy += dmh.getDiemHe4() * tinChi;
                tongTinChi += tinChi;
            }
        }

        if (tongTinChi == 0) return 0.0;
        double gpa = tongTichLuy / tongTinChi;
        return Math.round(gpa * 100.0) / 100.0;
    }

    /**
     * Lấy số tín chỉ theo mã lớp học phần
     */
    private int getTinChiByLHP(String maLHP) {
        // Query join qua DAO
        try {
            dao.LopHocPhanDAO lhpDAO = new dao.LopHocPhanDAO();
            dao.MonHocDAO mhDAO = new dao.MonHocDAO();
            var lhp = lhpDAO.getById(maLHP);
            if (lhp != null) {
                var mh = mhDAO.getById(lhp.getMaMH());
                if (mh != null) return mh.getSoTinChi();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 3; // Mặc định 3 tín chỉ
    }

    /**
     * Xếp loại học lực dựa trên GPA hệ 4
     */
    public String xepLoaiHocLuc(double gpa) {
        if (gpa >= 3.6) return "Xuất sắc";
        if (gpa >= 3.2) return "Giỏi";
        if (gpa >= 2.5) return "Khá";
        if (gpa >= 2.0) return "Trung bình";
        if (gpa >= 1.0) return "Yếu";
        return "Kém";
    }

    /**
     * Xếp loại theo điểm trung bình hệ 10
     */
    public String xepLoaiHe10(double diemTB) {
        if (diemTB >= 9.0) return "Xuất sắc";
        if (diemTB >= 8.0) return "Giỏi";
        if (diemTB >= 6.5) return "Khá";
        if (diemTB >= 5.0) return "Trung bình";
        if (diemTB >= 4.0) return "Yếu";
        return "Kém";
    }

    /**
     * Tính GPA kỳ (chỉ trong một học kỳ)
     */
    public double tinhGPAKy(String maSV, String maHK) {
        // Lấy điểm trong kỳ học cụ thể
        List<DiemMonHoc> danhSachDiem = diemDAO.getDiemBangSinhVien(maSV);
        double tongTichLuy = 0;
        int tongTinChi = 0;

        for (DiemMonHoc dmh : danhSachDiem) {
            // Kiểm tra LHP thuộc HK này
            if (dmh.getMaLHP() != null && dmh.getMaLHP().contains(maHK.substring(0, 3))) {
                if (!"CamThi".equals(dmh.getKetQua())) {
                    int tinChi = getTinChiByLHP(dmh.getMaLHP());
                    tongTichLuy += dmh.getDiemHe4() * tinChi;
                    tongTinChi += tinChi;
                }
            }
        }

        if (tongTinChi == 0) return 0.0;
        return Math.round((tongTichLuy / tongTinChi) * 100.0) / 100.0;
    }

    /**
     * Tổng số tín chỉ đã tích lũy (điểm >= 4.0)
     */
    public int tongTinChiTichLuy(String maSV) {
        List<DiemMonHoc> danhSachDiem = diemDAO.getDiemBangSinhVien(maSV);
        int tong = 0;
        for (DiemMonHoc dmh : danhSachDiem) {
            if ("Dat".equals(dmh.getKetQua())) {
                tong += getTinChiByLHP(dmh.getMaLHP());
            }
        }
        return tong;
    }

    /**
     * Bảng tổng kết GPA của tất cả SV trong lớp
     * @return Map<maSV, GPA>
     */
    public Map<String, Double> tinhGPADanhSach(List<String> danhSachMaSV) {
        Map<String, Double> result = new LinkedHashMap<>();
        for (String maSV : danhSachMaSV) {
            result.put(maSV, tinhGPA(maSV));
        }
        return result;
    }
}
