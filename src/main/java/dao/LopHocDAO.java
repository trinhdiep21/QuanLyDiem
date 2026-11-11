package dao;

import model.LopHoc;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LopHocDAO {
    private Connection conn;

    public LopHocDAO() {
        conn = DatabaseConnection.getConnection();
    }

    public List<LopHoc> getAll() {
        List<LopHoc> list = new ArrayList<>();
        String sql = "SELECT * FROM LopHoc ORDER BY maLop";
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapResultSet(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public List<LopHoc> getByKhoa(String maKhoa) {
        List<LopHoc> list = new ArrayList<>();
        String sql = "SELECT * FROM LopHoc WHERE maKhoa=? ORDER BY maLop";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maKhoa);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapResultSet(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public boolean insert(LopHoc lop) {
        String sql = "INSERT INTO LopHoc (maLop, tenLop, maKhoa, siSo) VALUES (?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, lop.getMaLop());
            ps.setString(2, lop.getTenLop());
            ps.setString(3, lop.getMaKhoa());
            ps.setInt(4, lop.getSiSo());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean update(LopHoc lop) {
        String sql = "UPDATE LopHoc SET tenLop=?, maKhoa=?, siSo=? WHERE maLop=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, lop.getTenLop());
            ps.setString(2, lop.getMaKhoa());
            ps.setInt(3, lop.getSiSo());
            ps.setString(4, lop.getMaLop());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean delete(String maLop) {
        String sql = "DELETE FROM LopHoc WHERE maLop=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maLop);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    private LopHoc mapResultSet(ResultSet rs) throws SQLException {
        return new LopHoc(rs.getString("maLop"), rs.getString("tenLop"), rs.getString("maKhoa"), rs.getInt("siSo"));
    }
}
