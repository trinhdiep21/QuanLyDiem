package dao;

import model.MonHoc;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MonHocDAO {
    private Connection conn;

    public MonHocDAO() {
        conn = DatabaseConnection.getConnection();
    }

    public List<MonHoc> getAll() {
        List<MonHoc> list = new ArrayList<>();
        String sql = "SELECT * FROM MonHoc ORDER BY maMH";
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapResultSet(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public MonHoc getById(String maMH) {
        String sql = "SELECT * FROM MonHoc WHERE maMH=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maMH);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapResultSet(rs);
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public boolean insert(MonHoc mh) {
        String sql = "INSERT INTO MonHoc (maMH, tenMH, soTinChi, loai, moTa) VALUES (?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, mh.getMaMH());
            ps.setString(2, mh.getTenMH());
            ps.setInt(3, mh.getSoTinChi());
            ps.setString(4, mh.getLoai());
            ps.setString(5, mh.getMoTa());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean update(MonHoc mh) {
        String sql = "UPDATE MonHoc SET tenMH=?, soTinChi=?, loai=?, moTa=? WHERE maMH=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, mh.getTenMH());
            ps.setInt(2, mh.getSoTinChi());
            ps.setString(3, mh.getLoai());
            ps.setString(4, mh.getMoTa());
            ps.setString(5, mh.getMaMH());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean delete(String maMH) {
        String sql = "DELETE FROM MonHoc WHERE maMH=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maMH);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    private MonHoc mapResultSet(ResultSet rs) throws SQLException {
        return new MonHoc(
            rs.getString("maMH"), rs.getString("tenMH"),
            rs.getInt("soTinChi"), rs.getString("loai"), rs.getString("moTa")
        );
    }
}
