package dao;

import model.KhoaHoc;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class KhoaHocDAO {
    private Connection conn;

    public KhoaHocDAO() {
        conn = DatabaseConnection.getConnection();
    }

    public List<KhoaHoc> getAll() {
        List<KhoaHoc> list = new ArrayList<>();
        String sql = "SELECT * FROM KhoaHoc ORDER BY namBatDau DESC";
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new KhoaHoc(rs.getString("maKhoa"), rs.getString("tenKhoa"),
                    rs.getInt("namBatDau"), rs.getInt("namKetThuc"), rs.getString("moTa")));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public boolean insert(KhoaHoc k) {
        String sql = "INSERT INTO KhoaHoc VALUES (?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, k.getMaKhoa()); ps.setString(2, k.getTenKhoa());
            ps.setInt(3, k.getNamBatDau()); ps.setInt(4, k.getNamKetThuc());
            ps.setString(5, k.getMoTa());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean update(KhoaHoc k) {
        String sql = "UPDATE KhoaHoc SET tenKhoa=?, namBatDau=?, namKetThuc=?, moTa=? WHERE maKhoa=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, k.getTenKhoa()); ps.setInt(2, k.getNamBatDau());
            ps.setInt(3, k.getNamKetThuc()); ps.setString(4, k.getMoTa());
            ps.setString(5, k.getMaKhoa());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean delete(String maKhoa) {
        String sql = "DELETE FROM KhoaHoc WHERE maKhoa=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maKhoa);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }
}
