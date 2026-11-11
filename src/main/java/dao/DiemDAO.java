package dao;

import model.CotDiem;
import model.DiemThanhPhan;
import model.DiemMonHoc;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DiemDAO {
    private Connection conn;

    public DiemDAO() {
        conn = DatabaseConnection.getConnection();
    }

    // ===== Cột điểm =====
    public List<CotDiem> getCotDiemByLHP(String maLHP) {
        List<CotDiem> list = new ArrayList<>();
        String sql = "SELECT * FROM CotDiem WHERE maLHP=? ORDER BY id";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maLHP);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new CotDiem(rs.getInt("id"), rs.getString("maLHP"),
                    rs.getString("tenCot"), rs.getDouble("trongSo"), rs.getString("loai")));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public boolean insertCotDiem(CotDiem cd) {
        String sql = "INSERT INTO CotDiem (maLHP, tenCot, trongSo, loai) VALUES (?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cd.getMaLHP()); ps.setString(2, cd.getTenCot());
            ps.setDouble(3, cd.getTrongSo()); ps.setString(4, cd.getLoai());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean deleteCotDiem(int id) {
        String sql = "DELETE FROM CotDiem WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    // ===== Điểm thành phần =====
    public DiemThanhPhan getDiemThanhPhan(String maSV, int idCotDiem) {
        String sql = "SELECT * FROM DiemThanhPhan WHERE maSV=? AND idCotDiem=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maSV); ps.setInt(2, idCotDiem);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                DiemThanhPhan d = new DiemThanhPhan();
                d.setId(rs.getInt("id")); d.setMaSV(rs.getString("maSV"));
                d.setIdCotDiem(rs.getInt("idCotDiem"));
                d.setDiem(rs.getDouble("diem")); d.setGhiChu(rs.getString("ghiChu"));
                d.setDaKhoa(rs.getBoolean("daKhoa"));
                return d;
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public boolean upsertDiemThanhPhan(String maSV, int idCotDiem, double diem) {
        String sql = "INSERT INTO DiemThanhPhan (maSV, idCotDiem, diem) VALUES (?,?,?) " +
                     "ON DUPLICATE KEY UPDATE diem=VALUES(diem)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maSV); ps.setInt(2, idCotDiem); ps.setDouble(3, diem);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean khoaDiem(String maSV, int idCotDiem, boolean khoa) {
        String sql = "UPDATE DiemThanhPhan SET daKhoa=? WHERE maSV=? AND idCotDiem=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBoolean(1, khoa); ps.setString(2, maSV); ps.setInt(3, idCotDiem);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    // ===== Điểm môn học =====
    public List<DiemMonHoc> getDiemMonHocByLHP(String maLHP) {
        List<DiemMonHoc> list = new ArrayList<>();
        String sql = "SELECT dm.*, sv.hoTen, mh.tenMH FROM DiemMonHoc dm " +
                     "JOIN SinhVien sv ON dm.maSV = sv.maSV " +
                     "JOIN LopHocPhan lhp ON dm.maLHP = lhp.maLHP " +
                     "JOIN MonHoc mh ON lhp.maMH = mh.maMH " +
                     "WHERE dm.maLHP=? ORDER BY sv.maSV";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maLHP);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapDiemMonHoc(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public List<DiemMonHoc> getDiemBangSinhVien(String maSV) {
        List<DiemMonHoc> list = new ArrayList<>();
        String sql = "SELECT dm.*, sv.hoTen, mh.tenMH FROM DiemMonHoc dm " +
                     "JOIN SinhVien sv ON dm.maSV = sv.maSV " +
                     "JOIN LopHocPhan lhp ON dm.maLHP = lhp.maLHP " +
                     "JOIN MonHoc mh ON lhp.maMH = mh.maMH " +
                     "WHERE dm.maSV=? ORDER BY dm.maLHP";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maSV);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapDiemMonHoc(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public boolean upsertDiemMonHoc(DiemMonHoc dmh) {
        String sql = "INSERT INTO DiemMonHoc (maSV, maLHP, diemTrungBinh, diemHe4, diemChu, ketQua) VALUES (?,?,?,?,?,?) " +
                     "ON DUPLICATE KEY UPDATE diemTrungBinh=VALUES(diemTrungBinh), diemHe4=VALUES(diemHe4), " +
                     "diemChu=VALUES(diemChu), ketQua=VALUES(ketQua)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, dmh.getMaSV()); ps.setString(2, dmh.getMaLHP());
            ps.setDouble(3, dmh.getDiemTrungBinh()); ps.setDouble(4, dmh.getDiemHe4());
            ps.setString(5, dmh.getDiemChu()); ps.setString(6, dmh.getKetQua());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    private DiemMonHoc mapDiemMonHoc(ResultSet rs) throws SQLException {
        DiemMonHoc d = new DiemMonHoc();
        d.setId(rs.getInt("id")); d.setMaSV(rs.getString("maSV"));
        d.setHoTen(rs.getString("hoTen")); d.setMaLHP(rs.getString("maLHP"));
        d.setTenMH(rs.getString("tenMH")); d.setDiemTrungBinh(rs.getDouble("diemTrungBinh"));
        d.setDiemHe4(rs.getDouble("diemHe4")); d.setDiemChu(rs.getString("diemChu"));
        d.setKetQua(rs.getString("ketQua"));
        return d;
    }
}
