package ui;

import dao.KhoaHocDAO;
import dao.LopHocDAO;
import dao.SinhVienDAO;
import model.KhoaHoc;
import model.LopHoc;
import model.SinhVien;
import service.GPAService;
import dao.DiemDAO;
import model.DiemMonHoc;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Map;

/**
 * Panel xem GPA, bảng điểm tổng kết sinh viên
 */
public class GPAPanel extends JPanel {

    private JComboBox<KhoaHoc> cbKhoa;
    private JComboBox<LopHoc> cbLop;
    private JTextField tfMaSV;
    private JTable tableTongKet;
    private DefaultTableModel modelTongKet;
    private JTable tableBangDiem;
    private DefaultTableModel modelBangDiem;
    private JLabel lblGPA, lblXepLoai, lblTinChi;

    private KhoaHocDAO khoaDAO;
    private LopHocDAO lopDAO;
    private SinhVienDAO svDAO;
    private DiemDAO diemDAO;
    private GPAService gpaService;

    public GPAPanel() {
        khoaDAO = new KhoaHocDAO();
        lopDAO = new LopHocDAO();
        svDAO = new SinhVienDAO();
        diemDAO = new DiemDAO();
        gpaService = new GPAService();
        initUI();
        loadData();
    }

    private void initUI() {
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ===== PANEL TÌM KIẾM =====
        JPanel searchPanel = new JPanel(new GridBagLayout());
        searchPanel.setBorder(BorderFactory.createTitledBorder("Tìm kiếm sinh viên"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        searchPanel.add(new JLabel("Khóa:"), gbc);
        gbc.gridx = 1;
        cbKhoa = new JComboBox<>();
        cbKhoa.setPreferredSize(new Dimension(160, 26));
        searchPanel.add(cbKhoa, gbc);

        gbc.gridx = 2;
        searchPanel.add(new JLabel("Lớp:"), gbc);
        gbc.gridx = 3;
        cbLop = new JComboBox<>();
        cbLop.setPreferredSize(new Dimension(160, 26));
        searchPanel.add(cbLop, gbc);

        gbc.gridx = 4;
        searchPanel.add(new JLabel("Mã SV:"), gbc);
        gbc.gridx = 5;
        tfMaSV = new JTextField(10);
        searchPanel.add(tfMaSV, gbc);

        gbc.gridx = 6;
        JButton btnTimKiem = new JButton("🔍 Tìm kiếm");
        btnTimKiem.setBackground(new Color(52, 152, 219));
        btnTimKiem.setForeground(Color.WHITE);
        btnTimKiem.addActionListener(e -> timKiem());
        searchPanel.add(btnTimKiem, gbc);

        gbc.gridx = 7;
        JButton btnXemAll = new JButton("Xem tất cả lớp");
        btnXemAll.addActionListener(e -> xemTatCaLop());
        searchPanel.add(btnXemAll, gbc);

        add(searchPanel, BorderLayout.NORTH);

        // ===== SPLIT PANE =====
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setDividerLocation(250);

        // Bảng tổng kết lớp (trên)
        String[] colsTongKet = {"STT", "Mã SV", "Họ và tên", "Lớp", "GPA Tích lũy", "Xếp loại", "TC Tích lũy"};
        modelTongKet = new DefaultTableModel(colsTongKet, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tableTongKet = new JTable(modelTongKet);
        tableTongKet.setRowHeight(24);
        tableTongKet.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) xemBangDiemSV();
        });
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createTitledBorder("Bảng GPA lớp"));
        topPanel.add(new JScrollPane(tableTongKet));
        splitPane.setTopComponent(topPanel);

        // Bảng điểm chi tiết (dưới)
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBorder(BorderFactory.createTitledBorder("Bảng điểm chi tiết sinh viên"));

        // Info GPA
        JPanel gpaInfoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
        gpaInfoPanel.setBackground(new Color(240, 248, 255));
        lblGPA = new JLabel("GPA: --");
        lblGPA.setFont(new Font("Arial", Font.BOLD, 16));
        lblGPA.setForeground(new Color(30, 80, 162));
        lblXepLoai = new JLabel("Xếp loại: --");
        lblXepLoai.setFont(new Font("Arial", Font.BOLD, 14));
        lblTinChi = new JLabel("Tín chỉ tích lũy: --");
        gpaInfoPanel.add(lblGPA);
        gpaInfoPanel.add(new JSeparator(SwingConstants.VERTICAL));
        gpaInfoPanel.add(lblXepLoai);
        gpaInfoPanel.add(new JSeparator(SwingConstants.VERTICAL));
        gpaInfoPanel.add(lblTinChi);
        bottomPanel.add(gpaInfoPanel, BorderLayout.NORTH);

        String[] colsBangDiem = {"STT", "Mã LHP", "Tên môn học", "Điểm TB", "Điểm chữ", "Hệ 4", "Kết quả"};
        modelBangDiem = new DefaultTableModel(colsBangDiem, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tableBangDiem = new JTable(modelBangDiem);
        tableBangDiem.setRowHeight(24);
        // Tô màu dòng không đạt
        tableBangDiem.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                String ketQua = (String) table.getValueAt(row, 6);
                if (!isSelected) {
                    if ("KhongDat".equals(ketQua) || "CamThi".equals(ketQua)) {
                        c.setBackground(new Color(255, 200, 200));
                    } else {
                        c.setBackground(Color.WHITE);
                    }
                }
                return c;
            }
        });
        bottomPanel.add(new JScrollPane(tableBangDiem), BorderLayout.CENTER);
        splitPane.setBottomComponent(bottomPanel);

        add(splitPane, BorderLayout.CENTER);

        // Listener cho khoa -> cập nhật lớp
        cbKhoa.addActionListener(e -> {
            KhoaHoc kh = (KhoaHoc) cbKhoa.getSelectedItem();
            cbLop.removeAllItems();
            cbLop.addItem(null);
            if (kh != null) {
                for (LopHoc lop : lopDAO.getByKhoa(kh.getMaKhoa())) {
                    cbLop.addItem(lop);
                }
            }
        });
    }

    private void loadData() {
        cbKhoa.removeAllItems();
        cbKhoa.addItem(null);
        for (KhoaHoc kh : khoaDAO.getAll()) cbKhoa.addItem(kh);
        cbLop.removeAllItems();
        cbLop.addItem(null);
    }

    private void timKiem() {
        modelTongKet.setRowCount(0);
        String keyword = tfMaSV.getText().trim();
        LopHoc lop = (LopHoc) cbLop.getSelectedItem();
        KhoaHoc khoa = (KhoaHoc) cbKhoa.getSelectedItem();

        List<SinhVien> svList = svDAO.searchSinhVien(
            keyword,
            lop != null ? lop.getMaLop() : null,
            khoa != null ? khoa.getMaKhoa() : null
        );

        int stt = 1;
        for (SinhVien sv : svList) {
            double gpa = gpaService.tinhGPA(sv.getMaSV());
            String xepLoai = gpaService.xepLoaiHocLuc(gpa);
            int tinChi = gpaService.tongTinChiTichLuy(sv.getMaSV());
            modelTongKet.addRow(new Object[]{
                stt++, sv.getMaSV(), sv.getHoTen(), sv.getTenLop(),
                String.format("%.2f", gpa), xepLoai, tinChi
            });
        }
    }

    private void xemTatCaLop() {
        cbKhoa.setSelectedItem(null);
        cbLop.setSelectedItem(null);
        tfMaSV.setText("");
        timKiem();
    }

    private void xemBangDiemSV() {
        int row = tableTongKet.getSelectedRow();
        if (row < 0) return;

        String maSV = (String) modelTongKet.getValueAt(row, 1);
        double gpa = gpaService.tinhGPA(maSV);
        int tinChi = gpaService.tongTinChiTichLuy(maSV);

        lblGPA.setText(String.format("GPA: %.2f", gpa));
        lblXepLoai.setText("Xếp loại: " + gpaService.xepLoaiHocLuc(gpa));
        lblTinChi.setText("Tín chỉ tích lũy: " + tinChi);

        modelBangDiem.setRowCount(0);
        List<DiemMonHoc> bangDiem = diemDAO.getDiemBangSinhVien(maSV);
        int stt = 1;
        for (DiemMonHoc d : bangDiem) {
            String ketQua = d.getKetQua();
            String ketQuaDisplay = "Dat".equals(ketQua) ? "Đạt" :
                                   "KhongDat".equals(ketQua) ? "Không đạt" : "Cấm thi";
            modelBangDiem.addRow(new Object[]{
                stt++, d.getMaLHP(), d.getTenMH(),
                String.format("%.2f", d.getDiemTrungBinh()),
                d.getDiemChu(),
                String.format("%.1f", d.getDiemHe4()),
                ketQua
            });
        }
    }
}
