package view;

import dao.DiemDAO;
import dao.HocKyDAO;
import dao.LopHocPhanDAO;
import dao.MonHocDAO;
import dao.SinhVienDAO;
import model.CotDiem;
import model.HocKy;
import model.LopHocPhan;
import model.MonHoc;
import model.SinhVien;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class FormLopHocPhan extends JFrame {

    private JTextField txtMaLHP;
    private JComboBox<MonHoc> cboMonHoc;
    private JComboBox<HocKy> cboHocKy;
    private JTextField txtGiaoVien;
    private JSpinner spLT;
    private JSpinner spTH;
    private JSpinner spTongBuoi;
    private JSpinner spPhepNghi;

    private JButton btnThem;
    private JButton btnSua;
    private JButton btnXoa;
    private JButton btnLamMoi;
    private JButton btnCotDiem;
    private JButton btnDangKySV;

    private JTable tblLHP;
    private DefaultTableModel modelTable;

    private LopHocPhanDAO lhpDAO;
    private MonHocDAO mhDAO;
    private HocKyDAO hkDAO;
    private DiemDAO diemDAO;
    private SinhVienDAO svDAO;

    public FormLopHocPhan() {
        lhpDAO = new LopHocPhanDAO();
        mhDAO = new MonHocDAO();
        hkDAO = new HocKyDAO();
        diemDAO = new DiemDAO();
        svDAO = new SinhVienDAO();
        initComponents();
        loadCombos();
        fillTable(lhpDAO.getAll());
    }

    private void initComponents() {
        setTitle("Quản Lý Lớp Học Phần");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(950, 680);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(5, 5));

        JLabel lblTitle = new JLabel("QUẢN LÝ LỚP HỌC PHẦN & CẤU HÌNH ĐIỂM", JLabel.CENTER);
        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 20));
        lblTitle.setForeground(new Color(25, 118, 210));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(lblTitle, BorderLayout.NORTH);

        JPanel pnlCenter = new JPanel(new BorderLayout(5, 5));
        pnlCenter.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));

        JPanel pnlForm = new JPanel(new GridLayout(4, 4, 10, 10));
        pnlForm.setBorder(BorderFactory.createTitledBorder("Thông tin lớp học phần"));

        pnlForm.add(new JLabel("Mã LHP:"));
        txtMaLHP = new JTextField();
        pnlForm.add(txtMaLHP);

        pnlForm.add(new JLabel("Môn học:"));
        cboMonHoc = new JComboBox<>();
        pnlForm.add(cboMonHoc);

        pnlForm.add(new JLabel("Học kỳ:"));
        cboHocKy = new JComboBox<>();
        pnlForm.add(cboHocKy);

        pnlForm.add(new JLabel("Giáo viên giảng dạy:"));
        txtGiaoVien = new JTextField();
        pnlForm.add(txtGiaoVien);

        pnlForm.add(new JLabel("Số tiết lý thuyết:"));
        spLT = new JSpinner(new SpinnerNumberModel(30, 0, 150, 5));
        pnlForm.add(spLT);

        pnlForm.add(new JLabel("Số tiết thực hành:"));
        spTH = new JSpinner(new SpinnerNumberModel(15, 0, 150, 5));
        pnlForm.add(spTH);

        pnlForm.add(new JLabel("Tổng số buổi học:"));
        spTongBuoi = new JSpinner(new SpinnerNumberModel(15, 1, 100, 1));
        pnlForm.add(spTongBuoi);

        pnlForm.add(new JLabel("Số buổi phép nghỉ:"));
        spPhepNghi = new JSpinner(new SpinnerNumberModel(3, 0, 20, 1));
        pnlForm.add(spPhepNghi);

        pnlCenter.add(pnlForm, BorderLayout.NORTH);

        JPanel pnlActions = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnThem = new JButton("Thêm");
        btnThem.setBackground(new Color(46, 125, 50));
        btnThem.setForeground(Color.WHITE);
        btnThem.addActionListener(e -> btnThemActionPerformed());

        btnSua = new JButton("Sửa");
        btnSua.setBackground(new Color(255, 152, 0));
        btnSua.setForeground(Color.WHITE);
        btnSua.addActionListener(e -> btnSuaActionPerformed());

        btnCotDiem = new JButton("⚙ Cấu hình Cột điểm (%)");
        btnCotDiem.setBackground(new Color(30, 136, 229));
        btnCotDiem.setForeground(Color.WHITE);
        btnCotDiem.addActionListener(e -> btnCotDiemActionPerformed());

        btnDangKySV = new JButton("👥 Quản lý Sinh viên học");
        btnDangKySV.setBackground(new Color(142, 36, 170));
        btnDangKySV.setForeground(Color.WHITE);
        btnDangKySV.addActionListener(e -> btnDangKySVActionPerformed());

        btnXoa = new JButton("Xóa");
        btnXoa.setBackground(new Color(211, 47, 47));
        btnXoa.setForeground(Color.WHITE);
        btnXoa.addActionListener(e -> btnXoaActionPerformed());

        btnLamMoi = new JButton("Làm mới");
        btnLamMoi.addActionListener(e -> clearForm());

        pnlActions.add(btnThem);
        pnlActions.add(btnSua);
        pnlActions.add(btnCotDiem);
        pnlActions.add(btnDangKySV);
        pnlActions.add(btnXoa);
        pnlActions.add(btnLamMoi);
        pnlCenter.add(pnlActions, BorderLayout.SOUTH);

        add(pnlCenter, BorderLayout.CENTER);

        String[] columns = {"STT", "Mã LHP", "Môn Học", "Học Kỳ", "Giáo Viên", "Tổng Buổi", "Phép Nghỉ"};
        modelTable = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblLHP = new JTable(modelTable);
        tblLHP.setRowHeight(24);
        tblLHP.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                tblLHPMouseClicked();
            }
        });

        JScrollPane scroll = new JScrollPane(tblLHP);
        scroll.setBorder(BorderFactory.createTitledBorder("Danh sách lớp học phần"));
        scroll.setPreferredSize(new Dimension(900, 260));
        add(scroll, BorderLayout.SOUTH);
    }

    private void loadCombos() {
        cboMonHoc.removeAllItems();
        for (MonHoc m : mhDAO.getAll()) cboMonHoc.addItem(m);

        cboHocKy.removeAllItems();
        for (HocKy h : hkDAO.getAll()) cboHocKy.addItem(h);
    }

    private void fillTable(List<LopHocPhan> list) {
        modelTable.setRowCount(0);
        int stt = 1;
        for (LopHocPhan lhp : list) {
            modelTable.addRow(new Object[]{
                stt++, lhp.getMaLHP(), lhp.getTenMH(), lhp.getTenHK(),
                lhp.getGiaoVien(), lhp.getTongSoBuoi(), lhp.getSoPhepNghi()
            });
        }
    }

    private void tblLHPMouseClicked() {
        int row = tblLHP.getSelectedRow();
        if (row < 0) return;

        txtMaLHP.setText(modelTable.getValueAt(row, 1).toString());
        txtMaLHP.setEditable(false);

        String tenMH = modelTable.getValueAt(row, 2).toString();
        for (int i = 0; i < cboMonHoc.getItemCount(); i++) {
            MonHoc m = cboMonHoc.getItemAt(i);
            if (m.getTenMH().equalsIgnoreCase(tenMH)) { cboMonHoc.setSelectedIndex(i); break; }
        }

        String tenHK = modelTable.getValueAt(row, 3).toString();
        for (int i = 0; i < cboHocKy.getItemCount(); i++) {
            HocKy h = cboHocKy.getItemAt(i);
            if (h.getTenHK().equalsIgnoreCase(tenHK)) { cboHocKy.setSelectedIndex(i); break; }
        }

        txtGiaoVien.setText(modelTable.getValueAt(row, 4) != null ? modelTable.getValueAt(row, 4).toString() : "");
        spTongBuoi.setValue(Integer.parseInt(modelTable.getValueAt(row, 5).toString()));
        spPhepNghi.setValue(Integer.parseInt(modelTable.getValueAt(row, 6).toString()));
    }

    private void clearForm() {
        txtMaLHP.setText("");
        txtMaLHP.setEditable(true);
        txtGiaoVien.setText("");
        spLT.setValue(30);
        spTH.setValue(15);
        spTongBuoi.setValue(15);
        spPhepNghi.setValue(3);
        fillTable(lhpDAO.getAll());
    }

    private void btnThemActionPerformed() {
        String ma = txtMaLHP.getText().trim();
        MonHoc mh = (MonHoc) cboMonHoc.getSelectedItem();
        HocKy hk = (HocKy) cboHocKy.getSelectedItem();
        if (ma.isEmpty() || mh == null || hk == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập Mã LHP, Môn học và Học kỳ!");
            return;
        }

        LopHocPhan lhp = new LopHocPhan();
        lhp.setMaLHP(ma);
        lhp.setMaMH(mh.getMaMH());
        lhp.setMaHK(hk.getMaHK());
        lhp.setGiaoVien(txtGiaoVien.getText().trim());
        lhp.setSoTietLyThuyet((int) spLT.getValue());
        lhp.setSoTietThucHanh((int) spTH.getValue());
        lhp.setTongSoBuoi((int) spTongBuoi.getValue());
        lhp.setSoPhepNghi((int) spPhepNghi.getValue());

        if (lhpDAO.insert(lhp)) {
            // Mặc định tạo 3 cột điểm chuẩn: Chuyên cần 10%, Giữa kỳ 30%, Cuối kỳ 60%
            diemDAO.insertCotDiem(new CotDiem(0, ma, "Chuyên cần", 10.0, "ThanhPhan"));
            diemDAO.insertCotDiem(new CotDiem(0, ma, "Giữa kỳ", 30.0, "ThanhPhan"));
            diemDAO.insertCotDiem(new CotDiem(0, ma, "Cuối kỳ", 60.0, "CuoiKy"));

            JOptionPane.showMessageDialog(this, "Thêm LHP thành công (Đã tự động khởi tạo 3 cột điểm 10%, 30%, 60%)!");
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Thêm thất bại!");
        }
    }

    private void btnSuaActionPerformed() {
        String ma = txtMaLHP.getText().trim();
        if (ma.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn LHP cần sửa!");
            return;
        }
        MonHoc mh = (MonHoc) cboMonHoc.getSelectedItem();
        HocKy hk = (HocKy) cboHocKy.getSelectedItem();

        LopHocPhan lhp = new LopHocPhan();
        lhp.setMaLHP(ma);
        lhp.setMaMH(mh.getMaMH());
        lhp.setMaHK(hk.getMaHK());
        lhp.setGiaoVien(txtGiaoVien.getText().trim());
        lhp.setSoTietLyThuyet((int) spLT.getValue());
        lhp.setSoTietThucHanh((int) spTH.getValue());
        lhp.setTongSoBuoi((int) spTongBuoi.getValue());
        lhp.setSoPhepNghi((int) spPhepNghi.getValue());

        if (lhpDAO.update(lhp)) {
            JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Cập nhật thất bại!");
        }
    }

    private void btnXoaActionPerformed() {
        String ma = txtMaLHP.getText().trim();
        if (ma.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn LHP cần xóa!");
            return;
        }

        if (JOptionPane.showConfirmDialog(this, "Xóa LHP " + ma + "?", "Xác nhận", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            if (lhpDAO.delete(ma)) {
                JOptionPane.showMessageDialog(this, "Đã xóa!");
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Xóa thất bại! LHP đang có dữ liệu điểm sinh viên.");
            }
        }
    }

    private void btnCotDiemActionPerformed() {
        String ma = txtMaLHP.getText().trim();
        if (ma.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn LHP trên bảng trước!");
            return;
        }

        JDialog dlg = new JDialog(this, "Cấu hình Cột điểm (%) - " + ma, true);
        dlg.setSize(500, 350);
        dlg.setLocationRelativeTo(this);
        dlg.setLayout(new BorderLayout(5, 5));

        String[] cols = {"ID", "Tên cột điểm", "Trọng số (%)", "Loại"};
        DefaultTableModel cdModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable cdTbl = new JTable(cdModel);
        cdTbl.setRowHeight(24);

        Runnable refreshCD = () -> {
            cdModel.setRowCount(0);
            List<CotDiem> list = diemDAO.getCotDiemByLHP(ma);
            double total = 0;
            for (CotDiem cd : list) {
                cdModel.addRow(new Object[]{cd.getId(), cd.getTenCot(), cd.getTrongSo(), cd.getLoai()});
                total += cd.getTrongSo();
            }
            dlg.setTitle("Cấu hình Cột điểm - " + ma + " (Tổng trọng số: " + total + "%)");
        };
        refreshCD.run();

        dlg.add(new JScrollPane(cdTbl), BorderLayout.CENTER);

        JPanel pnlBtn = new JPanel(new FlowLayout());
        JButton btnAdd = new JButton("➕ Thêm cột điểm");
        btnAdd.addActionListener(e -> {
            String ten = JOptionPane.showInputDialog(dlg, "Nhập tên cột (ví dụ: Chuyên cần, Thực hành, Giữa kỳ, Cuối kỳ):");
            if (ten == null || ten.trim().isEmpty()) return;
            String tsStr = JOptionPane.showInputDialog(dlg, "Nhập trọng số phần trăm % (ví dụ 10, 20, 30, 40):");
            if (tsStr == null) return;
            try {
                double ts = Double.parseDouble(tsStr.trim());
                String[] loaiArr = {"ThanhPhan", "CuoiKy"};
                String loai = (String) JOptionPane.showInputDialog(dlg, "Loại điểm:", "Chọn", JOptionPane.QUESTION_MESSAGE, null, loaiArr, loaiArr[0]);
                if (loai != null) {
                    diemDAO.insertCotDiem(new CotDiem(0, ma, ten.trim(), ts, loai));
                    refreshCD.run();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dlg, "Trọng số không hợp lệ!");
            }
        });

        JButton btnDel = new JButton("🗑 Xóa cột");
        btnDel.addActionListener(e -> {
            int sel = cdTbl.getSelectedRow();
            if (sel < 0) return;
            int id = (int) cdModel.getValueAt(sel, 0);
            diemDAO.deleteCotDiem(id);
            refreshCD.run();
        });

        pnlBtn.add(btnAdd);
        pnlBtn.add(btnDel);
        dlg.add(pnlBtn, BorderLayout.SOUTH);
        dlg.setVisible(true);
    }

    private void btnDangKySVActionPerformed() {
        String ma = txtMaLHP.getText().trim();
        if (ma.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn LHP trên bảng trước!");
            return;
        }

        JDialog dlg = new JDialog(this, "Danh sách Sinh viên học - " + ma, true);
        dlg.setSize(600, 420);
        dlg.setLocationRelativeTo(this);
        dlg.setLayout(new BorderLayout(5, 5));

        String[] cols = {"STT", "Mã SV", "Họ và tên", "Lớp", "Trạng thái"};
        DefaultTableModel svModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable svTbl = new JTable(svModel);
        svTbl.setRowHeight(24);

        Runnable refreshSV = () -> {
            svModel.setRowCount(0);
            List<SinhVien> list = svDAO.getSinhVienByLHP(ma);
            int stt = 1;
            for (SinhVien s : list) {
                svModel.addRow(new Object[]{stt++, s.getMaSV(), s.getHoTen(), s.getTenLop(), s.getTrangThai()});
            }
        };
        refreshSV.run();

        dlg.add(new JScrollPane(svTbl), BorderLayout.CENTER);

        JPanel pnlBtn = new JPanel(new FlowLayout());
        JButton btnAdd = new JButton("➕ Thêm SV vào lớp");
        btnAdd.addActionListener(e -> {
            String maSV = JOptionPane.showInputDialog(dlg, "Nhập Mã Sinh Viên:");
            if (maSV != null && !maSV.trim().isEmpty()) {
                SinhVien sv = svDAO.getSinhVienById(maSV.trim());
                if (sv == null) {
                    JOptionPane.showMessageDialog(dlg, "Không tìm thấy Mã SV: " + maSV.trim());
                } else {
                    lhpDAO.dangKySinhVien(sv.getMaSV(), ma);
                    refreshSV.run();
                }
            }
        });

        JButton btnDel = new JButton("🗑 Hủy đăng ký");
        btnDel.addActionListener(e -> {
            int sel = svTbl.getSelectedRow();
            if (sel < 0) return;
            String maSV = svModel.getValueAt(sel, 1).toString();
            lhpDAO.huyDangKySinhVien(maSV, ma);
            refreshSV.run();
        });

        pnlBtn.add(btnAdd);
        pnlBtn.add(btnDel);
        dlg.add(pnlBtn, BorderLayout.SOUTH);
        dlg.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FormLopHocPhan().setVisible(true));
    }
}
