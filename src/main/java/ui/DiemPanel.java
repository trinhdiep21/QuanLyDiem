package ui;

import dao.DiemDAO;
import dao.HocKyDAO;
import dao.LopHocPhanDAO;
import dao.SinhVienDAO;
import model.CotDiem;
import model.DiemMonHoc;
import model.DiemThanhPhan;
import model.HocKy;
import model.LopHocPhan;
import model.SinhVien;
import service.DiemService;
import service.ExcelService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DiemPanel extends JPanel {

    private JComboBox<HocKy> cbHocKy;
    private JComboBox<LopHocPhan> cbLHP;
    private JTable table;
    private DefaultTableModel tableModel;
    private JLabel lblStatus;

    private DiemDAO diemDAO;
    private LopHocPhanDAO lhpDAO;
    private HocKyDAO hkDAO;
    private SinhVienDAO svDAO;
    private DiemService diemService;
    private ExcelService excelService;

    private List<CotDiem> currentCotDiems = new ArrayList<>();
    private boolean isKhoaDiem = false;

    public DiemPanel() {
        diemDAO = new DiemDAO();
        lhpDAO = new LopHocPhanDAO();
        hkDAO = new HocKyDAO();
        svDAO = new SinhVienDAO();
        diemService = new DiemService();
        excelService = new ExcelService();
        initUI();
        loadCombos();
    }

    private void initUI() {
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Top Filter Bar
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));
        topPanel.setBorder(BorderFactory.createTitledBorder("Chọn Lớp học phần"));

        topPanel.add(new JLabel("Học kỳ:"));
        cbHocKy = new JComboBox<>();
        cbHocKy.setPreferredSize(new Dimension(170, 26));
        cbHocKy.addActionListener(e -> onHocKyChanged());
        topPanel.add(cbHocKy);

        topPanel.add(new JLabel("Lớp HP:"));
        cbLHP = new JComboBox<>();
        cbLHP.setPreferredSize(new Dimension(240, 26));
        cbLHP.addActionListener(e -> loadBangDiem());
        topPanel.add(cbLHP);

        JButton btnReload = new JButton("🔄 Tải lại");
        btnReload.addActionListener(e -> loadBangDiem());
        topPanel.add(btnReload);

        lblStatus = new JLabel("Trạng thái: Bình thường");
        lblStatus.setFont(new Font("Arial", Font.BOLD, 12));
        topPanel.add(Box.createHorizontalStrut(15));
        topPanel.add(lblStatus);

        add(topPanel, BorderLayout.NORTH);

        // Center Table
        tableModel = new DefaultTableModel();
        table = new JTable(tableModel);
        table.setRowHeight(26);
        table.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createTitledBorder("Bảng điểm chi tiết và tổng kết"));
        centerPanel.add(new JScrollPane(table), BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);

        // Bottom Action Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));

        JButton btnSave = new JButton("💾 Lưu điểm nhập");
        btnSave.setBackground(new Color(46, 125, 50)); btnSave.setForeground(Color.WHITE);
        btnSave.addActionListener(e -> luuDiem());

        JButton btnTinh = new JButton("⚡ Tính điểm & Quy đổi");
        btnTinh.setBackground(new Color(30, 136, 229)); btnTinh.setForeground(Color.WHITE);
        btnTinh.addActionListener(e -> tinhDiem());

        JButton btnExport = new JButton("📤 Xuất Excel");
        btnExport.setBackground(new Color(0, 121, 107)); btnExport.setForeground(Color.WHITE);
        btnExport.addActionListener(e -> exportExcel());

        JButton btnImport = new JButton("📥 Nhập Excel");
        btnImport.setBackground(new Color(230, 81, 0)); btnImport.setForeground(Color.WHITE);
        btnImport.addActionListener(e -> importExcel());

        JButton btnTemplate = new JButton("📄 Tải mẫu Excel");
        btnTemplate.addActionListener(e -> exportTemplate());

        btnPanel.add(btnSave);
        btnPanel.add(btnTinh);
        btnPanel.add(btnExport);
        btnPanel.add(btnImport);
        btnPanel.add(btnTemplate);

        add(btnPanel, BorderLayout.SOUTH);
    }

    private void loadCombos() {
        cbHocKy.removeAllItems();
        for (HocKy hk : hkDAO.getAll()) cbHocKy.addItem(hk);
        onHocKyChanged();
    }

    private void onHocKyChanged() {
        cbLHP.removeAllItems();
        HocKy hk = (HocKy) cbHocKy.getSelectedItem();
        if (hk != null) {
            for (LopHocPhan lhp : lhpDAO.getByHocKy(hk.getMaHK())) cbLHP.addItem(lhp);
        }
        loadBangDiem();
    }

    private void loadBangDiem() {
        LopHocPhan lhp = (LopHocPhan) cbLHP.getSelectedItem();
        HocKy hk = (HocKy) cbHocKy.getSelectedItem();

        if (lhp == null || hk == null) {
            tableModel.setDataVector(new Object[][]{}, new Object[]{});
            lblStatus.setText("Chưa chọn lớp học phần");
            return;
        }

        // Kiểm tra học kỳ đã đóng chưa
        isKhoaDiem = "DaDong".equals(hk.getTrangThai());
        if (isKhoaDiem) {
            lblStatus.setText("🔒 KỲ HỌC ĐÃ ĐÓNG SỔ (KHÔNG CHO PHÉP SỬA ĐIỂM)");
            lblStatus.setForeground(Color.RED);
        } else {
            lblStatus.setText("🔓 Kỳ học đang mở (cho phép nhập điểm)");
            lblStatus.setForeground(new Color(0, 120, 0));
        }

        currentCotDiems = diemDAO.getCotDiemByLHP(lhp.getMaLHP());
        List<SinhVien> svList = svDAO.getSinhVienByLHP(lhp.getMaLHP());

        // Xây dựng headers
        List<String> headers = new ArrayList<>();
        headers.add("STT");
        headers.add("Mã SV");
        headers.add("Họ và tên");
        for (CotDiem cd : currentCotDiems) {
            headers.add(cd.getTenCot() + " (" + (int) cd.getTrongSo() + "%)");
        }
        headers.add("Điểm TB (10)");
        headers.add("Điểm Chữ");
        headers.add("Điểm Hệ 4");
        headers.add("Kết quả");

        tableModel = new DefaultTableModel(headers.toArray(), 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                if (isKhoaDiem) return false;
                // Cột điểm thành phần mới cho phép sửa
                int firstGradeCol = 3;
                int lastGradeCol = 2 + currentCotDiems.size();
                return column >= firstGradeCol && column <= lastGradeCol;
            }
        };
        table.setModel(tableModel);

        int stt = 1;
        for (SinhVien sv : svList) {
            List<Object> row = new ArrayList<>();
            row.add(stt++);
            row.add(sv.getMaSV());
            row.add(sv.getHoTen());

            // Điểm các cột
            for (CotDiem cd : currentCotDiems) {
                DiemThanhPhan dtp = diemDAO.getDiemThanhPhan(sv.getMaSV(), cd.getId());
                row.add(dtp != null ? dtp.getDiem() : "");
            }

            // Điểm tổng kết
            List<DiemMonHoc> dmhList = diemDAO.getDiemMonHocByLHP(lhp.getMaLHP());
            DiemMonHoc matched = dmhList.stream().filter(d -> d.getMaSV().equals(sv.getMaSV())).findFirst().orElse(null);

            if (matched != null) {
                row.add(String.format("%.2f", matched.getDiemTrungBinh()));
                row.add(matched.getDiemChu());
                row.add(String.format("%.1f", matched.getDiemHe4()));
                String kq = "Dat".equals(matched.getKetQua()) ? "Đạt" :
                            "KhongDat".equals(matched.getKetQua()) ? "Không đạt" : "Cấm thi";
                row.add(kq);
            } else {
                row.add("--");
                row.add("--");
                row.add("--");
                row.add("Chưa tính");
            }

            tableModel.addRow(row.toArray());
        }
    }

    private void luuDiem() {
        if (isKhoaDiem) {
            JOptionPane.showMessageDialog(this, "Học kỳ đã đóng sổ, không thể chỉnh sửa điểm!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        LopHocPhan lhp = (LopHocPhan) cbLHP.getSelectedItem();
        if (lhp == null) return;

        if (table.isEditing()) {
            table.getCellEditor().stopCellEditing();
        }

        int count = 0;
        int rowCount = tableModel.getRowCount();
        for (int i = 0; i < rowCount; i++) {
            String maSV = (String) tableModel.getValueAt(i, 1);
            for (int j = 0; j < currentCotDiems.size(); j++) {
                Object val = tableModel.getValueAt(i, 3 + j);
                if (val != null && !val.toString().trim().isEmpty()) {
                    try {
                        double d = Double.parseDouble(val.toString().trim());
                        if (d >= 0 && d <= 10) {
                            diemDAO.upsertDiemThanhPhan(maSV, currentCotDiems.get(j).getId(), d);
                            count++;
                        }
                    } catch (NumberFormatException ignored) {}
                }
            }
        }
        JOptionPane.showMessageDialog(this, "Đã lưu " + count + " cột điểm thành công!");
    }

    private void tinhDiem() {
        LopHocPhan lhp = (LopHocPhan) cbLHP.getSelectedItem();
        if (lhp == null) return;

        // Lưu trước khi tính
        luuDiem();

        List<SinhVien> svList = svDAO.getSinhVienByLHP(lhp.getMaLHP());
        List<String> listMaSV = new ArrayList<>();
        for (SinhVien sv : svList) listMaSV.add(sv.getMaSV());

        int count = diemService.tinhDiemToLHP(lhp.getMaLHP(), listMaSV);
        loadBangDiem();
        JOptionPane.showMessageDialog(this, "Đã tính điểm và quy đổi GPA hệ 4 cho " + count + " sinh viên!");
    }

    private void exportExcel() {
        LopHocPhan lhp = (LopHocPhan) cbLHP.getSelectedItem();
        if (lhp == null) return;

        JFileChooser fc = new JFileChooser();
        fc.setSelectedFile(new File("BangDiem_" + lhp.getMaLHP() + ".xlsx"));
        if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                excelService.exportBangDiem(lhp.getMaLHP(), fc.getSelectedFile().getAbsolutePath());
                JOptionPane.showMessageDialog(this, "Xuất file Excel thành công!\n" + fc.getSelectedFile().getAbsolutePath());
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Lỗi xuất file: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void importExcel() {
        if (isKhoaDiem) {
            JOptionPane.showMessageDialog(this, "Kỳ học đã đóng sổ, không được phép import điểm!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        LopHocPhan lhp = (LopHocPhan) cbLHP.getSelectedItem();
        if (lhp == null) return;

        JFileChooser fc = new JFileChooser();
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                int count = excelService.importBangDiem(lhp.getMaLHP(), fc.getSelectedFile().getAbsolutePath());
                loadBangDiem();
                JOptionPane.showMessageDialog(this, "Đã import thành công " + count + " điểm từ Excel!");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Lỗi đọc file: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void exportTemplate() {
        LopHocPhan lhp = (LopHocPhan) cbLHP.getSelectedItem();
        if (lhp == null) return;

        JFileChooser fc = new JFileChooser();
        fc.setSelectedFile(new File("Mau_NhapDiem_" + lhp.getMaLHP() + ".xlsx"));
        if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                excelService.exportTemplate(lhp.getMaLHP(), fc.getSelectedFile().getAbsolutePath());
                JOptionPane.showMessageDialog(this, "Đã tải file mẫu thành công!\n" + fc.getSelectedFile().getAbsolutePath());
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Lỗi tạo mẫu: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
