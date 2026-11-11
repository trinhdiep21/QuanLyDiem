package service;

import dao.DiemDAO;
import dao.KhungCTDTDAO;
import model.DiemMonHoc;
import model.KhungCTDT;

import java.util.*;

/**
 * Service xét điều kiện tốt nghiệp tự động
 * Đối chiếu bảng điểm sinh viên với khung tín chỉ bắt buộc/tự chọn
 */
public class TotNghiepService {

    private DiemDAO diemDAO;
    private KhungCTDTDAO khungDAO;
    private GPAService gpaService;

    // Điều kiện tốt nghiệp
    private static final double GPA_MIN_TOT_NGHIEP = 2.0;
    private static final int MIN_TC_TU_CHON = 6;

    public TotNghiepService() {
        diemDAO = new DiemDAO();
        khungDAO = new KhungCTDTDAO();
        gpaService = new GPAService();
    }

    /**
     * Xét điều kiện tốt nghiệp
     */
    public KetQuaTotNghiep xetTotNghiep(String maSV, String maKhoa) {
        KetQuaTotNghiep kq = new KetQuaTotNghiep();
        kq.setMaSV(maSV);

        // Lấy khung CTĐT
        List<KhungCTDT> khungList = khungDAO.getByKhoa(maKhoa);
        // Lấy bảng điểm sinh viên
        List<DiemMonHoc> bangDiem = diemDAO.getDiemBangSinhVien(maSV);

        // Tập hợp môn đã qua của SV
        Set<String> monDaQua = new HashSet<>();
        for (DiemMonHoc d : bangDiem) {
            if ("Dat".equals(d.getKetQua())) {
                // Lấy mã môn từ LHP
                try {
                    dao.LopHocPhanDAO lhpDAO = new dao.LopHocPhanDAO();
                    var lhp = lhpDAO.getById(d.getMaLHP());
                    if (lhp != null) monDaQua.add(lhp.getMaMH());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        // Kiểm tra môn bắt buộc
        List<String> monBatBuocChua = new ArrayList<>();
        List<String> monTuChonDaQua = new ArrayList<>();
        int tcTuChonDaQua = 0;

        for (KhungCTDT kc : khungList) {
            if ("BatBuoc".equals(kc.getLoai())) {
                if (!monDaQua.contains(kc.getMaMH())) {
                    monBatBuocChua.add(kc.getTenMH() + " (" + kc.getMaMH() + ")");
                }
            } else if ("TuChon".equals(kc.getLoai())) {
                if (monDaQua.contains(kc.getMaMH())) {
                    tcTuChonDaQua += kc.getSoTinChi();
                    monTuChonDaQua.add(kc.getTenMH());
                }
            }
        }

        // GPA
        double gpa = gpaService.tinhGPA(maSV);
        int tongTinChi = gpaService.tongTinChiTichLuy(maSV);

        kq.setGpa(gpa);
        kq.setTongTinChiTichLuy(tongTinChi);
        kq.setMonBatBuocChuaHoc(monBatBuocChua);
        kq.setTcTuChonDaQua(tcTuChonDaQua);
        kq.setMonTuChonDaQua(monTuChonDaQua);

        // Điều kiện tốt nghiệp
        boolean duDieuKien = monBatBuocChua.isEmpty()
                && tcTuChonDaQua >= MIN_TC_TU_CHON
                && gpa >= GPA_MIN_TOT_NGHIEP;

        kq.setDuDieuKien(duDieuKien);

        // Xếp loại tốt nghiệp
        if (duDieuKien) {
            kq.setXepLoai(gpaService.xepLoaiHocLuc(gpa));
        }

        return kq;
    }

    /**
     * Kết quả xét tốt nghiệp
     */
    public static class KetQuaTotNghiep {
        private String maSV;
        private boolean duDieuKien;
        private double gpa;
        private int tongTinChiTichLuy;
        private List<String> monBatBuocChuaHoc;
        private int tcTuChonDaQua;
        private List<String> monTuChonDaQua;
        private String xepLoai;

        public String getMaSV() { return maSV; }
        public void setMaSV(String maSV) { this.maSV = maSV; }
        public boolean isDuDieuKien() { return duDieuKien; }
        public void setDuDieuKien(boolean duDieuKien) { this.duDieuKien = duDieuKien; }
        public double getGpa() { return gpa; }
        public void setGpa(double gpa) { this.gpa = gpa; }
        public int getTongTinChiTichLuy() { return tongTinChiTichLuy; }
        public void setTongTinChiTichLuy(int tongTinChiTichLuy) { this.tongTinChiTichLuy = tongTinChiTichLuy; }
        public List<String> getMonBatBuocChuaHoc() { return monBatBuocChuaHoc; }
        public void setMonBatBuocChuaHoc(List<String> monBatBuocChuaHoc) { this.monBatBuocChuaHoc = monBatBuocChuaHoc; }
        public int getTcTuChonDaQua() { return tcTuChonDaQua; }
        public void setTcTuChonDaQua(int tcTuChonDaQua) { this.tcTuChonDaQua = tcTuChonDaQua; }
        public List<String> getMonTuChonDaQua() { return monTuChonDaQua; }
        public void setMonTuChonDaQua(List<String> monTuChonDaQua) { this.monTuChonDaQua = monTuChonDaQua; }
        public String getXepLoai() { return xepLoai; }
        public void setXepLoai(String xepLoai) { this.xepLoai = xepLoai; }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("=== KẾT QUẢ XÉT TỐT NGHIỆP ===\n");
            sb.append("Mã SV: ").append(maSV).append("\n");
            sb.append("GPA: ").append(String.format("%.2f", gpa)).append("\n");
            sb.append("Tổng TC tích lũy: ").append(tongTinChiTichLuy).append("\n");
            sb.append("TC tự chọn đã qua: ").append(tcTuChonDaQua).append("\n");
            if (!monBatBuocChuaHoc.isEmpty()) {
                sb.append("Môn bắt buộc chưa học:\n");
                for (String m : monBatBuocChuaHoc) sb.append("  - ").append(m).append("\n");
            }
            sb.append("KẾT LUẬN: ").append(duDieuKien ? "✅ ĐỦ ĐIỀU KIỆN TỐT NGHIỆP" : "❌ CHƯA ĐỦ ĐIỀU KIỆN");
            if (duDieuKien) sb.append("\nXếp loại: ").append(xepLoai);
            return sb.toString();
        }
    }
}
