package ui;

import dao.MonHocDAO;
import model.MonHoc;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class MonHocPanel extends JPanel {

    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField tfTimKiem;
    private MonHocDAO mhDAO;

    public MonHocPanel() {
        mhDAO = new MonHocDAO();
        initUI();
        loadData();
    }

    private void initUI() {
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));
        topPanel.setBorder(BorderFactory.createTitledBorder("Tìm kiếm môn học"));
        topPanel.add(new JLabel("Từ khóa:"));
        tfTimKiem = new JTextField(18);
        topPanel.add(tfTimKiem);
        JButton btnSearch = new JButton("🔍 Tìm");
        btnSearch.setBackground(new Color(52, 152, 219)); btnSearch.setForeground(Color.WHITE);
        btnSearch.addActionListener(e -> search());
        topPanel.add(btnSearch);
        JButton btnAll = new JButton("🔄 Tất cả");
        btnAll.addActionListener(e -> { tfTimKiem.setText(""); loadData(); });
        topPanel.add(btnAll);
        add(topPanel, BorderLayout.NORTH);

        String[] cols = {"STT", "Mã MH", "Tên môn học", "Số TC", "Loại", "Mô tả"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(26);
        table.getColumnModel().getColumn(0).setPreferredWidth(40);
        table.getColumnModel().getColumn(1).setPreferredWidth(80);
        table.getColumnModel().getColumn(2).setPreferredWidth(250);
        JPanel center = new JPanel(new BorderLayout());
        center.setBorder(BorderFactory.createTitledBorder("Danh sách môn học"));
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
            if (row < 0) { JOptionPane.showMessageDialog(this, "Chọn môn học cần sửa!"); return; }
            showForm(mhDAO.getById((String) tableModel.getValueAt(row, 1)));
        });

        JButton btnXoa = new JButton("🗑 Xóa");
        btnXoa.setBackground(new Color(211, 47, 47)); btnXoa.setForeground(Color.WHITE);
        btnXoa.addActionListener(e -> xoa());

        btnPanel.add(btnThem); btnPanel.add(btnSua); btnPanel.add(btnXoa);
        add(btnPanel, BorderLayout.SOUTH);
    }

    private void loadData() {
        tableModel.setRowCount(0);
        int stt = 1;
        for (MonHoc mh : mhDAO.getAll()) {
            String loaiDisplay = "BatBuoc".equals(mh.getLoai()) ? "Bắt buộc" : "Tự chọn";
            tableModel.addRow(new Object[]{stt++, mh.getMaMH(), mh.getTenMH(), mh.getSoTinChi(), loaiDisplay, mh.getMoTa()});
        }
    }

    private void search() {
        String kw = tfTimKiem.getText().trim().toLowerCase();
        tableModel.setRowCount(0);
        int stt = 1;
        for (MonHoc mh : mhDAO.getAll()) {
            if (kw.isEmpty() || mh.getMaMH().toLowerCase().contains(kw) || mh.getTenMH().toLowerCase().contains(kw)) {
                String loaiDisplay = "BatBuoc".equals(mh.getLoai()) ? "Bắt buộc" : "Tự chọn";
                tableModel.addRow(new Object[]{stt++, mh.getMaMH(), mh.getTenMH(), mh.getSoTinChi(), loaiDisplay, mh.getMoTa()});
            }
        }
    }

    private void showForm(MonHoc mh) {
        boolean isEdit = mh != null;
        JDialog dlg = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
            isEdit ? "Sửa môn học" : "Thêm môn học", true);
        dlg.setSize(420, 320);
        dlg.setLocationRelativeTo(this);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; form.add(new JLabel("Mã MH *:"), gbc);
        gbc.gridx = 1; JTextField tfMa = new JTextField(isEdit ? mh.getMaMH() : "", 15);
        tfMa.setEditable(!isEdit); form.add(tfMa, gbc);

        gbc.gridx = 0; gbc.gridy = 1; form.add(new JLabel("Tên môn học *:"), gbc);
        gbc.gridx = 1; JTextField tfTen = new JTextField(isEdit ? mh.getTenMH() : "", 15); form.add(tfTen, gbc);

        gbc.gridx = 0; gbc.gridy = 2; form.add(new JLabel("Số tín chỉ:"), gbc);
        gbc.gridx = 1; JSpinner spTC = new JSpinner(new SpinnerNumberModel(isEdit ? mh.getSoTinChi() : 3, 1, 10, 1));
        form.add(spTC, gbc);

        gbc.gridx = 0; gbc.gridy = 3; form.add(new JLabel("Loại:"), gbc);
        gbc.gridx = 1; JComboBox<String> cbLoai = new JComboBox<>(new String[]{"BatBuoc", "TuChon"});
        if (isEdit) cbLoai.setSelectedItem(mh.getLoai()); form.add(cbLoai, gbc);

        gbc.gridx = 0; gbc.gridy = 4; form.add(new JLabel("Mô tả:"), gbc);
        gbc.gridx = 1; JTextField tfMoTa = new JTextField(isEdit ? mh.getMoTa() : "", 15); form.add(tfMoTa, gbc);

        dlg.add(form, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 8));
        JButton btnSave = new JButton(isEdit ? "💾 Cập nhật" : "💾 Thêm mới");
        btnSave.setBackground(new Color(46, 125, 50)); btnSave.setForeground(Color.WHITE);
        btnSave.addActionListener(e -> {
            String ma = tfMa.getText().trim(), ten = tfTen.getText().trim();
            if (ma.isEmpty() || ten.isEmpty()) {
                JOptionPane.showMessageDialog(dlg, "Mã và Tên không được trống!", "Lỗi", JOptionPane.ERROR_MESSAGE); return;
            }
            MonHoc newMH = new MonHoc(ma, ten, (int) spTC.getValue(), (String) cbLoai.getSelectedItem(), tfMoTa.getText().trim());
            boolean ok = isEdit ? mhDAO.update(newMH) : mhDAO.insert(newMH);
            if (ok) { dlg.dispose(); loadData(); JOptionPane.showMessageDialog(this, "Thành công!"); }
            else JOptionPane.showMessageDialog(dlg, "Thao tác thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        });
        JButton btnCancel = new JButton("Hủy");
        btnCancel.addActionListener(e -> dlg.dispose());
        btnPanel.add(btnSave); btnPanel.add(btnCancel);
        dlg.add(btnPanel, BorderLayout.SOUTH);
        dlg.setVisible(true);
    }

    private void xoa() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Chọn môn học cần xóa!"); return; }
        String ma = (String) tableModel.getValueAt(row, 1);
        String ten = (String) tableModel.getValueAt(row, 2);
        if (JOptionPane.showConfirmDialog(this, "Xóa môn \"" + ten + "\"?", "Xác nhận",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            if (mhDAO.delete(ma)) { loadData(); JOptionPane.showMessageDialog(this, "Đã xóa!"); }
            else JOptionPane.showMessageDialog(this, "Xóa thất bại! Môn học đang được sử dụng.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}
