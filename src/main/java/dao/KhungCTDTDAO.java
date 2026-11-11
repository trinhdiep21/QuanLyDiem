package dao;

import model.KhungCTDT;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class KhungCTDTDAO {
    private Connection conn;

    public KhungCTDTDAO() {
        conn = DatabaseConnection.getConnection();
    }

    public List<KhungCTDT> getByKhoa(String maKhoa) {
        List<KhungCTDT> list = new ArrayList<>();
        String sql = "SELECT k.*, kh.tenKhoa, mh.tenMH, mh.soTinChi FROM KhungCTDT k " +
                     "JOIN KhoaHoc kh ON k.maKhoa = kh.maKhoa " +
                     "JOIN MonHoc mh ON k.maMH = mh.maMH WHERE k.maKhoa=? ORDER BY k.loai, mh.maMH";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maKhoa);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                KhungCTDT kc = new KhungCTDT();
                kc.setId(rs.getInt("id"));
                kc.setMaKhoa(rs.getString("maKhoa")); kc.setTenKhoa(rs.getString("tenKhoa"));
                kc.setMaMH(rs.getString("maMH")); kc.setTenMH(rs.getString("tenMH"));
                kc.setSoTinChi(rs.getInt("soTinChi")); kc.setLoai(rs.getString("loai"));
                list.add(kc);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public boolean insert(String maKhoa, String maMH, String loai) {
        String sql = "INSERT IGNORE INTO KhungCTDT (maKhoa, maMH, loai) VALUES (?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maKhoa); ps.setString(2, maMH); ps.setString(3, loai);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM KhungCTDT WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }
}
