package dao;

import model.SinhVien;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SinhVienDAO {
    private Connection conn;

    public SinhVienDAO() {
        conn = DatabaseConnection.getConnection();
    }

    public List<SinhVien> getAllSinhVien() {
        List<SinhVien> list = new ArrayList<>();
        String sql = "SELECT sv.*, lh.tenLop FROM SinhVien sv LEFT JOIN LopHoc lh ON sv.maLop = lh.maLop ORDER BY sv.maSV";
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                SinhVien sv = mapResultSet(rs);
                sv.setTenLop(rs.getString("tenLop"));
                list.add(sv);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<SinhVien> searchSinhVien(String keyword, String maLop, String maKhoa) {
        List<SinhVien> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
            "SELECT sv.*, lh.tenLop FROM SinhVien sv LEFT JOIN LopHoc lh ON sv.maLop = lh.maLop WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append(" AND (sv.maSV LIKE ? OR sv.hoTen LIKE ?)");
            params.add("%" + keyword + "%");
            params.add("%" + keyword + "%");
        }
        if (maLop != null && !maLop.isEmpty()) {
            sql.append(" AND sv.maLop = ?");
            params.add(maLop);
        }
        if (maKhoa != null && !maKhoa.isEmpty()) {
            sql.append(" AND lh.maKhoa = ?");
            params.add(maKhoa);
        }
        sql.append(" ORDER BY sv.maSV");

        try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                SinhVien sv = mapResultSet(rs);
                sv.setTenLop(rs.getString("tenLop"));
                list.add(sv);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<SinhVien> getSinhVienByLHP(String maLHP) {
        List<SinhVien> list = new ArrayList<>();
        String sql = "SELECT sv.*, lh.tenLop FROM SinhVien sv " +
                     "JOIN DangKyHP dk ON sv.maSV = dk.maSV " +
                     "LEFT JOIN LopHoc lh ON sv.maLop = lh.maLop " +
                     "WHERE dk.maLHP = ? ORDER BY sv.maSV";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maLHP);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                SinhVien sv = mapResultSet(rs);
                sv.setTenLop(rs.getString("tenLop"));
                list.add(sv);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public SinhVien getSinhVienById(String maSV) {
        String sql = "SELECT sv.*, lh.tenLop FROM SinhVien sv LEFT JOIN LopHoc lh ON sv.maLop = lh.maLop WHERE sv.maSV = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maSV);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                SinhVien sv = mapResultSet(rs);
                sv.setTenLop(rs.getString("tenLop"));
                return sv;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean insert(SinhVien sv) {
        String sql = "INSERT INTO SinhVien (maSV, hoTen, ngaySinh, gioiTinh, diaChi, email, soDienThoai, maLop, trangThai) VALUES (?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, sv.getMaSV());
            ps.setString(2, sv.getHoTen());
            ps.setDate(3, sv.getNgaySinh() != null ? new java.sql.Date(sv.getNgaySinh().getTime()) : null);
            ps.setString(4, sv.getGioiTinh());
            ps.setString(5, sv.getDiaChi());
            ps.setString(6, sv.getEmail());
            ps.setString(7, sv.getSoDienThoai());
            ps.setString(8, sv.getMaLop());
            ps.setString(9, sv.getTrangThai());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(SinhVien sv) {
        String sql = "UPDATE SinhVien SET hoTen=?, ngaySinh=?, gioiTinh=?, diaChi=?, email=?, soDienThoai=?, maLop=?, trangThai=? WHERE maSV=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, sv.getHoTen());
            ps.setDate(2, sv.getNgaySinh() != null ? new java.sql.Date(sv.getNgaySinh().getTime()) : null);
            ps.setString(3, sv.getGioiTinh());
            ps.setString(4, sv.getDiaChi());
            ps.setString(5, sv.getEmail());
            ps.setString(6, sv.getSoDienThoai());
            ps.setString(7, sv.getMaLop());
            ps.setString(8, sv.getTrangThai());
            ps.setString(9, sv.getMaSV());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(String maSV) {
        String sql = "DELETE FROM SinhVien WHERE maSV=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maSV);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private SinhVien mapResultSet(ResultSet rs) throws SQLException {
        SinhVien sv = new SinhVien();
        sv.setMaSV(rs.getString("maSV"));
        sv.setHoTen(rs.getString("hoTen"));
        sv.setNgaySinh(rs.getDate("ngaySinh"));
        sv.setGioiTinh(rs.getString("gioiTinh"));
        sv.setDiaChi(rs.getString("diaChi"));
        sv.setEmail(rs.getString("email"));
        sv.setSoDienThoai(rs.getString("soDienThoai"));
        sv.setMaLop(rs.getString("maLop"));
        sv.setTrangThai(rs.getString("trangThai"));
        return sv;
    }
}
