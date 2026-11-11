package ui;

import dao.KhoaHocDAO;
import dao.SinhVienDAO;
import model.KhoaHoc;
import model.SinhVien;
import service.TotNghiepService;
import service.TotNghiepService.KetQuaTotNghiep;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Panel xét điều kiện tốt nghiệp tự động
 */
public class TotNghiepPanel extends JPanel {

    private JComboBox<KhoaHoc> cbKhoa;
    private JTextField tfMaSV;
    private JTable tableResult;
    private DefaultTableModel tableModel;
    private JTextArea txtDetail;
    private JLabel lblStatus;

    private KhoaHocDAO khoaDAO;
    private SinhVienDAO svDAO;
    private TotNghiepService totNghiepService;

    public TotNghiepPanel() {
        khoaDAO = new KhoaHocDAO();
        svDAO = new SinhVienDAO();
        totNghiepService = new TotNghiepService();
        initUI();
        loadData();
    }

    private void initUI() {
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ===== PANEL ĐIỀU KHIỂN =====
        JPanel topPanel = new JPanel(new GridBagLayout());
        topPanel.setBorder(BorderFactory.createTitledBorder("Xét điều kiện tốt nghiệp"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        topPanel.add(new JLabel("Khóa học:"), gbc);
        gbc.gridx = 1;
        cbKhoa = new JComboBox<>();
        cbKhoa.setPreferredSize(new Dimension(200, 26));
        topPanel.add(cbKhoa, gbc);

        gbc.gridx = 2;
        topPanel.add(new JLabel("Mã SV (để trống = xét cả lớp):"), gbc);
        gbc.gridx = 3;
        tfMaSV = new JTextField(12);
        topPanel.add(tfMaSV, gbc);

        gbc.gridx = 4;
        JButton btnXet = new JButton("🎓 Xét tốt nghiệp");
        btnXet.setBackground(new Color(46, 125, 50));
        btnXet.setForeground(Color.WHITE);
        btnXet.setFont(new Font("Arial", Font.BOLD, 13));
        btnXet.addActionListener(e -> xetTotNghiep());
        topPanel.add(btnXet, gbc);

        gbc.gridx = 5;
        JButton btnXetTatCa = new JButton("Xét cả khóa");
        btnXetTatCa.addActionListener(e -> xetTatCaKhoa());
        topPanel.add(btnXetTatCa, gbc);

        add(topPanel, BorderLayout.NORTH);

        // ===== SPLIT PANE =====
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(650);

        // Bảng kết quả
        String[] cols = {"STT", "Mã SV", "Họ và tên", "GPA", "TC Tích lũy", "Kết quả", "Xếp loại"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tableResult = new JTable(tableModel);
        tableResult.setRowHeight(26);
        // Tô màu
        tableResult.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                Object ketQua = table.getValueAt(row, 5);
                if (!isSelected) {
                    if ("✅ Đủ điều kiện".equals(ketQua)) {
                        c.setBackground(new Color(200, 255, 200));
                    } else {
                        c.setBackground(new Color(255, 200, 200));
                    }
                }
                return c;
            }
        });
        tableResult.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) hienThiChiTiet();
        });

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBorder(BorderFactory.createTitledBorder("Kết quả xét tốt nghiệp"));
        leftPanel.add(new JScrollPane(tableResult));

        // Status
        lblStatus = new JLabel("Chưa xét");
        lblStatus.setFont(new Font("Arial", Font.BOLD, 12));
        lblStatus.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        leftPanel.add(lblStatus, BorderLayout.SOUTH);
        splitPane.setLeftComponent(leftPanel);

        // Chi tiết
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(BorderFactory.createTitledBorder("Chi tiết xét tốt nghiệp"));
        txtDetail = new JTextArea();
        txtDetail.setEditable(false);
        txtDetail.setFont(new Font("Monospaced", Font.PLAIN, 13));
        txtDetail.setBackground(new Color(250, 250, 250));
        rightPanel.add(new JScrollPane(txtDetail));
        splitPane.setRightComponent(rightPanel);

        add(splitPane, BorderLayout.CENTER);
    }

    private void loadData() {
        cbKhoa.removeAllItems();
        for (KhoaHoc kh : khoaDAO.getAll()) cbKhoa.addItem(kh);
    }

    private void xetTotNghiep() {
        KhoaHoc khoa = (KhoaHoc) cbKhoa.getSelectedItem();
        if (khoa == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn khóa học!", "Lỗi", JOptionPane.WARNING_MESSAGE);
            return;
        }

        tableModel.setRowCount(0);
        String maSV = tfMaSV.getText().trim();

        if (!maSV.isEmpty()) {
            // Xét 1 SV
            xetMotSV(maSV, khoa.getMaKhoa());
        } else {
            // Xét tất cả SV trong khóa
            List<SinhVien> svList = svDAO.searchSinhVien(null, null, khoa.getMaKhoa());
            for (SinhVien sv : svList) {
                xetMotSV(sv.getMaSV(), khoa.getMaKhoa());
            }
        }

        lblStatus.setText("Đã xét " + tableModel.getRowCount() + " sinh viên");
    }

    private void xetMotSV(String maSV, String maKhoa) {
        KetQuaTotNghiep kq = totNghiepService.xetTotNghiep(maSV, maKhoa);
        SinhVien sv = svDAO.getSinhVienById(maSV);
        String hoTen = sv != null ? sv.getHoTen() : "N/A";
        String ketQua = kq.isDuDieuKien() ? "✅ Đủ điều kiện" : "❌ Chưa đủ";

        tableModel.addRow(new Object[]{
            tableModel.getRowCount() + 1,
            maSV, hoTen,
            String.format("%.2f", kq.getGpa()),
            kq.getTongTinChiTichLuy(),
            ketQua,
            kq.isDuDieuKien() ? kq.getXepLoai() : "--"
        });
    }

    private void xetTatCaKhoa() {
        tfMaSV.setText("");
        xetTotNghiep();
    }

    private void hienThiChiTiet() {
        int row = tableResult.getSelectedRow();
        if (row < 0) return;

        String maSV = (String) tableModel.getValueAt(row, 1);
        KhoaHoc khoa = (KhoaHoc) cbKhoa.getSelectedItem();
        if (khoa == null) return;

        KetQuaTotNghiep kq = totNghiepService.xetTotNghiep(maSV, khoa.getMaKhoa());
        txtDetail.setText(kq.toString());
    }
}
