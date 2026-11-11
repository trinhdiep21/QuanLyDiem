package dao;

import model.LopHocPhan;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LopHocPhanDAO {
    private Connection conn;

    public LopHocPhanDAO() {
        conn = DatabaseConnection.getConnection();
    }

    public List<LopHocPhan> getAll() {
        List<LopHocPhan> list = new ArrayList<>();
        String sql = "SELECT lhp.*, mh.tenMH, hk.tenHK FROM LopHocPhan lhp " +
                     "JOIN MonHoc mh ON lhp.maMH = mh.maMH " +
                     "JOIN HocKy hk ON lhp.maHK = hk.maHK ORDER BY lhp.maLHP";
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapResultSet(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public List<LopHocPhan> getByHocKy(String maHK) {
        List<LopHocPhan> list = new ArrayList<>();
        String sql = "SELECT lhp.*, mh.tenMH, hk.tenHK FROM LopHocPhan lhp " +
                     "JOIN MonHoc mh ON lhp.maMH = mh.maMH " +
                     "JOIN HocKy hk ON lhp.maHK = hk.maHK WHERE lhp.maHK=? ORDER BY lhp.maLHP";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maHK);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapResultSet(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public LopHocPhan getById(String maLHP) {
        String sql = "SELECT lhp.*, mh.tenMH, hk.tenHK FROM LopHocPhan lhp " +
                     "JOIN MonHoc mh ON lhp.maMH = mh.maMH " +
                     "JOIN HocKy hk ON lhp.maHK = hk.maHK WHERE lhp.maLHP=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maLHP);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapResultSet(rs);
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public boolean insert(LopHocPhan lhp) {
        String sql = "INSERT INTO LopHocPhan (maLHP, maMH, maHK, giaoVien, soTietLyThuyet, soTietThucHanh, tongSoBuoi, soPhepNghi) VALUES (?,?,?,?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, lhp.getMaLHP()); ps.setString(2, lhp.getMaMH());
            ps.setString(3, lhp.getMaHK()); ps.setString(4, lhp.getGiaoVien());
            ps.setInt(5, lhp.getSoTietLyThuyet()); ps.setInt(6, lhp.getSoTietThucHanh());
            ps.setInt(7, lhp.getTongSoBuoi()); ps.setInt(8, lhp.getSoPhepNghi());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean update(LopHocPhan lhp) {
        String sql = "UPDATE LopHocPhan SET maMH=?, maHK=?, giaoVien=?, soTietLyThuyet=?, soTietThucHanh=?, tongSoBuoi=?, soPhepNghi=? WHERE maLHP=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, lhp.getMaMH()); ps.setString(2, lhp.getMaHK());
            ps.setString(3, lhp.getGiaoVien()); ps.setInt(4, lhp.getSoTietLyThuyet());
            ps.setInt(5, lhp.getSoTietThucHanh()); ps.setInt(6, lhp.getTongSoBuoi());
            ps.setInt(7, lhp.getSoPhepNghi()); ps.setString(8, lhp.getMaLHP());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean delete(String maLHP) {
        String sql = "DELETE FROM LopHocPhan WHERE maLHP=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maLHP);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean dangKySinhVien(String maSV, String maLHP) {
        String sql = "INSERT IGNORE INTO DangKyHP (maSV, maLHP) VALUES (?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maSV); ps.setString(2, maLHP);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean huyDangKySinhVien(String maSV, String maLHP) {
        String sql = "DELETE FROM DangKyHP WHERE maSV=? AND maLHP=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maSV); ps.setString(2, maLHP);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }


    private LopHocPhan mapResultSet(ResultSet rs) throws SQLException {
        LopHocPhan lhp = new LopHocPhan();
        lhp.setMaLHP(rs.getString("maLHP"));
        lhp.setMaMH(rs.getString("maMH"));
        lhp.setTenMH(rs.getString("tenMH"));
        lhp.setMaHK(rs.getString("maHK"));
        lhp.setTenHK(rs.getString("tenHK"));
        lhp.setGiaoVien(rs.getString("giaoVien"));
        lhp.setSoTietLyThuyet(rs.getInt("soTietLyThuyet"));
        lhp.setSoTietThucHanh(rs.getInt("soTietThucHanh"));
        lhp.setTongSoBuoi(rs.getInt("tongSoBuoi"));
        lhp.setSoPhepNghi(rs.getInt("soPhepNghi"));
        return lhp;
    }
}
