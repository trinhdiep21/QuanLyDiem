package ui;

import dao.KhoaHocDAO;
import dao.KhungCTDTDAO;
import dao.MonHocDAO;
import model.KhoaHoc;
import model.KhungCTDT;
import model.MonHoc;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Panel quản lý Khung Chương trình Đào tạo
 */
public class KhungCTDTPanel extends JPanel {

    private JComboBox<KhoaHoc> cbKhoa;
    private JTable tableKhung;
    private DefaultTableModel tableModel;
    private JLabel lblTongBatBuoc, lblTongTuChon;

    private KhoaHocDAO khoaDAO;
    private KhungCTDTDAO khungDAO;
    private MonHocDAO mhDAO;

    public KhungCTDTPanel() {
        khoaDAO = new KhoaHocDAO();
        khungDAO = new KhungCTDTDAO();
        mhDAO = new MonHocDAO();
        initUI();
        loadKhoa();
    }

    private void initUI() {
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ===== TOP: Filter =====
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        topPanel.setBorder(BorderFactory.createTitledBorder("Khung Chương trình Đào tạo"));

        topPanel.add(new JLabel("Khóa học:"));
        cbKhoa = new JComboBox<>();
        cbKhoa.setPreferredSize(new Dimension(200, 26));
        cbKhoa.addActionListener(e -> loadKhungCTDT());
        topPanel.add(cbKhoa);

        JButton btnThem = new JButton("➕ Thêm môn");
        btnThem.setBackground(new Color(46, 125, 50));
        btnThem.setForeground(Color.WHITE);
        btnThem.addActionListener(e -> themMonVaoKhung());
        topPanel.add(btnThem);

        JButton btnXoa = new JButton("🗑 Xóa môn");
        btnXoa.setBackground(new Color(200, 50, 50));
        btnXoa.setForeground(Color.WHITE);
        btnXoa.addActionListener(e -> xoaMonKhoiKhung());
        topPanel.add(btnXoa);

        add(topPanel, BorderLayout.NORTH);

        // ===== CENTER: Table =====
        String[] cols = {"STT", "Mã môn", "Tên môn học", "Số TC", "Loại"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tableKhung = new JTable(tableModel);
        tableKhung.setRowHeight(26);

        // Tô màu phân biệt bắt buộc / tự chọn
        tableKhung.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                Object loai = table.getValueAt(row, 4);
                if (!isSelected) {
                    if ("Bắt buộc".equals(loai)) {
                        c.setBackground(new Color(255, 243, 224));
                    } else {
                        c.setBackground(new Color(232, 245, 233));
                    }
                }
                return c;
            }
        });

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("Danh sách môn học trong khung"));
        tablePanel.add(new JScrollPane(tableKhung));
        add(tablePanel, BorderLayout.CENTER);

        // ===== BOTTOM: Tổng kết =====
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
        bottomPanel.setBackground(new Color(240, 248, 255));
        bottomPanel.setBorder(BorderFactory.createEtchedBorder());
        lblTongBatBuoc = new JLabel("Tổng TC bắt buộc: 0");
        lblTongBatBuoc.setFont(new Font("Arial", Font.BOLD, 13));
        lblTongBatBuoc.setForeground(new Color(200, 100, 0));
        lblTongTuChon = new JLabel("Tổng TC tự chọn: 0");
        lblTongTuChon.setFont(new Font("Arial", Font.BOLD, 13));
        lblTongTuChon.setForeground(new Color(46, 125, 50));
        bottomPanel.add(lblTongBatBuoc);
        bottomPanel.add(new JSeparator(SwingConstants.VERTICAL));
        bottomPanel.add(lblTongTuChon);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void loadKhoa() {
        cbKhoa.removeAllItems();
        for (KhoaHoc kh : khoaDAO.getAll()) cbKhoa.addItem(kh);
    }

    private void loadKhungCTDT() {
        tableModel.setRowCount(0);
        KhoaHoc kh = (KhoaHoc) cbKhoa.getSelectedItem();
        if (kh == null) return;

        List<KhungCTDT> list = khungDAO.getByKhoa(kh.getMaKhoa());
        int stt = 1;
        int tongBatBuoc = 0, tongTuChon = 0;

        for (KhungCTDT kc : list) {
            String loaiDisplay = "BatBuoc".equals(kc.getLoai()) ? "Bắt buộc" : "Tự chọn";
            tableModel.addRow(new Object[]{
                stt++, kc.getMaMH(), kc.getTenMH(), kc.getSoTinChi(), loaiDisplay
            });
            if ("BatBuoc".equals(kc.getLoai())) tongBatBuoc += kc.getSoTinChi();
            else tongTuChon += kc.getSoTinChi();
        }

        lblTongBatBuoc.setText("Tổng TC bắt buộc: " + tongBatBuoc);
        lblTongTuChon.setText("Tổng TC tự chọn: " + tongTuChon);
    }

    private void themMonVaoKhung() {
        KhoaHoc kh = (KhoaHoc) cbKhoa.getSelectedItem();
        if (kh == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn khóa học!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Dialog chọn môn
        JDialog dlg = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Thêm môn vào khung", true);
        dlg.setLayout(new GridBagLayout());
        dlg.setSize(350, 200);
        dlg.setLocationRelativeTo(this);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        dlg.add(new JLabel("Môn học:"), gbc);
        gbc.gridx = 1;
        JComboBox<MonHoc> cbMon = new JComboBox<>();
        for (MonHoc mh : mhDAO.getAll()) cbMon.addItem(mh);
        dlg.add(cbMon, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        dlg.add(new JLabel("Loại:"), gbc);
        gbc.gridx = 1;
        JComboBox<String> cbLoai = new JComboBox<>(new String[]{"BatBuoc", "TuChon"});
        dlg.add(cbLoai, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        JButton btnOk = new JButton("Thêm");
        btnOk.setBackground(new Color(46, 125, 50));
        btnOk.setForeground(Color.WHITE);
        btnOk.addActionListener(ev -> {
            MonHoc mh = (MonHoc) cbMon.getSelectedItem();
            String loai = (String) cbLoai.getSelectedItem();
            if (mh != null) {
                khungDAO.insert(kh.getMaKhoa(), mh.getMaMH(), loai);
                loadKhungCTDT();
                dlg.dispose();
            }
        });
        dlg.add(btnOk, gbc);
        dlg.setVisible(true);
    }

    private void xoaMonKhoiKhung() {
        int row = tableKhung.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Chọn môn cần xóa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String tenMH = (String) tableModel.getValueAt(row, 2);
        int confirm = JOptionPane.showConfirmDialog(this,
            "Xóa môn \"" + tenMH + "\" khỏi khung CTĐT?", "Xác nhận xóa",
            JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            // Lấy id từ KhungCTDT
            KhoaHoc kh = (KhoaHoc) cbKhoa.getSelectedItem();
            List<KhungCTDT> list = khungDAO.getByKhoa(kh.getMaKhoa());
            if (row < list.size()) {
                khungDAO.delete(list.get(row).getId());
                loadKhungCTDT();
                JOptionPane.showMessageDialog(this, "Đã xóa thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
}
