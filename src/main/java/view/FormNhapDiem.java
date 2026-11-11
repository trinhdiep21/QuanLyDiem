package view;

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
import javax.swing.table.DefaultTableModel;

public class FormNhapDiem extends javax.swing.JFrame {

    private DiemDAO diemDAO;
    private LopHocPhanDAO lhpDAO;
    private HocKyDAO hkDAO;
    private SinhVienDAO svDAO;
    private DiemService diemService;
    private ExcelService excelService;
    private java.util.List<CotDiem> currentCotDiems = new java.util.ArrayList<>();
    private boolean isKhoaSo = false;
    private DefaultTableModel modelTable;

    public FormNhapDiem() {
        diemDAO = new DiemDAO();
        lhpDAO = new LopHocPhanDAO();
        hkDAO = new HocKyDAO();
        svDAO = new SinhVienDAO();
        diemService = new DiemService();
        excelService = new ExcelService();
        initComponents();
        loadCombos();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        panelTop = new javax.swing.JPanel();
        lblHocKy = new javax.swing.JLabel();
        cboHocKy = new javax.swing.JComboBox<>();
        lblLHP = new javax.swing.JLabel();
        cboLHP = new javax.swing.JComboBox<>();
        lblStatus = new javax.swing.JLabel();
        scrollPane = new javax.swing.JScrollPane();
        tblDiem = new javax.swing.JTable();
        panelBottom = new javax.swing.JPanel();
        btnLuuDiem = new javax.swing.JButton();
        btnTinhDiem = new javax.swing.JButton();
        btnExportExcel = new javax.swing.JButton();
        btnImportExcel = new javax.swing.JButton();
        btnTaiMau = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Quản Lý Điểm & Import/Export Excel");
        setPreferredSize(new java.awt.Dimension(1050, 700));

        panelTop.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 10, 10));

        lblHocKy.setText("Học kỳ:");
        panelTop.add(lblHocKy);

        cboHocKy.setPreferredSize(new java.awt.Dimension(200, 25));
        cboHocKy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboHocKyActionPerformed(evt);
            }
        });
        panelTop.add(cboHocKy);

        lblLHP.setText("Lớp học phần:");
        panelTop.add(lblLHP);

        cboLHP.setPreferredSize(new java.awt.Dimension(250, 25));
        cboLHP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboLHPActionPerformed(evt);
            }
        });
        panelTop.add(cboLHP);

        lblStatus.setText("Trạng thái");
        lblStatus.setForeground(new java.awt.Color(0, 120, 0));
        panelTop.add(lblStatus);

        getContentPane().add(panelTop, java.awt.BorderLayout.PAGE_START);

        modelTable = new DefaultTableModel(new Object[]{}, 0);
        tblDiem.setModel(modelTable);
        tblDiem.setRowHeight(24);
        scrollPane.setViewportView(tblDiem);
        scrollPane.setBorder(javax.swing.BorderFactory.createTitledBorder("Bảng điểm chi tiết sinh viên (Nhập trực tiếp điểm vào các cột điểm)"));

        getContentPane().add(scrollPane, java.awt.BorderLayout.CENTER);

        panelBottom.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 10, 10));

        btnLuuDiem.setText("Lưu điểm nhập");
        btnLuuDiem.setBackground(new java.awt.Color(40, 167, 69));
        btnLuuDiem.setForeground(java.awt.Color.WHITE);
        btnLuuDiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLuuDiemActionPerformed(evt);
            }
        });
        panelBottom.add(btnLuuDiem);

        btnTinhDiem.setText("Tính Điểm TB & Quy Đổi");
        btnTinhDiem.setBackground(new java.awt.Color(0, 123, 255));
        btnTinhDiem.setForeground(java.awt.Color.WHITE);
        btnTinhDiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTinhDiemActionPerformed(evt);
            }
        });
        panelBottom.add(btnTinhDiem);

        btnExportExcel.setText("Xuất file Excel");
        btnExportExcel.setBackground(new java.awt.Color(0, 128, 128));
        btnExportExcel.setForeground(java.awt.Color.WHITE);
        btnExportExcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExportExcelActionPerformed(evt);
            }
        });
        panelBottom.add(btnExportExcel);

        btnImportExcel.setText("Nhập từ Excel");
        btnImportExcel.setBackground(new java.awt.Color(255, 165, 0));
        btnImportExcel.setForeground(java.awt.Color.WHITE);
        btnImportExcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImportExcelActionPerformed(evt);
            }
        });
        panelBottom.add(btnImportExcel);

        btnTaiMau.setText("Tải mẫu Excel");
        btnTaiMau.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTaiMauActionPerformed(evt);
            }
        });
        panelBottom.add(btnTaiMau);

        getContentPane().add(panelBottom, java.awt.BorderLayout.PAGE_END);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>

    private void loadCombos() {
        cboHocKy.removeAllItems();
        for (HocKy hk : hkDAO.getAll()) cboHocKy.addItem(hk);
        onHocKyChanged();
    }

    private void cboHocKyActionPerformed(java.awt.event.ActionEvent evt) {
        onHocKyChanged();
    }

    private void onHocKyChanged() {
        cboLHP.removeAllItems();
        HocKy hk = (HocKy) cboHocKy.getSelectedItem();
        if (hk != null) {
            for (LopHocPhan lhp : lhpDAO.getByHocKy(hk.getMaHK())) cboLHP.addItem(lhp);
        }
        loadBangDiem();
    }

    private void cboLHPActionPerformed(java.awt.event.ActionEvent evt) {
        loadBangDiem();
    }

    private void loadBangDiem() {
        LopHocPhan lhp = (LopHocPhan) cboLHP.getSelectedItem();
        HocKy hk = (HocKy) cboHocKy.getSelectedItem();
        if (lhp == null || hk == null) {
            modelTable.setDataVector(new Object[][]{}, new Object[]{});
            return;
        }
        isKhoaSo = "DaDong".equals(hk.getTrangThai());
        if (isKhoaSo) {
            lblStatus.setText("KỲ HỌC ĐÃ ĐÓNG SỔ (KHÔNG CHO PHÉP SỬA ĐIỂM)");
            lblStatus.setForeground(java.awt.Color.RED);
            btnLuuDiem.setEnabled(false);
            btnImportExcel.setEnabled(false);
        } else {
            lblStatus.setText("Kỳ học đang mở (Cho phép nhập và sửa điểm)");
            lblStatus.setForeground(new java.awt.Color(0, 120, 0));
            btnLuuDiem.setEnabled(true);
            btnImportExcel.setEnabled(true);
        }
        currentCotDiems = diemDAO.getCotDiemByLHP(lhp.getMaLHP());
        java.util.List<SinhVien> svList = svDAO.getSinhVienByLHP(lhp.getMaLHP());
        java.util.List<String> headers = new java.util.ArrayList<>();
        headers.add("STT");
        headers.add("Mã SV");
        headers.add("Họ và tên");
        for (CotDiem cd : currentCotDiems) {
            headers.add(cd.getTenCot() + " (" + (int) cd.getTrongSo() + "%)");
        }
        headers.add("Điểm TB (10)");
        headers.add("Điểm Chữ");
        headers.add("Hệ 4");
        headers.add("Kết quả");
        modelTable = new javax.swing.table.DefaultTableModel(headers.toArray(), 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                if (isKhoaSo) return false;
                int start = 3;
                int end = 2 + currentCotDiems.size();
                return col >= start && col <= end;
            }
        };
        tblDiem.setModel(modelTable);
        int stt = 1;
        for (SinhVien sv : svList) {
            java.util.List<Object> row = new java.util.ArrayList<>();
            row.add(stt++);
            row.add(sv.getMaSV());
            row.add(sv.getHoTen());
            for (CotDiem cd : currentCotDiems) {
                DiemThanhPhan dtp = diemDAO.getDiemThanhPhan(sv.getMaSV(), cd.getId());
                row.add(dtp != null ? dtp.getDiem() : "");
            }
            java.util.List<DiemMonHoc> dmhList = diemDAO.getDiemMonHocByLHP(lhp.getMaLHP());
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
            modelTable.addRow(row.toArray());
        }
    }

    private void btnLuuDiemActionPerformed(java.awt.event.ActionEvent evt) {
        if (isKhoaSo) {
            javax.swing.JOptionPane.showMessageDialog(this, "Kỳ học đã đóng sổ, không thể chỉnh sửa điểm!");
            return;
        }
        if (tblDiem.isEditing()) tblDiem.getCellEditor().stopCellEditing();
        int count = 0;
        for (int i = 0; i < modelTable.getRowCount(); i++) {
            String maSV = modelTable.getValueAt(i, 1).toString();
            for (int j = 0; j < currentCotDiems.size(); j++) {
                Object val = modelTable.getValueAt(i, 3 + j);
                if (val != null && !val.toString().trim().isEmpty()) {
                    try {
                        double d = Double.parseDouble(val.toString().trim());
                        if (d >= 0 && d <= 10) {
                            diemDAO.upsertDiemThanhPhan(maSV, currentCotDiems.get(j).getId(), d);
                            count++;
                        }
                    } catch (Exception ignored) {}
                }
            }
        }
        javax.swing.JOptionPane.showMessageDialog(this, "Đã lưu " + count + " cột điểm thành công!");
    }

    private void btnTinhDiemActionPerformed(java.awt.event.ActionEvent evt) {
        LopHocPhan lhp = (LopHocPhan) cboLHP.getSelectedItem();
        if (lhp == null) return;
        btnLuuDiemActionPerformed(evt);
        java.util.List<SinhVien> svList = svDAO.getSinhVienByLHP(lhp.getMaLHP());
        java.util.List<String> listMaSV = new java.util.ArrayList<>();
        for (SinhVien sv : svList) listMaSV.add(sv.getMaSV());
        int count = diemService.tinhDiemToLHP(lhp.getMaLHP(), listMaSV);
        loadBangDiem();
        javax.swing.JOptionPane.showMessageDialog(this, "Đã tính điểm theo trọng số và quy đổi GPA hệ 4 cho " + count + " sinh viên!");
    }

    private void btnExportExcelActionPerformed(java.awt.event.ActionEvent evt) {
        LopHocPhan lhp = (LopHocPhan) cboLHP.getSelectedItem();
        if (lhp == null) return;
        javax.swing.JFileChooser fc = new javax.swing.JFileChooser();
        fc.setSelectedFile(new java.io.File("BangDiem_" + lhp.getMaLHP() + ".xlsx"));
        if (fc.showSaveDialog(this) == javax.swing.JFileChooser.APPROVE_OPTION) {
            try {
                excelService.exportBangDiem(lhp.getMaLHP(), fc.getSelectedFile().getAbsolutePath());
                javax.swing.JOptionPane.showMessageDialog(this, "Xuất file Excel thành công!");
            } catch (Exception e) {
                javax.swing.JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage());
            }
        }
    }

    private void btnImportExcelActionPerformed(java.awt.event.ActionEvent evt) {
        if (isKhoaSo) {
            javax.swing.JOptionPane.showMessageDialog(this, "Kỳ học đã đóng sổ, không được phép import điểm!");
            return;
        }
        LopHocPhan lhp = (LopHocPhan) cboLHP.getSelectedItem();
        if (lhp == null) return;
        javax.swing.JFileChooser fc = new javax.swing.JFileChooser();
        if (fc.showOpenDialog(this) == javax.swing.JFileChooser.APPROVE_OPTION) {
            try {
                int count = excelService.importBangDiem(lhp.getMaLHP(), fc.getSelectedFile().getAbsolutePath());
                loadBangDiem();
                javax.swing.JOptionPane.showMessageDialog(this, "Đã import thành công " + count + " điểm từ Excel!");
            } catch (Exception e) {
                javax.swing.JOptionPane.showMessageDialog(this, "Lỗi import: " + e.getMessage());
            }
        }
    }

    private void btnTaiMauActionPerformed(java.awt.event.ActionEvent evt) {
        LopHocPhan lhp = (LopHocPhan) cboLHP.getSelectedItem();
        if (lhp == null) return;
        javax.swing.JFileChooser fc = new javax.swing.JFileChooser();
        fc.setSelectedFile(new java.io.File("Mau_NhapDiem_" + lhp.getMaLHP() + ".xlsx"));
        if (fc.showSaveDialog(this) == javax.swing.JFileChooser.APPROVE_OPTION) {
            try {
                excelService.exportTemplate(lhp.getMaLHP(), fc.getSelectedFile().getAbsolutePath());
                javax.swing.JOptionPane.showMessageDialog(this, "Đã tải file mẫu Excel thành công!");
            } catch (Exception e) {
                javax.swing.JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage());
            }
        }
    }

    // Variables declaration - do not modify
    private javax.swing.JButton btnExportExcel;
    private javax.swing.JButton btnImportExcel;
    private javax.swing.JButton btnLuuDiem;
    private javax.swing.JButton btnTaiMau;
    private javax.swing.JButton btnTinhDiem;
    private javax.swing.JComboBox<HocKy> cboHocKy;
    private javax.swing.JComboBox<LopHocPhan> cboLHP;
    private javax.swing.JLabel lblHocKy;
    private javax.swing.JLabel lblLHP;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JPanel panelBottom;
    private javax.swing.JPanel panelTop;
    private javax.swing.JScrollPane scrollPane;
    private javax.swing.JTable tblDiem;
    // End of variables declaration
}
