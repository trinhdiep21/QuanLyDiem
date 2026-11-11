package ui;

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
import java.util.List;

public class LopHocPhanPanel extends JPanel {

    private JTable table;
    private DefaultTableModel tableModel;
    private JComboBox<HocKy> cbHocKy;

    private LopHocPhanDAO lhpDAO;
    private HocKyDAO hkDAO;
    private MonHocDAO mhDAO;
    private DiemDAO diemDAO;
    private SinhVienDAO svDAO;

    public LopHocPhanPanel() {
        lhpDAO = new LopHocPhanDAO();
        hkDAO = new HocKyDAO();
        mhDAO = new MonHocDAO();
        diemDAO = new DiemDAO();
        svDAO = new SinhVienDAO();
        initUI();
        loadData();
    }

    private void initUI() {
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Top Filter
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));
        topPanel.setBorder(BorderFactory.createTitledBorder("Lọc theo học kỳ"));

        topPanel.add(new JLabel("Học kỳ:"));
        cbHocKy = new JComboBox<>();
        cbHocKy.setPreferredSize(new Dimension(200, 26));
        cbHocKy.addActionListener(e -> loadData());
        topPanel.add(cbHocKy);

        JButton btnAll = new JButton("🔄 Tất cả");
        btnAll.addActionListener(e -> {
            cbHocKy.setSelectedItem(null);
            loadData();
        });
        topPanel.add(btnAll);

        add(topPanel, BorderLayout.NORTH);

        // Center Table
        String[] cols = {"STT", "Mã LHP", "Môn học", "Học kỳ", "Giáo viên", "Tổng số buổi", "Số phép nghỉ"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(26);

        JPanel center = new JPanel(new BorderLayout());
        center.setBorder(BorderFactory.createTitledBorder("Danh sách lớp học phần"));
        center.add(new JScrollPane(table));
        add(center, BorderLayout.CENTER);

        // South Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));

        JButton btnThem = new JButton("➕ Thêm LHP");
        btnThem.setBackground(new Color(46, 125, 50)); btnThem.setForeground(Color.WHITE);
        btnThem.addActionListener(e -> showForm(null));

        JButton btnSua = new JButton("✏ Sửa LHP");
        btnSua.setBackground(new Color(255, 152, 0)); btnSua.setForeground(Color.WHITE);
        btnSua.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(this, "Chọn lớp học phần cần sửa!"); return; }
            String maLHP = (String) tableModel.getValueAt(row, 1);
            showForm(lhpDAO.getById(maLHP));
        });

        JButton btnCotDiem = new JButton("⚙ Quản lý Cột điểm");
        btnCotDiem.setBackground(new Color(30, 136, 229)); btnCotDiem.setForeground(Color.WHITE);
        btnCotDiem.addActionListener(e -> openCotDiemDialog());

        JButton btnDangKySV = new JButton("👥 Đăng ký Sinh viên");
        btnDangKySV.setBackground(new Color(142, 36, 170)); btnDangKySV.setForeground(Color.WHITE);
        btnDangKySV.addActionListener(e -> openDangKyDialog());

        JButton btnXoa = new JButton("🗑 Xóa");
        btnXoa.setBackground(new Color(211, 47, 47)); btnXoa.setForeground(Color.WHITE);
        btnXoa.addActionListener(e -> xoaLHP());

        btnPanel.add(btnThem);
        btnPanel.add(btnSua);
        btnPanel.add(btnCotDiem);
        btnPanel.add(btnDangKySV);
        btnPanel.add(btnXoa);

        add(btnPanel, BorderLayout.SOUTH);

        // Load combobox
        cbHocKy.addItem(null);
        for (HocKy hk : hkDAO.getAll()) cbHocKy.addItem(hk);
    }

    private void loadData() {
        tableModel.setRowCount(0);
        HocKy hk = (HocKy) cbHocKy.getSelectedItem();
        List<LopHocPhan> list = hk != null ? lhpDAO.getByHocKy(hk.getMaHK()) : lhpDAO.getAll();
        int stt = 1;
        for (LopHocPhan lhp : list) {
            tableModel.addRow(new Object[]{
                stt++, lhp.getMaLHP(), lhp.getTenMH(), lhp.getTenHK(),
                lhp.getGiaoVien(), lhp.getTongSoBuoi(), lhp.getSoPhepNghi()
            });
        }
    }

    private void showForm(LopHocPhan lhp) {
        boolean isEdit = lhp != null;
        JDialog dlg = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
                isEdit ? "Sửa lớp học phần" : "Thêm lớp học phần", true);
        dlg.setSize(440, 380);
        dlg.setLocationRelativeTo(this);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; form.add(new JLabel("Mã LHP *:"), gbc);
        gbc.gridx = 1; JTextField tfMa = new JTextField(isEdit ? lhp.getMaLHP() : "", 15);
        tfMa.setEditable(!isEdit); form.add(tfMa, gbc);

        gbc.gridx = 0; gbc.gridy = 1; form.add(new JLabel("Môn học *:"), gbc);
        gbc.gridx = 1; JComboBox<MonHoc> cbMH = new JComboBox<>();
        for (MonHoc m : mhDAO.getAll()) {
            cbMH.addItem(m);
            if (isEdit && m.getMaMH().equals(lhp.getMaMH())) cbMH.setSelectedItem(m);
        }
        form.add(cbMH, gbc);

        gbc.gridx = 0; gbc.gridy = 2; form.add(new JLabel("Học kỳ *:"), gbc);
        gbc.gridx = 1; JComboBox<HocKy> cbHKForm = new JComboBox<>();
        for (HocKy h : hkDAO.getAll()) {
            cbHKForm.addItem(h);
            if (isEdit && h.getMaHK().equals(lhp.getMaHK())) cbHKForm.setSelectedItem(h);
        }
        form.add(cbHKForm, gbc);

        gbc.gridx = 0; gbc.gridy = 3; form.add(new JLabel("Giáo viên:"), gbc);
        gbc.gridx = 1; JTextField tfGV = new JTextField(isEdit ? lhp.getGiaoVien() : "", 15); form.add(tfGV, gbc);

        gbc.gridx = 0; gbc.gridy = 4; form.add(new JLabel("Số tiết LT:"), gbc);
        gbc.gridx = 1; JSpinner spLT = new JSpinner(new SpinnerNumberModel(isEdit ? lhp.getSoTietLyThuyet() : 30, 0, 150, 5));
        form.add(spLT, gbc);

        gbc.gridx = 0; gbc.gridy = 5; form.add(new JLabel("Số tiết TH:"), gbc);
        gbc.gridx = 1; JSpinner spTH = new JSpinner(new SpinnerNumberModel(isEdit ? lhp.getSoTietThucHanh() : 15, 0, 150, 5));
        form.add(spTH, gbc);

        gbc.gridx = 0; gbc.gridy = 6; form.add(new JLabel("Tổng số buổi:"), gbc);
        gbc.gridx = 1; JSpinner spBuoi = new JSpinner(new SpinnerNumberModel(isEdit ? lhp.getTongSoBuoi() : 15, 1, 100, 1));
        form.add(spBuoi, gbc);

        gbc.gridx = 0; gbc.gridy = 7; form.add(new JLabel("Số phép nghỉ:"), gbc);
        gbc.gridx = 1; JSpinner spPhep = new JSpinner(new SpinnerNumberModel(isEdit ? lhp.getSoPhepNghi() : 3, 0, 20, 1));
        form.add(spPhep, gbc);

        dlg.add(form, BorderLayout.CENTER);

        JPanel btnP = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 8));
        JButton btnSave = new JButton(isEdit ? "💾 Cập nhật" : "💾 Thêm mới");
        btnSave.setBackground(new Color(46, 125, 50)); btnSave.setForeground(Color.WHITE);
        btnSave.addActionListener(e -> {
            String ma = tfMa.getText().trim();
            MonHoc selMH = (MonHoc) cbMH.getSelectedItem();
            HocKy selHK = (HocKy) cbHKForm.getSelectedItem();
            if (ma.isEmpty() || selMH == null || selHK == null) {
                JOptionPane.showMessageDialog(dlg, "Vui lòng điền đủ Mã, Môn học, Học kỳ!"); return;
            }
            LopHocPhan item = new LopHocPhan();
            item.setMaLHP(ma);
            item.setMaMH(selMH.getMaMH());
            item.setMaHK(selHK.getMaHK());
            item.setGiaoVien(tfGV.getText().trim());
            item.setSoTietLyThuyet((int) spLT.getValue());
            item.setSoTietThucHanh((int) spTH.getValue());
            item.setTongSoBuoi((int) spBuoi.getValue());
            item.setSoPhepNghi((int) spPhep.getValue());

            boolean ok = isEdit ? lhpDAO.update(item) : lhpDAO.insert(item);
            if (ok) {
                // If new, create default grade columns (Chuyên cần 10%, Giữa kỳ 30%, Cuối kỳ 60%)
                if (!isEdit) {
                    diemDAO.insertCotDiem(new CotDiem(0, ma, "Chuyên cần", 10.0, "ThanhPhan"));
                    diemDAO.insertCotDiem(new CotDiem(0, ma, "Giữa kỳ", 30.0, "ThanhPhan"));
                    diemDAO.insertCotDiem(new CotDiem(0, ma, "Cuối kỳ", 60.0, "CuoiKy"));
                }
                dlg.dispose();
                loadData();
                JOptionPane.showMessageDialog(this, "Thành công!");
            } else {
                JOptionPane.showMessageDialog(dlg, "Thao tác thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });
        btnP.add(btnSave);
        JButton btnCancel = new JButton("Hủy"); btnCancel.addActionListener(e -> dlg.dispose());
        btnP.add(btnCancel);
        dlg.add(btnP, BorderLayout.SOUTH);
        dlg.setVisible(true);
    }

    private void openCotDiemDialog() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Chọn lớp học phần!"); return; }
        String maLHP = (String) tableModel.getValueAt(row, 1);

        JDialog dlg = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Cấu hình cột điểm - " + maLHP, true);
        dlg.setSize(500, 360);
        dlg.setLocationRelativeTo(this);
        dlg.setLayout(new BorderLayout(5, 5));

        String[] cols = {"ID", "Tên cột điểm", "Trọng số (%)", "Loại"};
        DefaultTableModel cdModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable cdTable = new JTable(cdModel);
        cdTable.setRowHeight(24);

        Runnable refreshCD = () -> {
            cdModel.setRowCount(0);
            List<CotDiem> list = diemDAO.getCotDiemByLHP(maLHP);
            double sum = 0;
            for (CotDiem cd : list) {
                cdModel.addRow(new Object[]{cd.getId(), cd.getTenCot(), cd.getTrongSo(), cd.getLoai()});
                sum += cd.getTrongSo();
            }
            dlg.setTitle("Cấu hình cột điểm - " + maLHP + " (Tổng trọng số: " + sum + "%)");
        };
        refreshCD.run();

        dlg.add(new JScrollPane(cdTable), BorderLayout.CENTER);

        JPanel bp = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 5));
        JButton btnAddCD = new JButton("➕ Thêm cột");
        btnAddCD.addActionListener(e -> {
            String ten = JOptionPane.showInputDialog(dlg, "Nhập tên cột điểm:");
            if (ten == null || ten.trim().isEmpty()) return;
            String tsStr = JOptionPane.showInputDialog(dlg, "Nhập trọng số % (ví dụ 10, 20, 30):");
            if (tsStr == null) return;
            try {
                double ts = Double.parseDouble(tsStr.trim());
                String[] loaiArr = {"ThanhPhan", "CuoiKy"};
                String loai = (String) JOptionPane.showInputDialog(dlg, "Chọn loại:", "Loại cột điểm",
                        JOptionPane.QUESTION_MESSAGE, null, loaiArr, loaiArr[0]);
                if (loai != null) {
                    diemDAO.insertCotDiem(new CotDiem(0, maLHP, ten.trim(), ts, loai));
                    refreshCD.run();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dlg, "Trọng số không hợp lệ!");
            }
        });

        JButton btnDelCD = new JButton("🗑 Xóa cột");
        btnDelCD.addActionListener(e -> {
            int sel = cdTable.getSelectedRow();
            if (sel < 0) return;
            int id = (int) cdModel.getValueAt(sel, 0);
            if (JOptionPane.showConfirmDialog(dlg, "Xóa cột điểm này?", "Xác nhận", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                diemDAO.deleteCotDiem(id);
                refreshCD.run();
            }
        });

        bp.add(btnAddCD); bp.add(btnDelCD);
        dlg.add(bp, BorderLayout.SOUTH);
        dlg.setVisible(true);
    }

    private void openDangKyDialog() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Chọn lớp học phần!"); return; }
        String maLHP = (String) tableModel.getValueAt(row, 1);

        JDialog dlg = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Danh sách sinh viên học - " + maLHP, true);
        dlg.setSize(600, 420);
        dlg.setLocationRelativeTo(this);
        dlg.setLayout(new BorderLayout(5, 5));

        String[] cols = {"STT", "Mã SV", "Họ và tên", "Lớp", "Trạng thái"};
        DefaultTableModel svModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable svTable = new JTable(svModel);
        svTable.setRowHeight(24);

        Runnable refreshSV = () -> {
            svModel.setRowCount(0);
            List<SinhVien> list = svDAO.getSinhVienByLHP(maLHP);
            int stt = 1;
            for (SinhVien s : list) {
                svModel.addRow(new Object[]{stt++, s.getMaSV(), s.getHoTen(), s.getTenLop(), s.getTrangThai()});
            }
        };
        refreshSV.run();

        dlg.add(new JScrollPane(svTable), BorderLayout.CENTER);

        JPanel bp = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 5));
        JButton btnAddSV = new JButton("➕ Thêm SV vào lớp");
        btnAddSV.addActionListener(e -> {
            String maSV = JOptionPane.showInputDialog(dlg, "Nhập Mã Sinh Viên:");
            if (maSV != null && !maSV.trim().isEmpty()) {
                SinhVien sv = svDAO.getSinhVienById(maSV.trim());
                if (sv == null) {
                    JOptionPane.showMessageDialog(dlg, "Không tìm thấy mã SV: " + maSV.trim());
                } else {
                    lhpDAO.dangKySinhVien(sv.getMaSV(), maLHP);
                    refreshSV.run();
                    JOptionPane.showMessageDialog(dlg, "Đã thêm sinh viên vào lớp!");
                }
            }
        });

        JButton btnDelSV = new JButton("🗑 Hủy đăng ký");
        btnDelSV.addActionListener(e -> {
            int sel = svTable.getSelectedRow();
            if (sel < 0) return;
            String maSV = (String) svModel.getValueAt(sel, 1);
            if (JOptionPane.showConfirmDialog(dlg, "Hủy sinh viên " + maSV + " khỏi lớp học phần?", "Xác nhận", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                lhpDAO.huyDangKySinhVien(maSV, maLHP);
                refreshSV.run();
            }
        });

        bp.add(btnAddSV); bp.add(btnDelSV);
        dlg.add(bp, BorderLayout.SOUTH);
        dlg.setVisible(true);
    }

    private void xoaLHP() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Chọn lớp học phần cần xóa!"); return; }
        String ma = (String) tableModel.getValueAt(row, 1);
        if (JOptionPane.showConfirmDialog(this, "Xóa lớp học phần \"" + ma + "\"?", "Xác nhận",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            if (lhpDAO.delete(ma)) { loadData(); JOptionPane.showMessageDialog(this, "Đã xóa!"); }
            else JOptionPane.showMessageDialog(this, "Xóa thất bại! LHP đang có dữ liệu điểm hoặc đăng ký.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}
