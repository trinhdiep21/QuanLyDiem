package service;

import dao.DiemDanhDAO;
import dao.LopHocPhanDAO;
import dao.SinhVienDAO;
import model.DiemDanh;
import model.LopHocPhan;
import model.SinhVien;

import java.util.*;

/**
 * Service quản lý điểm danh và cấm thi tự động
 */
public class DiemDanhService {

    private DiemDanhDAO diemDanhDAO;
    private LopHocPhanDAO lhpDAO;
    private SinhVienDAO svDAO;

    public DiemDanhService() {
        diemDanhDAO = new DiemDanhDAO();
        lhpDAO = new LopHocPhanDAO();
        svDAO = new SinhVienDAO();
    }

    /**
     * Kiểm tra xem sinh viên có bị cấm thi không
     * Quy định: Vắng > (1/5) tổng số buổi -> CẤM THI
     */
    public boolean kiemTraCamThi(String maSV, String maLHP) {
        LopHocPhan lhp = lhpDAO.getById(maLHP);
        if (lhp == null) return false;

        int tongSoBuoi = lhp.getTongSoBuoi();
        if (tongSoBuoi == 0) return false;

        int soVang = diemDanhDAO.countVang(maSV, maLHP);
        int gioiHanVang = tongSoBuoi / 5; // 20% = 1/5

        return soVang > gioiHanVang;
    }

    /**
     * Đếm số buổi vắng (không phép) của sinh viên
     */
    public int demSoVang(String maSV, String maLHP) {
        return diemDanhDAO.countVang(maSV, maLHP);
    }

    /**
     * Lấy danh sách sinh viên bị cấm thi trong lớp học phần
     */
    public List<SinhVien> getDanhSachCamThi(String maLHP) {
        LopHocPhan lhp = lhpDAO.getById(maLHP);
        List<SinhVien> camThiList = new ArrayList<>();

        if (lhp == null || lhp.getTongSoBuoi() == 0) return camThiList;

        List<SinhVien> svList = svDAO.getSinhVienByLHP(maLHP);
        int gioiHanVang = lhp.getTongSoBuoi() / 5;

        for (SinhVien sv : svList) {
            int soVang = diemDanhDAO.countVang(sv.getMaSV(), maLHP);
            if (soVang > gioiHanVang) {
                camThiList.add(sv);
            }
        }
        return camThiList;
    }

    /**
     * Tổng hợp điểm danh của lớp học phần
     * @return Map<maSV, soVang>
     */
    public Map<String, Integer> thongKeDiemDanh(String maLHP) {
        Map<String, Integer> result = new LinkedHashMap<>();
        List<SinhVien> svList = svDAO.getSinhVienByLHP(maLHP);
        for (SinhVien sv : svList) {
            int soVang = diemDanhDAO.countVang(sv.getMaSV(), maLHP);
            result.put(sv.getMaSV(), soVang);
        }
        return result;
    }

    /**
     * Ghi điểm danh hàng loạt cho một buổi học
     */
    public int ghiDiemDanhBuoi(String maLHP, Date ngayHoc, int buoiHoc,
                                 Map<String, String> trangThaiMap) {
        int count = 0;
        for (Map.Entry<String, String> entry : trangThaiMap.entrySet()) {
            DiemDanh dd = new DiemDanh();
            dd.setMaSV(entry.getKey());
            dd.setMaLHP(maLHP);
            dd.setNgayHoc(ngayHoc);
            dd.setBuoiHoc(buoiHoc);
            dd.setTrangThai(entry.getValue());
            if (diemDanhDAO.insert(dd)) count++;
        }
        return count;
    }
}
