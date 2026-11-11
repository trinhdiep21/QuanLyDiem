package ui;

import dao.KhoaHocDAO;
import dao.LopHocDAO;
import dao.SinhVienDAO;
import model.KhoaHoc;
import model.LopHoc;
import model.SinhVien;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

public class SinhVienPanel extends JPanel {

    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField tfTimKiem;
    private JComboBox<KhoaHoc> cbKhoa;
    private JComboBox<LopHoc> cbLop;

    private SinhVienDAO svDAO;
    private LopHocDAO lopDAO;
    private KhoaHocDAO khoaDAO;

    private static final SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy");

    public SinhVienPanel() {
        svDAO = new SinhVienDAO();
        lopDAO = new LopHocDAO();
        khoaDAO = new KhoaHocDAO();
        initUI();
        loadData();
    }

    private void initUI() {
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ===== TOP: Tìm kiếm =====
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Tìm kiếm"));

        searchPanel.add(new JLabel("Từ khóa:"));
        tfTimKiem = new JTextField(14);
        searchPanel.add(tfTimKiem);

        searchPanel.add(new JLabel("Khóa:"));
        cbKhoa = new JComboBox<>();
        cbKhoa.setPreferredSize(new Dimension(150, 26));
        cbKhoa.addActionListener(e -> onKhoaChanged());
        searchPanel.add(cbKhoa);

        searchPanel.add(new JLabel("Lớp:"));
        cbLop = new JComboBox<>();
        cbLop.setPreferredSize(new Dimension(150, 26));
        searchPanel.add(cbLop);

        JButton btnSearch = new JButton("🔍 Tìm");
        btnSearch.setBackground(new Color(52, 152, 219));
        btnSearch.setForeground(Color.WHITE);
        btnSearch.addActionListener(e -> search());
        searchPanel.add(btnSearch);

        JButton btnRefresh = new JButton("🔄 Tất cả");
        btnRefresh.addActionListener(e -> { cbKhoa.setSelectedIndex(0); tfTimKiem.setText(""); loadData(); });
        searchPanel.add(btnRefresh);

        add(searchPanel, BorderLayout.NORTH);

        // ===== CENTER: Table =====
        String[] cols = {"STT", "Mã SV", "Họ và tên", "Ngày sinh", "Giới tính", "Lớp", "SĐT", "Email", "Trạng thái"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(26);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getColumnModel().getColumn(0).setPreferredWidth(40);
        table.getColumnModel().getColumn(1).setPreferredWidth(90);
        table.getColumnModel().getColumn(2).setPreferredWidth(180);

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("Danh sách sinh viên"));
        tablePanel.add(new JScrollPane(table));
        add(tablePanel, BorderLayout.CENTER);

        // ===== SOUTH: Buttons =====
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));
        JButton btnThem = new JButton("➕ Thêm");
        btnThem.setBackground(new Color(46, 125, 50)); btnThem.setForeground(Color.WHITE);
        btnThem.addActionListener(e -> showForm(null));

        JButton btnSua = new JButton("✏ Sửa");
        btnSua.setBackground(new Color(255, 152, 0)); btnSua.setForeground(Color.WHITE);
        btnSua.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(this, "Chọn sinh viên cần sửa!"); return; }
            String maSV = (String) tableModel.getValueAt(row, 1);
            showForm(svDAO.getSinhVienById(maSV));
        });

        JButton btnXoa = new JButton("🗑 Xóa");
        btnXoa.setBackground(new Color(211, 47, 47)); btnXoa.setForeground(Color.WHITE);
        btnXoa.addActionListener(e -> xoaSV());

        btnPanel.add(btnThem); btnPanel.add(btnSua); btnPanel.add(btnXoa);
        JLabel lblCount = new JLabel();
        btnPanel.add(Box.createHorizontalStrut(20));
        btnPanel.add(lblCount);
        add(btnPanel, BorderLayout.SOUTH);
    }

    private void loadData() {
        // Load khoa
        Object savedKhoa = cbKhoa.getSelectedItem();
        cbKhoa.removeAllItems();
        cbKhoa.addItem(null);
        for (KhoaHoc kh : khoaDAO.getAll()) cbKhoa.addItem(kh);

        tableModel.setRowCount(0);
        List<SinhVien> list = svDAO.getAllSinhVien();
        int stt = 1;
        for (SinhVien sv : list) {
            String ngaySinh = sv.getNgaySinh() != null ? SDF.format(sv.getNgaySinh()) : "";
            tableModel.addRow(new Object[]{
                stt++, sv.getMaSV(), sv.getHoTen(), ngaySinh,
                sv.getGioiTinh(), sv.getTenLop(), sv.getSoDienThoai(),
                sv.getEmail(), sv.getTrangThai()
            });
        }
    }

    private void onKhoaChanged() {
        cbLop.removeAllItems();
        cbLop.addItem(null);
        KhoaHoc kh = (KhoaHoc) cbKhoa.getSelectedItem();
        if (kh != null) {
            for (LopHoc lop : lopDAO.getByKhoa(kh.getMaKhoa())) cbLop.addItem(lop);
        }
    }

    private void search() {
        tableModel.setRowCount(0);
        String kw = tfTimKiem.getText().trim();
        LopHoc lop = (LopHoc) cbLop.getSelectedItem();
        KhoaHoc khoa = (KhoaHoc) cbKhoa.getSelectedItem();
        List<SinhVien> list = svDAO.searchSinhVien(
            kw.isEmpty() ? null : kw,
            lop != null ? lop.getMaLop() : null,
            khoa != null ? khoa.getMaKhoa() : null
        );
        int stt = 1;
        for (SinhVien sv : list) {
            String ngaySinh = sv.getNgaySinh() != null ? SDF.format(sv.getNgaySinh()) : "";
            tableModel.addRow(new Object[]{
                stt++, sv.getMaSV(), sv.getHoTen(), ngaySinh,
                sv.getGioiTinh(), sv.getTenLop(), sv.getSoDienThoai(),
                sv.getEmail(), sv.getTrangThai()
            });
        }
    }

    private void showForm(SinhVien sv) {
        boolean isEdit = sv != null;
        JDialog dlg = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
            isEdit ? "Sửa thông tin sinh viên" : "Thêm sinh viên mới", true);
        dlg.setSize(480, 420);
        dlg.setLocationRelativeTo(this);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField tfMaSV = addField(form, gbc, "Mã SV *:", 0, isEdit ? sv.getMaSV() : "");
        tfMaSV.setEditable(!isEdit);
        JTextField tfHoTen = addField(form, gbc, "Họ và tên *:", 1, isEdit ? sv.getHoTen() : "");
        JTextField tfNgaySinh = addField(form, gbc, "Ngày sinh (dd/MM/yyyy):", 2,
            isEdit && sv.getNgaySinh() != null ? SDF.format(sv.getNgaySinh()) : "");
        JTextField tfDiaChi = addField(form, gbc, "Địa chỉ:", 3, isEdit ? sv.getDiaChi() : "");
        JTextField tfEmail = addField(form, gbc, "Email:", 4, isEdit ? sv.getEmail() : "");
        JTextField tfSDT = addField(form, gbc, "Số điện thoại:", 5, isEdit ? sv.getSoDienThoai() : "");

        gbc.gridx = 0; gbc.gridy = 6;
        form.add(new JLabel("Giới tính:"), gbc);
        gbc.gridx = 1;
        JComboBox<String> cbGT = new JComboBox<>(new String[]{"Nam", "Nu", "Khac"});
        if (isEdit) cbGT.setSelectedItem(sv.getGioiTinh());
        form.add(cbGT, gbc);

        gbc.gridx = 0; gbc.gridy = 7;
        form.add(new JLabel("Lớp:"), gbc);
        gbc.gridx = 1;
        JComboBox<LopHoc> cbLopForm = new JComboBox<>();
        cbLopForm.addItem(null);
        for (LopHoc lop : lopDAO.getAll()) {
            cbLopForm.addItem(lop);
            if (isEdit && lop.getMaLop().equals(sv.getMaLop())) cbLopForm.setSelectedItem(lop);
        }
        form.add(cbLopForm, gbc);

        gbc.gridx = 0; gbc.gridy = 8;
        form.add(new JLabel("Trạng thái:"), gbc);
        gbc.gridx = 1;
        JComboBox<String> cbTT = new JComboBox<>(new String[]{"DangHoc", "TotNghiep", "NghiHoc", "ThoiHoc"});
        if (isEdit) cbTT.setSelectedItem(sv.getTrangThai());
        form.add(cbTT, gbc);

        dlg.add(form, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 8));
        JButton btnSave = new JButton(isEdit ? "💾 Cập nhật" : "💾 Thêm mới");
        btnSave.setBackground(new Color(46, 125, 50)); btnSave.setForeground(Color.WHITE);
        btnSave.addActionListener(e -> {
            String maSVVal = tfMaSV.getText().trim();
            String hoTenVal = tfHoTen.getText().trim();
            if (maSVVal.isEmpty() || hoTenVal.isEmpty()) {
                JOptionPane.showMessageDialog(dlg, "Mã SV và Họ tên không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            SinhVien newSV = new SinhVien();
            newSV.setMaSV(maSVVal);
            newSV.setHoTen(hoTenVal);
            try {
                String ns = tfNgaySinh.getText().trim();
                if (!ns.isEmpty()) newSV.setNgaySinh(SDF.parse(ns));
            } catch (ParseException ex) { /* ignore */ }
            newSV.setDiaChi(tfDiaChi.getText().trim());
            newSV.setEmail(tfEmail.getText().trim());
            newSV.setSoDienThoai(tfSDT.getText().trim());
            newSV.setGioiTinh((String) cbGT.getSelectedItem());
            LopHoc selLop = (LopHoc) cbLopForm.getSelectedItem();
            newSV.setMaLop(selLop != null ? selLop.getMaLop() : null);
            newSV.setTrangThai((String) cbTT.getSelectedItem());

            boolean ok = isEdit ? svDAO.update(newSV) : svDAO.insert(newSV);
            if (ok) {
                JOptionPane.showMessageDialog(dlg, isEdit ? "Cập nhật thành công!" : "Thêm thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                dlg.dispose();
                loadData();
            } else {
                JOptionPane.showMessageDialog(dlg, "Thao tác thất bại! Kiểm tra lại dữ liệu.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });
        JButton btnCancel = new JButton("Hủy");
        btnCancel.addActionListener(e -> dlg.dispose());
        btnPanel.add(btnSave); btnPanel.add(btnCancel);
        dlg.add(btnPanel, BorderLayout.SOUTH);
        dlg.setVisible(true);
    }

    private JTextField addField(JPanel form, GridBagConstraints gbc, String label, int row, String value) {
        gbc.gridx = 0; gbc.gridy = row;
        form.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        JTextField tf = new JTextField(value, 20);
        form.add(tf, gbc);
        return tf;
    }

    private void xoaSV() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Chọn sinh viên cần xóa!"); return; }
        String maSV = (String) tableModel.getValueAt(row, 1);
        String hoTen = (String) tableModel.getValueAt(row, 2);
        int ok = JOptionPane.showConfirmDialog(this,
            "Xóa sinh viên \"" + hoTen + "\" (" + maSV + ")?", "Xác nhận",
            JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (ok == JOptionPane.YES_OPTION) {
            if (svDAO.delete(maSV)) {
                loadData();
                JOptionPane.showMessageDialog(this, "Đã xóa!");
            } else {
                JOptionPane.showMessageDialog(this, "Xóa thất bại! Sinh viên có dữ liệu liên quan.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
