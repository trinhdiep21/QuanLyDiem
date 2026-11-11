package ui;

import dao.KhoaHocDAO;
import dao.LopHocDAO;
import model.KhoaHoc;
import model.LopHoc;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class LopHocPanel extends JPanel {

    private JTable table;
    private DefaultTableModel tableModel;
    private JComboBox<KhoaHoc> cbKhoa;
    private LopHocDAO lopDAO;
    private KhoaHocDAO khoaDAO;

    public LopHocPanel() {
        lopDAO = new LopHocDAO();
        khoaDAO = new KhoaHocDAO();
        initUI();
        loadData();
    }

    private void initUI() {
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));
        topPanel.setBorder(BorderFactory.createTitledBorder("Lọc theo khóa"));
        topPanel.add(new JLabel("Khóa:"));
        cbKhoa = new JComboBox<>();
        cbKhoa.setPreferredSize(new Dimension(180, 26));
        cbKhoa.addActionListener(e -> loadData());
        topPanel.add(cbKhoa);
        JButton btnAll = new JButton("🔄 Tất cả");
        btnAll.addActionListener(e -> { cbKhoa.setSelectedIndex(0); loadData(); });
        topPanel.add(btnAll);
        add(topPanel, BorderLayout.NORTH);

        String[] cols = {"STT", "Mã lớp", "Tên lớp", "Mã khóa", "Sĩ số"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(26);
        JPanel center = new JPanel(new BorderLayout());
        center.setBorder(BorderFactory.createTitledBorder("Danh sách lớp học"));
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
            if (row < 0) { JOptionPane.showMessageDialog(this, "Chọn lớp cần sửa!"); return; }
            String maLop = (String) tableModel.getValueAt(row, 1);
            List<LopHoc> all = lopDAO.getAll();
            all.stream().filter(l -> l.getMaLop().equals(maLop)).findFirst().ifPresent(this::showForm);
        });

        JButton btnXoa = new JButton("🗑 Xóa");
        btnXoa.setBackground(new Color(211, 47, 47)); btnXoa.setForeground(Color.WHITE);
        btnXoa.addActionListener(e -> xoa());

        btnPanel.add(btnThem); btnPanel.add(btnSua); btnPanel.add(btnXoa);
        add(btnPanel, BorderLayout.SOUTH);

        // Load khoa
        cbKhoa.addItem(null);
        for (KhoaHoc kh : khoaDAO.getAll()) cbKhoa.addItem(kh);
    }

    private void loadData() {
        tableModel.setRowCount(0);
        KhoaHoc kh = (KhoaHoc) cbKhoa.getSelectedItem();
        List<LopHoc> list = kh != null ? lopDAO.getByKhoa(kh.getMaKhoa()) : lopDAO.getAll();
        int stt = 1;
        for (LopHoc lop : list) {
            tableModel.addRow(new Object[]{stt++, lop.getMaLop(), lop.getTenLop(), lop.getMaKhoa(), lop.getSiSo()});
        }
    }

    private void showForm(LopHoc lop) {
        boolean isEdit = lop != null;
        JDialog dlg = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
            isEdit ? "Sửa lớp học" : "Thêm lớp học", true);
        dlg.setSize(380, 250);
        dlg.setLocationRelativeTo(this);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; form.add(new JLabel("Mã lớp *:"), gbc);
        gbc.gridx = 1; JTextField tfMa = new JTextField(isEdit ? lop.getMaLop() : "", 15);
        tfMa.setEditable(!isEdit); form.add(tfMa, gbc);

        gbc.gridx = 0; gbc.gridy = 1; form.add(new JLabel("Tên lớp *:"), gbc);
        gbc.gridx = 1; JTextField tfTen = new JTextField(isEdit ? lop.getTenLop() : "", 15); form.add(tfTen, gbc);

        gbc.gridx = 0; gbc.gridy = 2; form.add(new JLabel("Khóa học:"), gbc);
        gbc.gridx = 1; JComboBox<KhoaHoc> cbKhoaForm = new JComboBox<>();
        for (KhoaHoc kh : khoaDAO.getAll()) {
            cbKhoaForm.addItem(kh);
            if (isEdit && kh.getMaKhoa().equals(lop.getMaKhoa())) cbKhoaForm.setSelectedItem(kh);
        }
        form.add(cbKhoaForm, gbc);

        gbc.gridx = 0; gbc.gridy = 3; form.add(new JLabel("Sĩ số:"), gbc);
        gbc.gridx = 1; JSpinner spSiSo = new JSpinner(new SpinnerNumberModel(isEdit ? lop.getSiSo() : 40, 0, 200, 1));
        form.add(spSiSo, gbc);

        dlg.add(form, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 8));
        JButton btnSave = new JButton(isEdit ? "💾 Cập nhật" : "💾 Thêm mới");
        btnSave.setBackground(new Color(46, 125, 50)); btnSave.setForeground(Color.WHITE);
        btnSave.addActionListener(e -> {
            String ma = tfMa.getText().trim(), ten = tfTen.getText().trim();
            if (ma.isEmpty() || ten.isEmpty()) {
                JOptionPane.showMessageDialog(dlg, "Mã và Tên không được trống!"); return;
            }
            KhoaHoc selKhoa = (KhoaHoc) cbKhoaForm.getSelectedItem();
            LopHoc newLop = new LopHoc(ma, ten, selKhoa != null ? selKhoa.getMaKhoa() : null, (int) spSiSo.getValue());
            boolean ok = isEdit ? lopDAO.update(newLop) : lopDAO.insert(newLop);
            if (ok) { dlg.dispose(); loadData(); JOptionPane.showMessageDialog(this, "Thành công!"); }
            else JOptionPane.showMessageDialog(dlg, "Thao tác thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        });
        btnPanel.add(btnSave);
        JButton btnCancel = new JButton("Hủy"); btnCancel.addActionListener(e -> dlg.dispose());
        btnPanel.add(btnCancel);
        dlg.add(btnPanel, BorderLayout.SOUTH);
        dlg.setVisible(true);
    }

    private void xoa() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Chọn lớp cần xóa!"); return; }
        String ma = (String) tableModel.getValueAt(row, 1);
        String ten = (String) tableModel.getValueAt(row, 2);
        if (JOptionPane.showConfirmDialog(this, "Xóa lớp \"" + ten + "\"?", "Xác nhận",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            if (lopDAO.delete(ma)) { loadData(); JOptionPane.showMessageDialog(this, "Đã xóa!"); }
            else JOptionPane.showMessageDialog(this, "Xóa thất bại! Lớp đang có sinh viên.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}
