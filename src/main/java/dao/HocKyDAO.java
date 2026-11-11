package dao;

import model.HocKy;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HocKyDAO {
    private Connection conn;

    public HocKyDAO() {
        conn = DatabaseConnection.getConnection();
    }

    public List<HocKy> getAll() {
        List<HocKy> list = new ArrayList<>();
        String sql = "SELECT * FROM HocKy ORDER BY namHoc DESC, kyHoc";
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapResultSet(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public boolean insert(HocKy hk) {
        String sql = "INSERT INTO HocKy (maHK, tenHK, namHoc, kyHoc, ngayBatDau, ngayKetThuc, trangThai) VALUES (?,?,?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, hk.getMaHK()); ps.setString(2, hk.getTenHK());
            ps.setString(3, hk.getNamHoc()); ps.setInt(4, hk.getKyHoc());
            ps.setDate(5, hk.getNgayBatDau() != null ? new java.sql.Date(hk.getNgayBatDau().getTime()) : null);
            ps.setDate(6, hk.getNgayKetThuc() != null ? new java.sql.Date(hk.getNgayKetThuc().getTime()) : null);
            ps.setString(7, hk.getTrangThai());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean update(HocKy hk) {
        String sql = "UPDATE HocKy SET tenHK=?, namHoc=?, kyHoc=?, ngayBatDau=?, ngayKetThuc=?, trangThai=? WHERE maHK=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, hk.getTenHK()); ps.setString(2, hk.getNamHoc());
            ps.setInt(3, hk.getKyHoc());
            ps.setDate(4, hk.getNgayBatDau() != null ? new java.sql.Date(hk.getNgayBatDau().getTime()) : null);
            ps.setDate(5, hk.getNgayKetThuc() != null ? new java.sql.Date(hk.getNgayKetThuc().getTime()) : null);
            ps.setString(6, hk.getTrangThai()); ps.setString(7, hk.getMaHK());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean delete(String maHK) {
        String sql = "DELETE FROM HocKy WHERE maHK=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maHK);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    private HocKy mapResultSet(ResultSet rs) throws SQLException {
        HocKy hk = new HocKy();
        hk.setMaHK(rs.getString("maHK"));
        hk.setTenHK(rs.getString("tenHK"));
        hk.setNamHoc(rs.getString("namHoc"));
        hk.setKyHoc(rs.getInt("kyHoc"));
        hk.setNgayBatDau(rs.getDate("ngayBatDau"));
        hk.setNgayKetThuc(rs.getDate("ngayKetThuc"));
        hk.setTrangThai(rs.getString("trangThai"));
        return hk;
    }
}
