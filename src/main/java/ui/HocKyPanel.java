package ui;

import dao.HocKyDAO;
import model.HocKy;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class HocKyPanel extends JPanel {

    private JTable table;
    private DefaultTableModel tableModel;
    private HocKyDAO hkDAO;

    public HocKyPanel() {
        hkDAO = new HocKyDAO();
        initUI();
        loadData();
    }

    private void initUI() {
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));
        topPanel.setBorder(BorderFactory.createTitledBorder("Quản lý học kỳ"));
        JButton btnRefresh = new JButton("🔄 Làm mới");
        btnRefresh.addActionListener(e -> loadData());
        topPanel.add(btnRefresh);
        add(topPanel, BorderLayout.NORTH);

        String[] cols = {"STT", "Mã HK", "Tên học kỳ", "Năm học", "Kỳ học", "Trạng thái"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(26);

        JPanel center = new JPanel(new BorderLayout());
        center.setBorder(BorderFactory.createTitledBorder("Danh sách học kỳ"));
        center.add(new JScrollPane(table));
        add(center, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));
        JButton btnThem = new JButton("➕ Thêm");
        btnThem.setBackground(new Color(46, 125, 50)); btnThem.setForeground(Color.WHITE);
        btnThem.addActionListener(e -> showForm(null));

        JButton btnSua = new JButton("✏ Sửa");
        btnSua.setBackground(new Color(255, 152, 0)); btnSua.setForeground(Color.WHITE);
        btnSua.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(this, "Chọn học kỳ cần sửa!"); return; }
            String maHK = (String) tableModel.getValueAt(row, 1);
            List<HocKy> list = hkDAO.getAll();
            list.stream().filter(h -> h.getMaHK().equals(maHK)).findFirst().ifPresent(this::showForm);
        });

        JButton btnToggleStatus = new JButton("🔒 Đóng/Mở kỳ");
        btnToggleStatus.setBackground(new Color(100, 100, 150)); btnToggleStatus.setForeground(Color.WHITE);
        btnToggleStatus.addActionListener(e -> toggleStatus());

        JButton btnXoa = new JButton("🗑 Xóa");
        btnXoa.setBackground(new Color(211, 47, 47)); btnXoa.setForeground(Color.WHITE);
        btnXoa.addActionListener(e -> xoa());

        btnPanel.add(btnThem);
        btnPanel.add(btnSua);
        btnPanel.add(btnToggleStatus);
        btnPanel.add(btnXoa);
        add(btnPanel, BorderLayout.SOUTH);
    }

    private void loadData() {
        tableModel.setRowCount(0);
        List<HocKy> list = hkDAO.getAll();
        int stt = 1;
        for (HocKy hk : list) {
            String tt = "DangHoc".equals(hk.getTrangThai()) ? "Đang học" : "Đã đóng (Khóa)";
            tableModel.addRow(new Object[]{stt++, hk.getMaHK(), hk.getTenHK(), hk.getNamHoc(), hk.getKyHoc(), tt});
        }
    }

    private void toggleStatus() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Chọn học kỳ!"); return; }
        String maHK = (String) tableModel.getValueAt(row, 1);
        List<HocKy> list = hkDAO.getAll();
        HocKy found = list.stream().filter(h -> h.getMaHK().equals(maHK)).findFirst().orElse(null);
        if (found != null) {
            String nextTT = "DangHoc".equals(found.getTrangThai()) ? "DaDong" : "DangHoc";
            found.setTrangThai(nextTT);
            if (hkDAO.update(found)) {
                loadData();
                JOptionPane.showMessageDialog(this, "Đã cập nhật trạng thái học kỳ thành công!");
            }
        }
    }

    private void showForm(HocKy hk) {
        boolean isEdit = hk != null;
        JDialog dlg = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
                isEdit ? "Sửa học kỳ" : "Thêm học kỳ", true);
        dlg.setSize(380, 280);
        dlg.setLocationRelativeTo(this);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; form.add(new JLabel("Mã HK *:"), gbc);
        gbc.gridx = 1; JTextField tfMa = new JTextField(isEdit ? hk.getMaHK() : "", 15);
        tfMa.setEditable(!isEdit); form.add(tfMa, gbc);

        gbc.gridx = 0; gbc.gridy = 1; form.add(new JLabel("Tên HK *:"), gbc);
        gbc.gridx = 1; JTextField tfTen = new JTextField(isEdit ? hk.getTenHK() : "", 15); form.add(tfTen, gbc);

        gbc.gridx = 0; gbc.gridy = 2; form.add(new JLabel("Năm học *:"), gbc);
        gbc.gridx = 1; JTextField tfNam = new JTextField(isEdit ? hk.getNamHoc() : "2024-2025", 15); form.add(tfNam, gbc);

        gbc.gridx = 0; gbc.gridy = 3; form.add(new JLabel("Kỳ học:"), gbc);
        gbc.gridx = 1; JSpinner spKy = new JSpinner(new SpinnerNumberModel(isEdit ? hk.getKyHoc() : 1, 1, 3, 1));
        form.add(spKy, gbc);

        gbc.gridx = 0; gbc.gridy = 4; form.add(new JLabel("Trạng thái:"), gbc);
        gbc.gridx = 1; JComboBox<String> cbTT = new JComboBox<>(new String[]{"DangHoc", "DaDong"});
        if (isEdit) cbTT.setSelectedItem(hk.getTrangThai());
        form.add(cbTT, gbc);

        dlg.add(form, BorderLayout.CENTER);

        JPanel btnP = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 8));
        JButton btnSave = new JButton(isEdit ? "💾 Cập nhật" : "💾 Thêm mới");
        btnSave.setBackground(new Color(46, 125, 50)); btnSave.setForeground(Color.WHITE);
        btnSave.addActionListener(e -> {
            String ma = tfMa.getText().trim(), ten = tfTen.getText().trim(), nam = tfNam.getText().trim();
            if (ma.isEmpty() || ten.isEmpty()) {
                JOptionPane.showMessageDialog(dlg, "Vui lòng nhập đầy đủ Mã và Tên!"); return;
            }
            HocKy item = new HocKy();
            item.setMaHK(ma); item.setTenHK(ten); item.setNamHoc(nam);
            item.setKyHoc((int) spKy.getValue());
            item.setTrangThai((String) cbTT.getSelectedItem());

            boolean ok = isEdit ? hkDAO.update(item) : hkDAO.insert(item);
            if (ok) { dlg.dispose(); loadData(); JOptionPane.showMessageDialog(this, "Thành công!"); }
            else JOptionPane.showMessageDialog(dlg, "Thao tác thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        });
        btnP.add(btnSave);
        JButton btnCancel = new JButton("Hủy"); btnCancel.addActionListener(e -> dlg.dispose());
        btnP.add(btnCancel);
        dlg.add(btnP, BorderLayout.SOUTH);
        dlg.setVisible(true);
    }

    private void xoa() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Chọn học kỳ cần xóa!"); return; }
        String ma = (String) tableModel.getValueAt(row, 1);
        if (JOptionPane.showConfirmDialog(this, "Xóa học kỳ \"" + ma + "\"?", "Xác nhận",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            if (hkDAO.delete(ma)) { loadData(); JOptionPane.showMessageDialog(this, "Đã xóa!"); }
            else JOptionPane.showMessageDialog(this, "Xóa thất bại! Học kỳ đang có lớp học phần.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}
