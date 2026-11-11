package dao;

import model.DiemDanh;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DiemDanhDAO {
    private Connection conn;

    public DiemDanhDAO() {
        conn = DatabaseConnection.getConnection();
    }

    public List<DiemDanh> getByLHP(String maLHP) {
        List<DiemDanh> list = new ArrayList<>();
        String sql = "SELECT dd.*, sv.hoTen FROM DiemDanh dd JOIN SinhVien sv ON dd.maSV = sv.maSV WHERE dd.maLHP=? ORDER BY dd.ngayHoc, sv.maSV";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maLHP);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapResultSet(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public int countVang(String maSV, String maLHP) {
        String sql = "SELECT COUNT(*) FROM DiemDanh WHERE maSV=? AND maLHP=? AND trangThai='Vang'";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maSV); ps.setString(2, maLHP);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return 0;
    }

    public boolean insert(DiemDanh dd) {
        String sql = "INSERT INTO DiemDanh (maSV, maLHP, ngayHoc, buoiHoc, trangThai) VALUES (?,?,?,?,?) " +
                     "ON DUPLICATE KEY UPDATE trangThai=VALUES(trangThai)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, dd.getMaSV()); ps.setString(2, dd.getMaLHP());
            ps.setDate(3, new java.sql.Date(dd.getNgayHoc().getTime()));
            ps.setInt(4, dd.getBuoiHoc()); ps.setString(5, dd.getTrangThai());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    private DiemDanh mapResultSet(ResultSet rs) throws SQLException {
        DiemDanh dd = new DiemDanh();
        dd.setId(rs.getInt("id")); dd.setMaSV(rs.getString("maSV"));
        dd.setHoTen(rs.getString("hoTen")); dd.setMaLHP(rs.getString("maLHP"));
        dd.setNgayHoc(rs.getDate("ngayHoc")); dd.setBuoiHoc(rs.getInt("buoiHoc"));
        dd.setTrangThai(rs.getString("trangThai"));
        return dd;
    }
}
