package view;

import dao.KhoaHocDAO;
import dao.LopHocDAO;
import model.KhoaHoc;
import model.LopHoc;

public class FormLopHoc extends javax.swing.JFrame {

    private LopHocDAO lopDAO;
    private KhoaHocDAO khoaDAO;

    public FormLopHoc() {
        lopDAO = new LopHocDAO();
        khoaDAO = new KhoaHocDAO();
        initComponents();
        loadCboKhoa();
        fillTable(lopDAO.getAll());
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblTitle = new javax.swing.JLabel();
        pnlForm = new javax.swing.JPanel();
        lblMaLop = new javax.swing.JLabel();
        txtMaLop = new javax.swing.JTextField();
        lblTenLop = new javax.swing.JLabel();
        txtTenLop = new javax.swing.JTextField();
        lblKhoa = new javax.swing.JLabel();
        cboKhoa = new javax.swing.JComboBox<>();
        lblSiSo = new javax.swing.JLabel();
        spSiSo = new javax.swing.JSpinner();
        pnlActions = new javax.swing.JPanel();
        btnThem = new javax.swing.JButton();
        btnSua = new javax.swing.JButton();
        btnXoa = new javax.swing.JButton();
        btnLamMoi = new javax.swing.JButton();
        scrTable = new javax.swing.JScrollPane();
        tblLopHoc = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Quản Lý Lớp Sinh Hoạt");
        setPreferredSize(new java.awt.Dimension(750, 560));
        getContentPane().setLayout(new java.awt.BorderLayout(5, 5));

        lblTitle.setFont(new java.awt.Font("Tahoma", 1, 20));
        lblTitle.setForeground(new java.awt.Color(25, 118, 210));
        lblTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitle.setText("QUẢN LÝ LỚP SINH HOẠT");
        lblTitle.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 5, 5, 5));
        getContentPane().add(lblTitle, java.awt.BorderLayout.PAGE_START);

        javax.swing.JPanel pnlCenter = new javax.swing.JPanel();
        pnlCenter.setLayout(new java.awt.BorderLayout(5, 5));
        pnlCenter.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 10, 10));

        pnlForm.setBorder(javax.swing.BorderFactory.createTitledBorder("Thông tin lớp học"));
        pnlForm.setLayout(new java.awt.GridLayout(2, 4, 10, 10));

        lblMaLop.setText("Mã lớp:");
        pnlForm.add(lblMaLop);
        pnlForm.add(txtMaLop);

        lblTenLop.setText("Tên lớp:");
        pnlForm.add(lblTenLop);
        pnlForm.add(txtTenLop);

        lblKhoa.setText("Khóa đào tạo:");
        pnlForm.add(lblKhoa);
        pnlForm.add(cboKhoa);

        lblSiSo.setText("Sĩ số dự kiến:");
        pnlForm.add(lblSiSo);
        spSiSo.setModel(new javax.swing.SpinnerNumberModel(40, 1, 200, 1));
        pnlForm.add(spSiSo);

        pnlCenter.add(pnlForm, java.awt.BorderLayout.PAGE_START);

        pnlActions.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 10, 5));

        btnThem.setText("Thêm");
        btnThem.setBackground(new java.awt.Color(76, 175, 80));
        btnThem.setForeground(java.awt.Color.WHITE);
        btnThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemActionPerformed(evt);
            }
        });
        pnlActions.add(btnThem);

        btnSua.setText("Sửa");
        btnSua.setBackground(new java.awt.Color(255, 152, 0));
        btnSua.setForeground(java.awt.Color.WHITE);
        btnSua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaActionPerformed(evt);
            }
        });
        pnlActions.add(btnSua);

        btnXoa.setText("Xóa");
        btnXoa.setBackground(new java.awt.Color(244, 67, 54));
        btnXoa.setForeground(java.awt.Color.WHITE);
        btnXoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaActionPerformed(evt);
            }
        });
        pnlActions.add(btnXoa);

        btnLamMoi.setText("Làm mới");
        btnLamMoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearForm();
            }
        });
        pnlActions.add(btnLamMoi);

        pnlCenter.add(pnlActions, java.awt.BorderLayout.CENTER);

        modelTable = new javax.swing.table.DefaultTableModel(
            new Object[]{"STT", "Mã Lớp", "Tên Lớp", "Mã Khóa", "Sĩ Số"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblLopHoc.setModel(modelTable);
        tblLopHoc.setRowHeight(24);
        tblLopHoc.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblLopHocMouseClicked(evt);
            }
        });
        scrTable.setViewportView(tblLopHoc);
        scrTable.setBorder(javax.swing.BorderFactory.createTitledBorder("Danh sách lớp học"));

        pnlCenter.add(scrTable, java.awt.BorderLayout.PAGE_END);
        scrTable.setPreferredSize(new java.awt.Dimension(700, 300));

        getContentPane().add(pnlCenter, java.awt.BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    // ==================== Business Logic ====================

    private void loadCboKhoa() {
        cboKhoa.removeAllItems();
        cboKhoa.addItem(null);
        for (KhoaHoc k : khoaDAO.getAll()) {
            cboKhoa.addItem(k);
        }
    }

    private void fillTable(java.util.List<LopHoc> list) {
        modelTable.setRowCount(0);
        int stt = 1;
        for (LopHoc lop : list) {
            modelTable.addRow(new Object[]{
                stt++, lop.getMaLop(), lop.getTenLop(), lop.getMaKhoa(), lop.getSiSo()
            });
        }
    }

    private void tblLopHocMouseClicked(java.awt.event.MouseEvent evt) {
        int row = tblLopHoc.getSelectedRow();
        if (row < 0) return;
        txtMaLop.setText(modelTable.getValueAt(row, 1).toString());
        txtMaLop.setEditable(false);
        txtTenLop.setText(modelTable.getValueAt(row, 2).toString());
        String maKhoa = modelTable.getValueAt(row, 3) != null ? modelTable.getValueAt(row, 3).toString() : "";
        for (int i = 0; i < cboKhoa.getItemCount(); i++) {
            KhoaHoc k = cboKhoa.getItemAt(i);
            if (k != null && k.getMaKhoa().equalsIgnoreCase(maKhoa)) {
                cboKhoa.setSelectedIndex(i);
                break;
            }
        }
        spSiSo.setValue(Integer.parseInt(modelTable.getValueAt(row, 4).toString()));
    }

    private void clearForm() {
        txtMaLop.setText("");
        txtMaLop.setEditable(true);
        txtTenLop.setText("");
        cboKhoa.setSelectedIndex(0);
        spSiSo.setValue(40);
        fillTable(lopDAO.getAll());
    }

    private void btnThemActionPerformed(java.awt.event.ActionEvent evt) {
        String ma = txtMaLop.getText().trim();
        String ten = txtTenLop.getText().trim();
        if (ma.isEmpty() || ten.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this, "Vui lòng nhập Mã lớp và Tên lớp!");
            return;
        }
        KhoaHoc k = (KhoaHoc) cboKhoa.getSelectedItem();
        LopHoc lop = new LopHoc(ma, ten, k != null ? k.getMaKhoa() : null, (int) spSiSo.getValue());
        if (lopDAO.insert(lop)) {
            javax.swing.JOptionPane.showMessageDialog(this, "Thêm lớp học thành công!");
            clearForm();
        } else {
            javax.swing.JOptionPane.showMessageDialog(this, "Thêm thất bại!");
        }
    }

    private void btnSuaActionPerformed(java.awt.event.ActionEvent evt) {
        String ma = txtMaLop.getText().trim();
        String ten = txtTenLop.getText().trim();
        if (ma.isEmpty() || ten.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this, "Vui lòng chọn lớp học cần sửa!");
            return;
        }
        KhoaHoc k = (KhoaHoc) cboKhoa.getSelectedItem();
        LopHoc lop = new LopHoc(ma, ten, k != null ? k.getMaKhoa() : null, (int) spSiSo.getValue());
        if (lopDAO.update(lop)) {
            javax.swing.JOptionPane.showMessageDialog(this, "Cập nhật lớp học thành công!");
            clearForm();
        } else {
            javax.swing.JOptionPane.showMessageDialog(this, "Cập nhật thất bại!");
        }
    }

    private void btnXoaActionPerformed(java.awt.event.ActionEvent evt) {
        String ma = txtMaLop.getText().trim();
        if (ma.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this, "Vui lòng chọn lớp học cần xóa!");
            return;
        }
        int ok = javax.swing.JOptionPane.showConfirmDialog(this, "Xóa lớp học " + ma + "?", "Xác nhận", javax.swing.JOptionPane.YES_NO_OPTION);
        if (ok == javax.swing.JOptionPane.YES_OPTION) {
            if (lopDAO.delete(ma)) {
                javax.swing.JOptionPane.showMessageDialog(this, "Xóa thành công!");
                clearForm();
            } else {
                javax.swing.JOptionPane.showMessageDialog(this, "Xóa thất bại! Lớp đang có sinh viên.");
            }
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnLamMoi;
    private javax.swing.JButton btnSua;
    private javax.swing.JButton btnThem;
    private javax.swing.JButton btnXoa;
    private javax.swing.JComboBox<KhoaHoc> cboKhoa;
    private javax.swing.JLabel lblKhoa;
    private javax.swing.JLabel lblMaLop;
    private javax.swing.JLabel lblSiSo;
    private javax.swing.JLabel lblTenLop;
    private javax.swing.JLabel lblTitle;
    private javax.swing.table.DefaultTableModel modelTable;
    private javax.swing.JPanel pnlActions;
    private javax.swing.JPanel pnlForm;
    private javax.swing.JScrollPane scrTable;
    private javax.swing.JSpinner spSiSo;
    private javax.swing.JTable tblLopHoc;
    private javax.swing.JTextField txtMaLop;
    private javax.swing.JTextField txtTenLop;
    // End of variables declaration//GEN-END:variables
}
